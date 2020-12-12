package org.example.repo;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.example.web.dto.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<User> repo = new ArrayList<>();

    public List<User> retrieveAll() {
        return new ArrayList<>(repo);
    }

    public void save(User user) {
        if (!user.getUsername().isEmpty() && !user.getPassword().isEmpty()) {
            repo.add(user);
            logger.info("Save new user: " + user);
        }
    }

    public boolean checkUserRegister(String login, String password) {
        for (User user : retrieveAll()) {
            if (user.getUsername().equals(login) && user.getPassword().equals(password)) {
                logger.info("Auth is completed: " + login + " " + password);
                return true;
            }
        }
        return false;
    }
}
