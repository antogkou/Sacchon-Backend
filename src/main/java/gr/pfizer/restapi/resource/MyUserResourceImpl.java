package gr.pfizer.restapi.resource;


import gr.pfizer.restapi.exception.BadEntityException;
import gr.pfizer.restapi.exception.EmailExistException;
import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.exception.PasswordValidationException;
import gr.pfizer.restapi.model.MyUser;
import gr.pfizer.restapi.repository.util.JpaUtil;
import gr.pfizer.restapi.representation.MyUserRepresentation;
import gr.pfizer.restapi.resource.interfaces.MyUserResource;
import gr.pfizer.restapi.repository.UserRepository;
import gr.pfizer.restapi.resource.util.Password;
import gr.pfizer.restapi.resource.util.ResourceValidator;
import gr.pfizer.restapi.security.Role;
import org.restlet.Request;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class MyUserResourceImpl extends ServerResource implements MyUserResource {

    public static final Logger LOGGER = Engine.getLogger(MyUserResourceImpl.class);
    private String email;
    private UserRepository repository;


    @Override
    protected void doInit() {
        LOGGER.info("+ ------ Initialising user resource starts");
        try {
            repository = new UserRepository(JpaUtil.getEntityManager());
            email = getAttribute("email");
        }
        catch(Exception e) {
            email = null;
        }
        LOGGER.info("+ ------ Initialising user resource ends");
    }

    @Override
    public MyUserRepresentation getAccountDetails() throws NotFoundException{
        LOGGER.info("+ ------ Retrieve account details");
        String email = Request.getCurrent().getClientInfo().getUser().toString();

        Optional<MyUser> user = repository.findByEmail(email);
        MyUser myUser;
        setExisting(user.isPresent());
        if (!isExisting())
            throw new NotFoundException("+ ------- Couldn't find the user");
        else {
            myUser = user.get();
            return new MyUserRepresentation(myUser);
        }

    }

    @Override
    public MyUserRepresentation register(MyUserRepresentation myUserRepresentation) throws BadEntityException,
            PasswordValidationException, EmailExistException {
        LOGGER.info("+ ------ Registering user account!");
        ResourceValidator.notNull(myUserRepresentation);
        ResourceValidator.validate(myUserRepresentation);
        LOGGER.info("+ ------ Account checked!");

        new Password().validation(myUserRepresentation.getPassword());

        if (repository.findByEmail(myUserRepresentation.getEmail()).isPresent())
            throw new EmailExistException("Email already registered");

        // SETS explicit the role of a user
        myUserRepresentation.setUserRole(Role.ROLE_PATIENT.getRoleName());
        myUserRepresentation.setIsActive(1);

        LOGGER.info("+ ------ Account passed the validations!");
        try{
            repository.save(myUserRepresentation.createUser());
        }catch (Exception e){
            e.printStackTrace();
            throw new BadEntityException("Can't register the user");
        }

        LOGGER.info("+ ------ Account registered!");
        return myUserRepresentation;
    }

    @Override
    public MyUserRepresentation update(MyUserRepresentation myUserRepresentation) throws BadEntityException, PasswordValidationException, NotFoundException {
        LOGGER.info("+ ------ Validating user account!");
        ResourceValidator.notNull(myUserRepresentation);
        ResourceValidator.validate(myUserRepresentation);
        new Password().validation(myUserRepresentation.getPassword());
        LOGGER.info("+ ------ Account validated!");
        Optional<MyUser> account = repository.findByEmail(myUserRepresentation.createUser().getEmail());
        long id = account.get().getUser_id();
        setExisting(account.isPresent());
        try{
            if (isExisting()){
                LOGGER.info("+ ------ Account updating...");
                repository.update(id, myUserRepresentation.createUser());
                LOGGER.info("+ ------ Account updated successfully!");
                return myUserRepresentation;
            }
            else
                throw new NotFoundException("User doesn't exists! " + email);
        }catch (Exception e){
            e.printStackTrace();
            throw new NotFoundException("Resource exception!");
        }
    }

    @Override
    public MyUserRepresentation delete(MyUserRepresentation myUserRepresentation) throws BadEntityException, NotFoundException {
        LOGGER.info("+ ------ Deleting user account!");
        ResourceValidator.notNull(myUserRepresentation);
        ResourceValidator.validate(myUserRepresentation);
        Optional<MyUser> account = repository.findByEmail(myUserRepresentation.getEmail());
        setExisting(account.isPresent());

        try{
            if (isExisting()){
                long id = account.get().getUser_id();
                myUserRepresentation.setIsActive(0);
                repository.disableUser(id, myUserRepresentation.createUser());
                if(account.get().getUserRole().equals("doctor"));{
                String email = Request.getCurrent().getClientInfo().getUser().toString();
                    List<MyUserRepresentation> patients = new ArrayList<>();
                    Optional<List<MyUser>> users = repository.findDoctorPatients(email);
                    setExisting(users.isPresent());
                    try{
                        if (isExisting()){
                            users.get().forEach(user -> patients.add (new MyUserRepresentation(user)));
                            patients.stream()
                                    .forEach(patient ->{
                                        String patientEmail = patient.getEmail();
                                        Optional<MyUser> patientAccount = repository.findByEmail(patientEmail);
                                        long patientId = patientAccount.get().getUser_id();
                                        repository.disableUserDoctor(patientId, myUserRepresentation.createUser());
                                    });
                        }
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
            else
                throw new NotFoundException("Account does not exists!");
        }catch (Exception e){
            e.printStackTrace();
            throw new NotFoundException("Account didn't deleted!");
        }

        return myUserRepresentation;
    }
}
