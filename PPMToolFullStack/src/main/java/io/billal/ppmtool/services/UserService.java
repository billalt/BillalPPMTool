package io.billal.ppmtool.services;

import io.billal.ppmtool.domain.User;
import io.billal.ppmtool.exceptions.UsernameExistException;
import io.billal.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser){


        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            //Username has to be unique (exception)
            User user = userRepository.findByUsername(newUser.getUsername());
            if(user != null){
                throw new UsernameExistException("Username '"+newUser.getUsername()+"' already exists");
            }
            newUser.setUsername(newUser.getUsername());
            // Make sure that password and confirmPassword match
            // We don't persist or show the confirmPassword
            newUser.setConfirmPassword("");
            return userRepository.save(newUser);

        }catch (Exception e){
            throw new UsernameExistException("Username '"+newUser.getUsername()+"' already exists");
        }
    }
}
