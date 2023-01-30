package ir.maktab.service;

import ir.maktab.data.model.Expert;
import ir.maktab.data.model.Suggestion;
import ir.maktab.data.repository.ExpertRepository;
import ir.maktab.util.exception.NotCorrect;
import ir.maktab.util.exception.NotFound;
import ir.maktab.util.exception.NotFoundUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpertService {
    public final static int MAX_SIZE = 300000;
    private final ExpertRepository expertRepository;
    private final SuggestionService suggestionService;

    public void signUp(Expert expert) {
        expertRepository.save(expert);
    }

    public Expert signIn(String email, String password) throws NotFoundUser {
        Expert expert = expertRepository.findExpertByEmail(email).orElseThrow(() -> new NotFoundUser("This email is not exist"));
        if (!(expert.getPassword().equals(password)))
            throw new NotFoundUser("This user is not correct");
        return expert;
    }

    public Expert changePassword(String newPassword, String confirmedPassword, Expert expert) throws NotCorrect {
        if (!newPassword.equals(confirmedPassword))
            throw new NotCorrect("The new password and confirmed password must be match");
        expert.setPassword(newPassword);
        return expertRepository.save(expert);
    }

    public List<Expert> getAll() {
        return expertRepository.findAll();
    }

    public void delete(Expert expert) {
        expertRepository.delete(expert);
    }

    public Expert update(Expert expert) {
        return expertRepository.save(expert);
    }

    public Expert getExpertByEmail(String email) throws NotFoundUser {
        return expertRepository.findExpertByEmail(email).orElseThrow(() -> new NotFoundUser("This email is not exist"));
    }

    public Expert getExpertById(Long id) throws NotFoundUser {
        return expertRepository.findExpertById(id).orElseThrow(() -> new NotFoundUser("This user is not found"));
    }

    public String getPath(File file) {
        return file.getPath();
    }

    public void validImage(File file, Expert expert) throws IOException, NotFound {
        ImageInputStream imageInputStream = ImageIO.createImageInputStream(file);
        Iterator<ImageReader> imageReadersList = ImageIO.getImageReaders(imageInputStream);
        if (!imageReadersList.hasNext()) {
            throw new NotFound("Image Readers not found");
        }
        ImageReader reader = imageReadersList.next();
        String formatName = reader.getFormatName();
        if (formatName.equals("jpg") && file.length() <= MAX_SIZE) {
            signUp(expert);
        }
        imageInputStream.close();
    }

    public Expert deposit(Expert expert, Suggestion suggestion) throws NotFound, NotFoundUser {
        Expert expertByEmail = getExpertByEmail(expert.getEmail());
        Suggestion suggestionById = suggestionService.getSuggestionById(suggestion.getId());
        double deposit = expertByEmail.getCredit() + suggestionById.getPrice();
        expert.setCredit(deposit);
        return update(expert);
    }

    public void saveImage(Expert expert) {
        File file = new File("C:\\Users\\HOME\\Downloads\\phase2-project\\phase2-project\\src\\main\\java\\ir\\maktab\\img.jpg");
        byte[] bFile = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        expert.setImage(bFile);
        expertRepository.save(expert);
    }

    public void getImage(String email) throws NotFoundUser {
        Expert expert = getExpertByEmail(email);
        byte[] image = expert.getImage();
        try{
            FileOutputStream fos = new FileOutputStream("C:\\Users\\HOME\\Downloads\\phase2-project\\phase2-project\\src\\main\\java\\ir\\maktab\\image2.jpg");
            fos.write(image);
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
