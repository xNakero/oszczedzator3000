package pl.pz.oszczedzator3000.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pz.oszczedzator3000.model.Role;
import pl.pz.oszczedzator3000.repo.RoleRepository;
import pl.pz.oszczedzator3000.service.RoleService;

import java.util.List;


@RestController
public class RoleController {

    private final RoleService roleService;
    private final RoleRepository roleRepository;

    public RoleController(RoleService roleService, RoleRepository roleRepository) {
        this.roleService = roleService;
        this.roleRepository = roleRepository;
    }

    @DeleteMapping("/role/delete/{id}")
    public ResponseEntity<Object> deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }

    @GetMapping("/role/details/{id}")
    public Role getRole(@PathVariable Long id) {
        if (roleRepository.findById(id).isPresent())
            return roleRepository.findById(id).get();
        else return null;
    }

    @GetMapping("/role/all")
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @PutMapping("/role/update/{id}") // nie updateuje
    public ResponseEntity<Object> updateRole(@PathVariable Long id, @RequestBody Role role) {
        return roleService.updateRole(id, role);
    }


}

