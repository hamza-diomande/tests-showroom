package com.oceane.dm.document.init;

import com.oceane.dm.document.model.User;
import com.oceane.dm.document.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserDataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0 || (userRepository.count() == 1 && userRepository.findByIdentifier("admin").isPresent()) ) {
            // Insertion des utilisateurs seulement si aucun utilisateur n'existe ou s'il y a un utilisateur avec l'identifier "admin"
            List<User> users = Arrays.asList(
                    new User("user1", passwordEncoder.encode("user1"), "User", "One", "Company A", "user1@example.com", User.Role.USER_ROLE),
                    new User("user2", passwordEncoder.encode("user2"), "User", "Two", "Company B", "user2@example.com", User.Role.USER_ROLE),
                    new User("user3", passwordEncoder.encode("user3"), "User", "Three", "Company C", "user3@example.com", User.Role.USER_ROLE),
                    new User("user4", passwordEncoder.encode("user4"), "User", "Four", "Company D", "user4@example.com", User.Role.USER_ROLE),
                    new User("user5", passwordEncoder.encode("user5"), "User", "Five", "Company E", "user5@example.com", User.Role.USER_ROLE),
                    new User("user6", passwordEncoder.encode("user6"), "User", "Six", "Company F", "user6@example.com", User.Role.USER_ROLE),
                    new User("user7", passwordEncoder.encode("user7"), "User", "Seven", "Company G", "user7@example.com", User.Role.USER_ROLE),
                    new User("user8", passwordEncoder.encode("user8"), "User", "Eight", "Company H", "user8@example.com", User.Role.USER_ROLE),
                    new User("user9", passwordEncoder.encode("user9"), "User", "Nine", "Company I", "user9@example.com", User.Role.USER_ROLE),
                    new User("user10", passwordEncoder.encode("user10"), "User", "Ten", "Company J", "user10@example.com", User.Role.USER_ROLE),
                    new User("user11", passwordEncoder.encode("user11"), "User", "Eleven", "Company K", "user11@example.com", User.Role.USER_ROLE),
                    new User("user12", passwordEncoder.encode("user12"), "User", "Twelve", "Company L", "user12@example.com", User.Role.USER_ROLE),
                    new User("user13", passwordEncoder.encode("user13"), "User", "Thirteen", "Company M", "user13@example.com", User.Role.USER_ROLE),
                    new User("user14", passwordEncoder.encode("user14"), "User", "Fourteen", "Company N", "user14@example.com", User.Role.USER_ROLE),
                    new User("user15", passwordEncoder.encode("user15"), "User", "Fifteen", "Company O", "user15@example.com", User.Role.USER_ROLE)
            );
            userRepository.saveAll(users);
        }
    }
}
