package ir.maktab.service;

import ir.maktab.data.model.Expert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageServiceInterface {

    Expert uploadImage(MultipartFile file, Long id) throws IOException;

    byte[] getImage(String email);
}
