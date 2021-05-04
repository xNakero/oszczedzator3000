package pl.pz.oszczedzator3000.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pz.oszczedzator3000.dto.exception.ExceptionDto;
import pl.pz.oszczedzator3000.dto.jwt.JwtDto;
import pl.pz.oszczedzator3000.dto.user.UserAuthenticationDto;
import pl.pz.oszczedzator3000.service.JwtService;

@RestController
@RequestMapping("api/v1")
public class UserController {

    private JwtService jwtService;

    @Autowired
    public UserController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody UserAuthenticationDto userAuthenticationDto) {
        String token = jwtService.authenticate(userAuthenticationDto);
        if (token.equals("Bad credentials")) {
            return new ResponseEntity<>(new ExceptionDto(token), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new JwtDto(token), HttpStatus.OK);
    }

    @PostMapping("extend-token")
    public ResponseEntity<?> extendToken() {
        return new ResponseEntity<>(new JwtDto(jwtService.generateNewToken()), HttpStatus.OK);
    }
}
