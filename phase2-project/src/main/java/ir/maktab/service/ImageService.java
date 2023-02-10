package ir.maktab.service;

import ir.maktab.data.model.Expert;
import ir.maktab.data.model.Image;
import ir.maktab.data.repository.ImageRepository;
import ir.maktab.util.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final ExpertService expertService;

    @Transactional
    public Expert uploadImage(MultipartFile file, Long id) throws IOException {
        Expert expert = expertService.getExpertById(id);
        if ((!Objects.requireNonNull(file.getContentType()).equalsIgnoreCase("image/jpeg")) || file.getSize() > 300000)
            throw new ValidationException("type must be jpg and size less than 300000");
        Image image = imageRepository.save(Image.builder().type(file.getContentType()).imageData(file.getBytes()).build());
        expert.setImage(image);
        expertService.update(expert);
        return expert;
    }

    @Transactional
    public byte[] getImage(String email) {
        Expert expert = expertService.getExpertByEmail(email);
        return expert.getImage().getImageData();
    }
}
