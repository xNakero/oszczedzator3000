package pl.pz.oszczedzator3000.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Repository;
import pl.pz.oszczedzator3000.dto.userpersonaldetails.UserPersonalDetailsDto;
import pl.pz.oszczedzator3000.model.UserPersonalDetails;

@Mapper(componentModel = "spring")
@Repository
public interface UserPersonalDetailsMapper {

    UserPersonalDetailsDto mapToUserPersonalDetailsDto(UserPersonalDetails userPersonalDetails);

    UserPersonalDetails mapToUserPersonalDetails(UserPersonalDetailsDto userPersonalDetailsDto);

    @Mappings({
            @Mapping(source = "id", target = "userId")
    })
    UserPersonalDetails mapToUserPersonalDetailsWithId(UserPersonalDetailsDto userPersonalDetailsDto, Long id);
}
