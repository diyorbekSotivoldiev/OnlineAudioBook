package org.example.onlineaudiobook;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication

public class OnlineAudioBookApplication {



    public static void main(String[] args) {
        SpringApplication.run(OnlineAudioBookApplication.class, args);
    }


}
