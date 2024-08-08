package org.example.onlineaudiobook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class OnlineAudioBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineAudioBookApplication.class, args);
    }

}
