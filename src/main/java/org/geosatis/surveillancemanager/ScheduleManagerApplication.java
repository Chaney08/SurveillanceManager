package org.geosatis.surveillancemanager;

import org.geosatis.surveillancemanager.model.User;
import org.geosatis.surveillancemanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.stream.Stream;

@SpringBootApplication
@ComponentScan(basePackages={"org.geosatis.surveillancemanager.controller", "org.geosatis.surveillancemanager.config", "org.geosatis.surveillancemanager.service", "org.geosatis.surveillancemanager.repository"
        , "org.geosatis.surveillancemanager.utils"})
public class ScheduleManagerApplication {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    public static void main(String[] args) {
        SpringApplication.run(ScheduleManagerApplication.class, args);
    }

    //This just creates base users for testing, add as many users as required to array
   /* @Bean
    CommandLineRunner init(UserRepository userRepository) {
        return args -> {
            Stream.of("user1").forEach(name -> {
                User user = new User(name, passwordEncoder.encode("123"),name);
                userRepository.save(user);
            });
            userRepository.findAll().forEach(System.out::println);
        };
    }*/
}
