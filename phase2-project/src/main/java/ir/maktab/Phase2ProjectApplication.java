package ir.maktab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class Phase2ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(Phase2ProjectApplication.class, args);
    }

}
