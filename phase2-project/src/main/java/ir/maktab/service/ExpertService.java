package ir.maktab.service;

import ir.maktab.data.enums.ExpertStatus;
import ir.maktab.data.enums.Role;
import ir.maktab.data.model.Expert;
import ir.maktab.data.repository.ExpertRepository;
import ir.maktab.util.exception.NotCorrect;
import ir.maktab.util.exception.NotFound;
import ir.maktab.util.exception.NotFoundUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpertService {
    public final static int MAX_SIZE = 300000;
    private final ExpertRepository expertRepository;

    public void signUp(Expert expert) {
        expert.setRole(Role.EXPORT);
        expert.setScore(0);
        expert.setExpertStatus(ExpertStatus.NEW);
        expert.setCredit(0);
        expertRepository.save(expert);
    }

    public Expert signIn(String email, String password) {
        Expert expert = expertRepository.findExpertByEmail(email).orElseThrow(() -> new NotFoundUser("This email is not exist"));
        if (!(expert.getPassword().equals(password)))
            throw new NotFoundUser("This user is not correct");
        return expert;
    }


    public Expert changePasswordExpert(String newPassword, String confirmedPassword, String email) {
        Expert expertByEmail = getExpertByEmail(email);
        if (!newPassword.equals(confirmedPassword))
            throw new NotCorrect("The new password and confirmed password must be match");
        expertByEmail.setPassword(newPassword);
        return expertRepository.save(expertByEmail);
    }

    public List<Expert> getAll() {
        return expertRepository.findAll();
    }

    public void delete(Expert expert) {
        Expert expert1 = getExpertByEmail(expert.getEmail());
        expert.setId(expert1.getId());
        expertRepository.delete(expert);
    }

    public Expert update(Expert expert) {
        Expert expert1 = getExpertByEmail(expert.getEmail());
        expert.setId(expert1.getId());
        return expertRepository.save(expert);
    }

    public Expert getExpertByEmail(String email) {
        return expertRepository.findExpertByEmail(email).orElseThrow(() -> new NotFoundUser("This email is not exist"));
    }

    public Expert getExpertById(Long id) {
        return expertRepository.findExpertById(id).orElseThrow(() -> new NotFoundUser("This user is not found"));
    }

    public String getPath(File file) {
        return file.getPath();
    }

    public void validImage(File file, Expert expert) throws IOException {
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

    public Expert saveImage(Long id) {
        File file = new File("C:\\Users\\HOME\\Downloads\\phase2-project\\phase2-project\\src\\main\\java\\ir\\maktab\\img.jpg");
        byte[] bFile = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Expert expert = getExpertById(id);
        expert.setImage(bFile);
        return expertRepository.save(expert);
    }

    public void getImage(String email) {
        Expert expert = getExpertByEmail(email);
        byte[] image = expert.getImage();
        try {
            FileOutputStream fos = new FileOutputStream("C:\\Users\\HOME\\Downloads\\phase2-project\\phase2-project\\src\\main\\java\\ir\\maktab\\image2.jpg");
            fos.write(image);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
