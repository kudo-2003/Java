package run_main.spring_maven.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import run_main.spring_maven.entity.Image;
import run_main.spring_maven.repository.ImageRepository;

import java.util.List;

@RestController
public class HelloController {

    private final ImageRepository imageRepository;

    public HelloController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @GetMapping("/")
    public String sayhello() {
        return "Hello Back end!";
    }

    @GetMapping("/images")
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }
}