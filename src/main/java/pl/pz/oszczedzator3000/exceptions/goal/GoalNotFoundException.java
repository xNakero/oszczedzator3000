package pl.pz.oszczedzator3000.exceptions.goal;

public class GoalNotFoundException extends RuntimeException{

    public GoalNotFoundException(Long goalId) {
        super("Could not find goal with id = " + goalId);
    }
}
