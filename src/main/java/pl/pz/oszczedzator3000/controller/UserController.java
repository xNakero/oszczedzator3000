package pl.pz.oszczedzator3000.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pz.oszczedzator3000.dto.UserDTO;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.service.UserService;

import java.util.List;


@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/create") // jak tworzyc? (sety)
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/user/details/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/user/all")
    public List<UserDTO> getUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("user/delete/{id}") // usuwa wszystkich a nie jednego
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

}

