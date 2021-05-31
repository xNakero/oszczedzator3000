package pl.pz.oszczedzator3000.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pz.oszczedzator3000.dto.exception.ExceptionDto;
import pl.pz.oszczedzator3000.dto.jwt.JwtDto;
import pl.pz.oszczedzator3000.dto.user.AuthDto;
import pl.pz.oszczedzator3000.dto.user.UserDto;
import pl.pz.oszczedzator3000.dto.user.UsernameDto;
import pl.pz.oszczedzator3000.service.JwtService;
import pl.pz.oszczedzator3000.service.UserService;

import java.util.Base64;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final JwtService jwtService;
    private final UserService userService;

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
        if (token.equals("User is disabled")) {
            return new ResponseEntity<>(new ExceptionDto("User is disabled"), HttpStatus.CONFLICT);
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

    @PostMapping("auth")
    public ResponseEntity<?> confirmEmail(@RequestBody AuthDto authDto) {
        userService.confirmEmail(authDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("new-token")
    public ResponseEntity<?> newToken(@RequestBody UsernameDto usernameDto) {
        userService.resendToken(usernameDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
