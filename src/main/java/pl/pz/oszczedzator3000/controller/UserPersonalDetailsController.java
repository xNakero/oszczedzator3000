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
@RequestMapping("api/v1/users")
public class UserPersonalDetailsController {

    private UserPersonalDetailsService userPersonalDetailsService;

    @Autowired
    public UserPersonalDetailsController(UserPersonalDetailsService userPersonalDetailsService) {
        this.userPersonalDetailsService = userPersonalDetailsService;
    }

    @PreAuthorize(value = "#userId == authentication.details")
    @GetMapping("{userId}/details")
    public ResponseEntity<UserPersonalDetailsDto> getUserPersonalDetails(@PathVariable Long userId) {
        return new ResponseEntity<>(userPersonalDetailsService.getUserPersonalDetails(userId), HttpStatus.OK);
    }

    @PreAuthorize(value = "#userId == authentication.details")
    @PostMapping("{userId}/details")
    public ResponseEntity<UserPersonalDetailsDto> postUserPersonalDetails(@PathVariable Long userId,
                                                                          @RequestBody UserPersonalDetailsDto userPersonalDetailsDto) {
        Optional<UserPersonalDetailsDto> userPersonalDetailsDtoOptional =
                userPersonalDetailsService.postUserPersonalDetails(userId, userPersonalDetailsDto);
        return userPersonalDetailsDtoOptional
                .map(personalDetailsDto -> new ResponseEntity<>(personalDetailsDto, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PreAuthorize(value = "#userId == authentication.details")
    @PutMapping("{userId}/details")
    public ResponseEntity<UserPersonalDetailsDto> updateUserPersonalDetails(@PathVariable Long userId,
                                                                            @RequestBody UserPersonalDetailsDto userPersonalDetailsDto) {
        return new ResponseEntity<>(
                userPersonalDetailsService.updateUserPersonalDetails(userId, userPersonalDetailsDto),
                HttpStatus.OK);
    }
}
