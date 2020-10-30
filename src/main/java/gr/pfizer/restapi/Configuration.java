package gr.pfizer.restapi;

public class Configuration {

    public static final String USER_ROLE_PATIENT= "patient";
    public static final String USER_ROLE_DOCTOR= "doctor";
    public static final String USER_ROLE_ADMIN= "admin";

    /**
     * This variable used in the repository to retrieve the minimum measurements that a patients has to log in the system
     * in order to be able to get a consult from his/her doctor or being detected by a doctor.
     */
    public static final int MINIMUM_MEASUREMENTS_PER_MONTH = 30;

}
