package gr.pfizer.restapi.resource;

import gr.pfizer.restapi.model.MyUser;
import gr.pfizer.restapi.repository.UserRepository;
import gr.pfizer.restapi.repository.util.JpaUtil;
import gr.pfizer.restapi.representation.MyUserRepresentation;
import gr.pfizer.restapi.resource.interfaces.MyUserResourceAllGet;
import org.restlet.engine.Engine;
import org.restlet.resource.ServerResource;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MyUserResourceAllGetImpl extends ServerResource implements MyUserResourceAllGet {

    public static final Logger LOGGER = Engine.getLogger(MyUserResourceAllGetImpl.class);
    UserRepository userRepository;

    @Override
    protected void doInit(){
        try {
            userRepository = new UserRepository(JpaUtil.getEntityManager());
        }
        catch(Exception e) {
            LOGGER.info("Couldn't connect to DB");
            e.printStackTrace();
        }
    }

    @Override
    public List<MyUserRepresentation> getAllUsers() {

        LOGGER.info("+ ---- Retrieve all users for admin...");

        List<MyUser> users = userRepository.findAll();
        LOGGER.info("+ ---- Retrieve all users completed!");

        List<MyUserRepresentation> myUserRepresentation = new ArrayList<>();
        users.forEach(user -> myUserRepresentation.add (new MyUserRepresentation(user)));

        return myUserRepresentation;
    }


}
