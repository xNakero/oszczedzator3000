package pl.pz.oszczedzator3000.exceptions.goalanalyser;

public class ContributionHigherThanPriceException extends RuntimeException {

    public ContributionHigherThanPriceException() {
        super("Initial Contribution is higher than price");
    }
}
