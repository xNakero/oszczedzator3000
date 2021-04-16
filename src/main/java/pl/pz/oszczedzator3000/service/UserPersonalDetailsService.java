package pl.pz.oszczedzator3000.service;

import com.fasterxml.classmate.TypeBindings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pz.oszczedzator3000.dto.UserPersonalDetailsDto;
import pl.pz.oszczedzator3000.exceptions.UserNotFoundException;
import pl.pz.oszczedzator3000.exceptions.UserPersonalDetailsNotFoundException;
import pl.pz.oszczedzator3000.mapper.UserPersonalDetailsMapper;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.model.UserPersonalDetails;
import pl.pz.oszczedzator3000.repository.UserPersonalDetailsRepository;
import pl.pz.oszczedzator3000.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserPersonalDetailsService {

    private UserPersonalDetailsRepository userPersonalDetailsRepository;
    private UserRepository userRepository;
    private UserPersonalDetailsMapper userPersonalDetailsMapper;

    @Autowired
    public UserPersonalDetailsService(UserPersonalDetailsRepository userPersonalDetailsRepository,
                                      UserRepository userRepository,
                                      UserPersonalDetailsMapper userPersonalDetailsMapper) {
        this.userPersonalDetailsRepository = userPersonalDetailsRepository;
        this.userRepository = userRepository;
        this.userPersonalDetailsMapper = userPersonalDetailsMapper;
    }

    public UserPersonalDetailsDto getUserPersonalDetails(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        UserPersonalDetails userPersonalDetails = userPersonalDetailsRepository.findById(userId)
                .orElseThrow(() -> new UserPersonalDetailsNotFoundException(userId));
        return userPersonalDetailsMapper.mapToUserPersonalDetailsDto(userPersonalDetails);
    }

    public Optional<UserPersonalDetailsDto> postUserPersonalDetails(
            Long userId,
            UserPersonalDetailsDto userPersonalDetailsDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        if (userPersonalDetailsDto.hasInvalidData()) {
            return Optional.empty();
        }
        UserPersonalDetails userPersonalDetails = userPersonalDetailsMapper
                .mapToUserPersonalDetails(userPersonalDetailsDto);
        userPersonalDetails.setUserId(userId);
        userPersonalDetails.setUser(user);
        userPersonalDetailsRepository.save(userPersonalDetails);
        return Optional.of(userPersonalDetailsDto);
    }

    @Transactional
    public UserPersonalDetailsDto updateUserPersonalDetails(
            Long userId,
            UserPersonalDetailsDto userPersonalDetailsDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        UserPersonalDetails userPersonalDetails = userPersonalDetailsRepository.findById(userId)
                .orElseThrow(() -> new UserPersonalDetailsNotFoundException(userId));
        if (userPersonalDetailsDto.getAge() >= 18) {
            userPersonalDetails.setAge(userPersonalDetailsDto.getAge());
        }
        if (userPersonalDetailsDto.getKids() >= 0) {
            userPersonalDetails.setKids(userPersonalDetailsDto.getKids());
        }
        if (userPersonalDetailsDto.getSex() != null) {
            userPersonalDetails.setSex(userPersonalDetailsDto.getSex());
        }
        if (userPersonalDetailsDto.getProfession() != null) {
            userPersonalDetails.setProfession(userPersonalDetailsDto.getProfession());
        }
        if (userPersonalDetailsDto.getRelationshipStatus() != null) {
            userPersonalDetails.setRelationshipStatus(userPersonalDetailsDto.getRelationshipStatus());
        }
        if (userPersonalDetailsDto.getSalary() >= 0) {
            userPersonalDetails.setSalary(userPersonalDetailsDto.getSalary());
        }
        return userPersonalDetailsMapper.mapToUserPersonalDetailsDto(userPersonalDetails);
    }
}
