package org.example.services;

import org.example.entities.User;
import org.example.errors.UserNotFoundException;
import org.example.models.UserDetailsInfo;
import org.example.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = usersRepository.findByEmail(username);

        if (user == null) {
            throw new UserNotFoundException();
        }
        return new UserDetailsInfo(user);
    }
}
