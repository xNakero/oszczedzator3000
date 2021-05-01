package pl.pz.oszczedzator3000.dto.userpersonaldetails;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pz.oszczedzator3000.model.enums.Profession;
import pl.pz.oszczedzator3000.model.enums.RelationshipStatus;
import pl.pz.oszczedzator3000.model.enums.Sex;

@Getter
@Setter
public class UserPersonalDetailsDto {

    private double salary;
    private Profession profession;
    private int age;
    private Sex sex;
    @JsonAlias("relationship_status")
    @JsonProperty("relationship_status")
    private RelationshipStatus relationshipStatus;
    private int kids;

    public UserPersonalDetailsDto() {
        this.salary = -1.0;
        this.kids = -1;
    }

    public boolean hasInvalidData() {
        return salary < 0 || profession == null || age < 18 || sex == null || relationshipStatus == null || kids < 0;
    }
}
