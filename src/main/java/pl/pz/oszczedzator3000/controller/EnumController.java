package pl.pz.oszczedzator3000.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pz.oszczedzator3000.model.enums.Category;
import pl.pz.oszczedzator3000.model.enums.Profession;
import pl.pz.oszczedzator3000.model.enums.RelationshipStatus;
import pl.pz.oszczedzator3000.model.enums.Sex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/enums")
public class EnumController {


    @GetMapping("{name}")
    public ResponseEntity<List<String>> getEnums(@PathVariable String name) {
        List<String> enums = new ArrayList<>();
        switch (name) {
            case "category":
                Arrays.asList(Category.values()).forEach(category -> enums.add(category.name()));
                break;
            case "profession":
                Arrays.asList(Profession.values()).forEach(category -> enums.add(category.name()));
                break;
            case "relationshipStatus":
                Arrays.asList(RelationshipStatus.values()).forEach(category -> enums.add(category.name()));
                break;
            case "sex":
                Arrays.asList(Sex.values()).forEach(category -> enums.add(category.name()));
                break;
            default:
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(enums, HttpStatus.OK);
    }
}