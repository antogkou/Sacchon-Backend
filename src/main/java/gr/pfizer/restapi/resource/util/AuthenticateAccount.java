package gr.pfizer.restapi.resource.util;

import gr.pfizer.restapi.model.MyUser;
import gr.pfizer.restapi.repository.UserRepository;
import gr.pfizer.restapi.repository.util.JpaUtil;

public class AuthenticateAccount {

    private static UserRepository userRepository;

    public static boolean validateAccount(String email, String password){
        userRepository = new UserRepository(JpaUtil.getEntityManager());
        try {
            MyUser myUser = userRepository.findByEmail(email).get();
            if (myUser.getEmail().equals(email) && myUser.getPassword().equals(password))
                return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
