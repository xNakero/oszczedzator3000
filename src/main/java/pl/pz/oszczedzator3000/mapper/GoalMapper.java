package pl.pz.oszczedzator3000.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;
import pl.pz.oszczedzator3000.dto.goal.GoalRequestDto;
import pl.pz.oszczedzator3000.dto.goal.GoalResponseDetailsDto;
import pl.pz.oszczedzator3000.dto.goal.GoalResponseDto;
import pl.pz.oszczedzator3000.model.Goal;

@Mapper(componentModel = "spring")
@Repository
public interface GoalMapper {

    Goal mapToGoal(GoalRequestDto goalRequestDto);

    GoalResponseDto mapToGoalResponseDto(Goal goal);

    GoalResponseDetailsDto mapToGoalResponseDetailsDto(Goal goal);

}
