package gr.pfizer.restapi.security;

import gr.pfizer.restapi.security.dao.ApplicationUser;
import gr.pfizer.restapi.security.dao.ApplicationUserPersistence;
import org.restlet.Request;
import org.restlet.security.Role;
import org.restlet.security.SecretVerifier;
import java.sql.SQLException;

public class CustomVerifier extends SecretVerifier {

    public int verify(String identifier, char[] secret)
            throws IllegalArgumentException {
        ApplicationUserPersistence applicationUserPersistence = ApplicationUserPersistence.getApplicationUserPersistence();
        ApplicationUser user = null;
        try {
            user = applicationUserPersistence.findById(identifier);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //user contains both user hints and roles
        if (user!=null
                && compare(user.getPassword().toCharArray(), secret)) {
            Request request = Request.getCurrent();
          
            request.getClientInfo().getRoles().add(new Role(user.getRole().getRoleName()));
          
            return SecretVerifier.RESULT_VALID;
        } else {
            return SecretVerifier.RESULT_INVALID;
        }
    }
}