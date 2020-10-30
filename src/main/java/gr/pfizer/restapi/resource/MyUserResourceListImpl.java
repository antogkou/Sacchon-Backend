package gr.pfizer.restapi.resource;

import gr.pfizer.restapi.exception.BadEntityException;
import gr.pfizer.restapi.exception.LoginException;
import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.model.MyUser;
import gr.pfizer.restapi.repository.UserRepository;
import gr.pfizer.restapi.repository.util.JpaUtil;
import gr.pfizer.restapi.representation.LoginRepresentation;
import gr.pfizer.restapi.representation.MyUserRepresentation;
import gr.pfizer.restapi.resource.interfaces.MyUserResourceList;
import gr.pfizer.restapi.resource.util.AuthenticateAccount;
import gr.pfizer.restapi.resource.util.ResourceValidator;
import org.restlet.Request;
import org.restlet.engine.Engine;
import org.restlet.resource.ServerResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class MyUserResourceListImpl extends ServerResource implements MyUserResourceList {

    public static final Logger LOGGER = Engine.getLogger(MyUserResourceImpl.class);
    UserRepository userRepository;

    @Override
    protected void doInit(){
        try {
            userRepository = new UserRepository(JpaUtil.getEntityManager());
//            u_id = Long.parseLong(getAttribute("u_id"));
        }
        catch(Exception e) {
            LOGGER.info("Couldn't connect to DB");
            e.printStackTrace();
        }
    }


    @Override
    public List<MyUserRepresentation> getPatientsWithoutDoctor() throws BadEntityException {
        LOGGER.info("+ ---- Retrieve users without doctor advisor...");
        List<MyUserRepresentation> usersWithoutDoctor = new ArrayList<>();
        Optional<List<MyUser>> users = userRepository.findPatientsWithoutDoctor();
        ResourceValidator.notNull(users);
        LOGGER.info("+ ---- Retrieve users without doctor advisor completed!");
        setExisting(users.isPresent());
        try{
            if (isExisting()){
                users.get().forEach(user -> usersWithoutDoctor.add (new MyUserRepresentation(user)));
            }
            else {
                throw new NotFoundException("Couldn't retrieve patients without doctor");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return usersWithoutDoctor;
    }

    @Override
    public MyUserRepresentation login(LoginRepresentation loginRepresentation) throws LoginException {
        if (!AuthenticateAccount.validateAccount(loginRepresentation.getEmail(), loginRepresentation.getPassword()))
            throw new LoginException();
        return new MyUserRepresentation(userRepository.findByEmail(loginRepresentation.getEmail()).get());
    }
}
