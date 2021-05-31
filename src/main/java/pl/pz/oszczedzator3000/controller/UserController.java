package pl.pz.oszczedzator3000.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pz.oszczedzator3000.dto.exception.ExceptionDto;
import pl.pz.oszczedzator3000.dto.jwt.JwtDto;
import pl.pz.oszczedzator3000.dto.user.*;
import pl.pz.oszczedzator3000.service.JwtService;
import pl.pz.oszczedzator3000.service.UserService;

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
    public ResponseEntity<?> register(@RequestBody RegistrationDto registrationDto) {
        userService.register(registrationDto);
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

    @PostMapping("logout-all")
    public ResponseEntity<?> logout() {
        userService.logoutAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeDto passwordChangeDto) {
        if (userService.changePassword(passwordChangeDto)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordFirstStepDto forgotPasswordFirstStepDto) {
        userService.forgotPassword(forgotPasswordFirstStepDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("forgot-password/new-password")
    public ResponseEntity<?> newPassword(@RequestBody ForgotPasswordSecondStepDto forgotPasswordSecondStepDto) {
        userService.newPassword(forgotPasswordSecondStepDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
