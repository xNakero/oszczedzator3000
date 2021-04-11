package pl.pz.oszczedzator3000.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.pz.oszczedzator3000.model.Role;
import pl.pz.oszczedzator3000.repo.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public ResponseEntity<Object> deleteRole(Long id) {
        if (roleRepository.findById(id).isPresent()) {
            if (roleRepository.getOne(id).getUsers().size() == 0) {
                roleRepository.deleteById(id);
                if (roleRepository.findById(id).isPresent()) {
                    return ResponseEntity.unprocessableEntity().body("Failed to delete the specified record");
                } else return ResponseEntity.ok().body("Successfully deleted specified record");
            } else
                return ResponseEntity.unprocessableEntity().body("Failed to delete,  Please delete the users associated with this role");
        } else
            return ResponseEntity.unprocessableEntity().body("No Records Found");
    }

    public ResponseEntity<Object> updateRole(Long id, Role role) {
        if (roleRepository.findById(id).isPresent()) {
            Role newRole = roleRepository.findById(id).get();
            newRole.setName(role.getName());
            Role savedRole = roleRepository.save(newRole);
            if (roleRepository.findById(savedRole.getRoleId()).isPresent())
                return ResponseEntity.accepted().body("Role saved successfully");
            else return ResponseEntity.badRequest().body("Failed to update Role");

        } else return ResponseEntity.unprocessableEntity().body("Specified Role not found");
    }
}



