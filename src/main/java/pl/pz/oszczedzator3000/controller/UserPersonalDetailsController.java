package pl.pz.oszczedzator3000.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.pz.oszczedzator3000.dto.userpersonaldetails.UserPersonalDetailsDto;
import pl.pz.oszczedzator3000.service.UserPersonalDetailsService;

import java.util.Optional;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class UserPersonalDetailsController {

    private final UserPersonalDetailsService userPersonalDetailsService;

    @Autowired
    public UserPersonalDetailsController(UserPersonalDetailsService userPersonalDetailsService) {
        this.userPersonalDetailsService = userPersonalDetailsService;
    }


    @GetMapping("details")
    public ResponseEntity<UserPersonalDetailsDto> getUserPersonalDetails() {
        return new ResponseEntity<>(userPersonalDetailsService.getUserPersonalDetails(), HttpStatus.OK);
    }

    @PostMapping("details")
    public ResponseEntity<UserPersonalDetailsDto> postUserPersonalDetails(@RequestBody UserPersonalDetailsDto userPersonalDetailsDto) {
        Optional<UserPersonalDetailsDto> userPersonalDetailsDtoOptional =
                userPersonalDetailsService.postUserPersonalDetails(userPersonalDetailsDto);
        return userPersonalDetailsDtoOptional
                .map(personalDetailsDto -> new ResponseEntity<>(personalDetailsDto, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PatchMapping("details")
    public ResponseEntity<UserPersonalDetailsDto> updateUserPersonalDetails(@RequestBody UserPersonalDetailsDto userPersonalDetailsDto) {
        return new ResponseEntity<>(
                userPersonalDetailsService.updateUserPersonalDetails(userPersonalDetailsDto),
                HttpStatus.OK);
    }
}
