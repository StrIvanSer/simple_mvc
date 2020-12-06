package org.example.app.services;

import org.example.repo.UserRepository;
import org.example.web.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepo;

    @Autowired
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> getAllUsers() {
        return userRepo.retrieveAll();
    }

    public boolean checkUserRegister(String login, String password){
        return userRepo.checkUserRegister(login, password);
    }

    public void saveUser(User user) {
        userRepo.save(user);
    }

}
