package gr.pfizer.restapi.security;

import gr.pfizer.restapi.Configuration;
import org.restlet.Application;
import org.restlet.data.ChallengeScheme;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MemoryRealm;
import org.restlet.security.User;
import org.restlet.security.Verifier;


public class Shield {
    public static final String ROLE_ADMIN = Configuration.USER_ROLE_ADMIN;
    public static final String ROLE_DOCTOR = Configuration.USER_ROLE_DOCTOR;
    public static final String ROLE_PATIENT = Configuration.USER_ROLE_PATIENT;

    private Application application;

    public Shield(Application application) {
        this.application = application;
    }


    public ChallengeAuthenticator createApiGuard() {

        ChallengeAuthenticator apiGuard = new ChallengeAuthenticator(
                application.getContext(), ChallengeScheme.HTTP_BASIC, "realm");

        // - Verifier : checks authentication
        // - Enroler : to check authorization (roles)
        Verifier verifier = new CustomVerifier();
        apiGuard.setVerifier(verifier);

        return apiGuard;
    }
}
