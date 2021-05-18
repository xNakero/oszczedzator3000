package pl.pz.oszczedzator3000;

import java.util.Random;

public class Constants {

    public static int SALARY_DEVIATION = 500;
    public static int AGE_DEVIATION = 3;
    public static int MONTH_DAYS_THEORETICAL = 30;
    public static int SEED = new Random().nextInt(Integer.MAX_VALUE);
    public static int TOKEN_VALIDATION_MINUTES = 10;
    public static String USERNAME_REGEX = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+[.][a-zA-Z]+$";
    public static String PASSWORD_REGEX = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+";
}
