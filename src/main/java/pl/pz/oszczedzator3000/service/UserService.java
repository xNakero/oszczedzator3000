package pl.pz.oszczedzator3000.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.pz.oszczedzator3000.dto.UserDTO;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.repo.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(obj -> modelMapper.map(obj, UserDTO.class))
                .collect(Collectors.toList());
    }

    // Create new User
    public ResponseEntity<Object> createUser(User model) {
        User user = new User();
        if (userRepository.findByEmail(model.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("The Email is already Present, Failed to Create new User");
        } else {
            user.setUserId(model.getUserId());
            user.setUsername(model.getUsername());
            user.setPassword(model.getPassword());
            user.setEmail(model.getEmail());
            user.setAuthorities(model.getAuthorities());
            user.setExpenses(model.getExpenses());
            user.setGoals(model.getGoals());
            user.setCreationDate(model.getCreationDate());

            User savedUser = userRepository.save(user);
            if (userRepository.findById(savedUser.getUserId()).isPresent())
                return ResponseEntity.ok("User Created Successfully");
            else return ResponseEntity.unprocessableEntity().body("Failed Creating User as Specified");
        }
    }

    // Delete User
    public ResponseEntity<Object> deleteUser(Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            if (userRepository.findById(id).isPresent())
                return ResponseEntity.unprocessableEntity().body("Failed to Delete the specified User");
            else return ResponseEntity.ok().body("Successfully deleted the specified user");
        } else return ResponseEntity.badRequest().body("Cannot find the user specified");
    }

    public User getUser(Long id) {
        if (userRepository.findById(id).isPresent()) {
            User user = userRepository.findById(id).get();
            User userModel = new User();
            userModel.setUserId(user.getUserId());
            userModel.setUsername(user.getUsername());
            userModel.setPassword(user.getPassword());
            userModel.setEmail(user.getEmail());
            userModel.setAuthorities(user.getAuthorities()); // ?
            userModel.setExpenses(user.getExpenses());
            userModel.setGoals(user.getGoals());
            userModel.setCreationDate(user.getCreationDate());
            return userModel;
        } else return null;
    }

    public List<User> getUsers() {
        List<User> userList = userRepository.findAll();
        if (userList.size() > 0) {
            List<User> userModels = new ArrayList<>();
            for (User user : userList) {
                User model = new User();
                model.setUserId(user.getUserId());
                model.setUsername(user.getUsername());
                model.setPassword(user.getPassword());
                model.setEmail(user.getEmail());
                model.setAuthorities(user.getAuthorities()); // ?
                model.setExpenses(user.getExpenses());
                model.setGoals(user.getGoals());
                model.setCreationDate(user.getCreationDate());
                userModels.add(model);
            }
            return userModels;
        } else return new ArrayList<User>();
    }

}
















