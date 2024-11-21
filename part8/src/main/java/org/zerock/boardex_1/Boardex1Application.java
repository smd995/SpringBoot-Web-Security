package org.zerock.boardex_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Boardex1Application {

    public static void main(String[] args) {
        SpringApplication.run(Boardex1Application.class, args);
    }

}
