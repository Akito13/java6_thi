package com.java6.nhom1.rest.controller;

import com.java6.nhom1.model.User;
import com.java6.nhom1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class AdminRestController {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("user/{id}")
    public ResponseEntity<List<User>> getUser(@PathVariable Optional<String> id){
        if(id.isEmpty())
            return ResponseEntity.badRequest().body(null);
        Optional<User> result = userRepo.findById(id.get());
        return result.map(user -> ResponseEntity.ok(List.of(user)))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(null));
    }

    @PostMapping("user")
    public ResponseEntity<List<User>> createUser(@Validated @RequestBody User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepo.save(user);
            return new ResponseEntity<>(List.of(savedUser), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PutMapping("user")
    public ResponseEntity<List<User>> updateUser(@Validated @RequestBody User user){
        try {
            System.out.println("From updateUser rest: " + user.getPassword());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepo.save(user);
            return new ResponseEntity<>(List.of(savedUser), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @DeleteMapping("user/{id}")
    public ResponseEntity<List<User>> deleteUser(@PathVariable Optional<String> id){
        if(id.isEmpty())
            return ResponseEntity.badRequest().body(null);
        try {
            userRepo.deleteById(id.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("user/search")
    public ResponseEntity<List<User>> searchUser(@RequestParam("id") Optional<String> id){
        List<User> users = userRepo.findAllByIdLike("%" + id.orElse("") + "%");
        users.stream().forEach(user -> System.out.println(user.getId()));
        System.out.println("REST SEARCH: " + id);
        return ResponseEntity.ok(users);
    }

    @GetMapping("users")
    public ResponseEntity<List<User>> loadUsers(){
        return ResponseEntity.ok(userRepo.findAll());
    }

}
