package gr.pfizer.restapi.resource;

import gr.pfizer.restapi.exception.BadEntityException;
import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.model.Consults;
import gr.pfizer.restapi.model.MyUser;
import gr.pfizer.restapi.repository.ConsultsRepository;
import gr.pfizer.restapi.repository.UserRepository;
import gr.pfizer.restapi.repository.util.JpaUtil;
import gr.pfizer.restapi.representation.ConsultsRepresentation;
import gr.pfizer.restapi.representation.MyUserRepresentation;
import gr.pfizer.restapi.resource.interfaces.DoctorResource;
import gr.pfizer.restapi.resource.util.InitParams;
import gr.pfizer.restapi.resource.util.ResourceValidator;
import org.restlet.Request;
import org.restlet.engine.Engine;
import org.restlet.resource.ServerResource;
import org.restlet.security.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class DoctorResourceImpl extends ServerResource implements DoctorResource {

    public static final Logger LOGGER = Engine.getLogger(MyUserResourceImpl.class);
    private UserRepository userRepository;
    private ConsultsRepository consultsRepository;
    private String patientEmail;

    @Override
    protected void doInit() {
        LOGGER.info("+ ------ Initialising user resource starts");
        try {
            userRepository = new UserRepository(JpaUtil.getEntityManager());
            consultsRepository = new ConsultsRepository(JpaUtil.getEntityManager());
            InitParams.initializeEmailParam(getQueryValue("email"));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("+ ------ Initialising user resource ends");
    }

    // TODO TESTING
    @Override
    public List<MyUserRepresentation> getDoctorPatients() throws BadEntityException {
        String email = Request.getCurrent().getClientInfo().getUser().toString();
        LOGGER.info("+ ---- Retrieve doctor's patients...");
        List<MyUserRepresentation> patients = new ArrayList<>();
        Optional<List<MyUser>> users = userRepository.findDoctorPatients(email);


        setExisting(users.isPresent());
        try{
            if (isExisting()){
                users.get().forEach(user -> patients.add (new MyUserRepresentation(user)));
            }
            else {
                throw new NotFoundException("Couldn't retrieve doctor's patients");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        LOGGER.info("+ ---- Retrieve doctor's patients completed!");
        return patients;
    }

    @Override
    public List<ConsultsRepresentation> getMyPatientConsults() {
        LOGGER.info("+ ---- Retrieve doctor's consults of: " + patientEmail);
        String doctorEmail = Request.getCurrent().getClientInfo().getUser().toString();
        List<ConsultsRepresentation> consults = new ArrayList<>();
        Optional<List<Consults>> patientConsults = consultsRepository.findAllDoctorConsultsByEmailForSpecifiedPatient(doctorEmail, patientEmail);

        LOGGER.info("+ ---- Retrieve doctor's consults!");
        setExisting(patientConsults.isPresent());
        try{
            if (isExisting())
                patientConsults.get().forEach(consult -> consults.add (new ConsultsRepresentation(consult)));
            else
                throw new NotFoundException("Couldn't retrieve doctor's patients");

        }catch (Exception e){
            e.printStackTrace();
        }
        return consults;
    }

}
