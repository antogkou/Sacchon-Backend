package gr.pfizer.restapi.representation;

import gr.pfizer.restapi.model.MyUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.restlet.engine.Engine;

import java.util.Date;
import java.util.logging.Logger;


@Data
@NoArgsConstructor
public class MyUserRepresentation {

    public static final Logger LOGGER = Engine.getLogger(MyUserRepresentation.class);

    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zipCode;
    private String phoneNumber;
    private String email;
    private String password;
    private String userRole;
    private Date createdDate;
    private int isActive;
    private int hasActiveDoctor;

    private String uri;

    public MyUserRepresentation(MyUser user) {
        LOGGER.finer("+---------  Create User representation Router");
        if (user != null) {
            firstName = user.getFirstName();
            lastName = user.getLastName();
            address = user.getAddress();
            city = user.getCity();
            zipCode = user.getZipCode();
            phoneNumber = user.getPhoneNumber();
            email = user.getEmail();
            password = user.getPassword();
            userRole = user.getUserRole();
            createdDate = user.getCreatedDate();
            hasActiveDoctor = user.getHasActiveDoctor();
            isActive = user.getIsActive();
            uri = "http://localhost:9000/v1/team6/user/" + user.getUser_id();
        }
    }

    public MyUser createUser() {
        LOGGER.finer("+---------  Create User");
        MyUser user = new MyUser();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAddress(address);
        user.setCity(city);
        user.setZipCode(zipCode);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setPassword(password);
        user.setUserRole(userRole);
        user.setCreatedDate(createdDate);
        user.setIsActive(isActive);

        return user;
    }

}


