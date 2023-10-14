package com.samuel.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;
    
    
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody UserModel userModel) {
        var usuarioOptional = userRepository.findByUserName(userModel.getUserName());

        if (usuarioOptional != null) 
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O usuário já existe");

        String hashPassword = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(hashPassword);

        UserModel save = userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
        
    }
}
