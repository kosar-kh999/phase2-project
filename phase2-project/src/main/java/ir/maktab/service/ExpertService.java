package ir.maktab.service;

import ir.maktab.data.enums.ExpertStatus;
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
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpertService {
    public final static int MAX_SIZE = 300000;
    private final ExpertRepository expertRepository;

    public void signUp(Expert expert) {
        expertRepository.save(expert);
    }

    public Optional<Expert> signIn(String email, String password) throws NotFoundUser {
        Expert expert = expertRepository.findExpertByEmail(email).orElseThrow(() -> new NotFoundUser("This email is not exist ! "));
        if (!(expert.getPassword().equals(password)))
            throw new NotFoundUser("This user is not correct");
        return Optional.ofNullable(expert);
    }

    public void changePassword(String newPassword, String confirmedPassword, Expert expert) throws NotCorrect {
        if (!newPassword.equals(confirmedPassword))
            throw new NotCorrect("The new password and confirmed password must be match");
        expert.setPassword(newPassword);
        expertRepository.save(expert);
    }

    public List<Expert> getAll() {
        return expertRepository.findAll();
    }

    public void delete(Expert expert) {
        expertRepository.delete(expert);
    }

    public Expert getStatus(ExpertStatus expertStatus) {
        return expertRepository.getExpertByExpertStatus(expertStatus);
    }

    public void update(Expert expert) {
        expertRepository.save(expert);
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
}
