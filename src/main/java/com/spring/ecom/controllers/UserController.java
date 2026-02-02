package com.spring.ecom.controllers;

import com.spring.ecom.dtos.UserDTO;
import com.spring.ecom.dtos.UserMapper;
import com.spring.ecom.entities.User;
import com.spring.ecom.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    @GetMapping
    public List<UserDTO> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO > getByUserId(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(UserMapper.mapToDTO(user));
    }
}
