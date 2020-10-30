package gr.pfizer.restapi.resource;

import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.model.Consults;
import gr.pfizer.restapi.repository.ConsultsRepository;
import gr.pfizer.restapi.repository.UserRepository;
import gr.pfizer.restapi.repository.util.JpaUtil;
import gr.pfizer.restapi.representation.ConsultsRepresentation;
import gr.pfizer.restapi.resource.interfaces.PatientResource;
import gr.pfizer.restapi.security.Shield;
import org.restlet.Request;
import org.restlet.engine.Engine;
import org.restlet.resource.ServerResource;
import org.restlet.security.User;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PatientResourceImpl extends ServerResource implements PatientResource {

    private ConsultsRepository consultsRepository;
    private UserRepository userRepository;
    public static final Logger LOGGER = Engine.getLogger(ConsultsListResourceImpl.class);

    /**
     * initializes consult resource
     */
    protected void doInit() {
        LOGGER.info("Initialising ConsultsListResourceImpl resource starts");
        try {
            consultsRepository = new ConsultsRepository(JpaUtil.getEntityManager());
            userRepository = new UserRepository(JpaUtil.getEntityManager());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ConsultsRepresentation> getAllPatientsConsults() throws NotFoundException {

        String email = Request.getCurrent().getClientInfo().getUser().toString();

        try{
            List<Consults> consults = consultsRepository.findAllPatientsConsultsByEmail(email);
            List<ConsultsRepresentation> result = new ArrayList<>();
            consults.forEach(consult -> result.add(new ConsultsRepresentation(consult)));
            return result;
        }catch (Exception e){
            throw new NotFoundException("Couldn't retrieve consults");
        }
    }
}
