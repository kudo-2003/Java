package run_main.spring_maven.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import run_main.spring_maven.entity.Image;


public interface ImageRepository extends JpaRepository <Image, Long>{
    
}
