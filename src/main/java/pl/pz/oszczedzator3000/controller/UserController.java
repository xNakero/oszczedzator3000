package pl.pz.oszczedzator3000.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pz.oszczedzator3000.dto.exception.ExceptionDto;
import pl.pz.oszczedzator3000.dto.jwt.JwtDto;
import pl.pz.oszczedzator3000.dto.user.UserDto;
import pl.pz.oszczedzator3000.service.JwtService;
import pl.pz.oszczedzator3000.service.UserService;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private JwtService jwtService;
    private UserService userService;

    @Autowired
    public UserController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        String token = jwtService.authenticate(userDto);
        if (token.equals("Bad credentials")) {
            return new ResponseEntity<>(new ExceptionDto(token), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new JwtDto(token), HttpStatus.OK);
    }

    @PostMapping("token-extension")
    public ResponseEntity<?> extendToken() {
        return new ResponseEntity<>(new JwtDto(jwtService.generateNewToken()), HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        userService.register(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
