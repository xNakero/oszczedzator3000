package pl.pz.oszczedzator3000.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pz.oszczedzator3000.dto.userpersonaldetails.UserPersonalDetailsDto;
import pl.pz.oszczedzator3000.service.UserPersonalDetailsService;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/users")
public class UserPersonalDetailsController {

    private UserPersonalDetailsService userPersonalDetailsService;

    @Autowired
    public UserPersonalDetailsController(UserPersonalDetailsService userPersonalDetailsService) {
        this.userPersonalDetailsService = userPersonalDetailsService;
    }

    @GetMapping("{userId}/details")
    public ResponseEntity<UserPersonalDetailsDto> getUserPersonalDetails(@PathVariable Long userId) {
        return new ResponseEntity<>(userPersonalDetailsService.getUserPersonalDetails(userId), HttpStatus.OK);
    }

    @PostMapping("{userId}/details")
    public ResponseEntity<UserPersonalDetailsDto> postUserPersonalDetails(@PathVariable Long userId,
                                                                          @RequestBody UserPersonalDetailsDto userPersonalDetailsDto) {
        Optional<UserPersonalDetailsDto> userPersonalDetailsDtoOptional =
                userPersonalDetailsService.postUserPersonalDetails(userId, userPersonalDetailsDto);
        return userPersonalDetailsDtoOptional
                .map(personalDetailsDto -> new ResponseEntity<>(personalDetailsDto, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PutMapping("{userId}/details")
    public ResponseEntity<UserPersonalDetailsDto> updateUserPersonalDetails(@PathVariable Long userId,
                                                                            @RequestBody UserPersonalDetailsDto userPersonalDetailsDto) {
        return new ResponseEntity<>(
                userPersonalDetailsService.updateUserPersonalDetails(userId, userPersonalDetailsDto),
                HttpStatus.OK);
    }
}
