package gr.pfizer.restapi.resource;

import gr.pfizer.restapi.model.Consults;
import gr.pfizer.restapi.model.MyUser;
import gr.pfizer.restapi.repository.ConsultsRepository;
import gr.pfizer.restapi.repository.UserRepository;
import gr.pfizer.restapi.repository.util.JpaUtil;
import gr.pfizer.restapi.representation.ConsultsRepresentation;
import gr.pfizer.restapi.representation.MyUserRepresentation;
import gr.pfizer.restapi.resource.interfaces.DoctorConsultResource;
import javassist.NotFoundException;
import org.restlet.Request;
import org.restlet.engine.Engine;
import org.restlet.resource.ServerResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class DoctorConsultResourceImpl extends ServerResource implements DoctorConsultResource {

    private ConsultsRepository consultsRepository;
    private UserRepository userRepository;
    public static final Logger LOGGER = Engine.getLogger(ConsultsResourceImpl.class);

    protected void doInit() {
        LOGGER.info("+ ------ Initialising consult resource starts");
        try {
            userRepository = new UserRepository(JpaUtil.getEntityManager());
            consultsRepository = new ConsultsRepository(JpaUtil.getEntityManager());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("+ ------ Initialising consult resource ends");
    }


    @Override
    public List<ConsultsRepresentation> getDoctorConsults() throws NotFoundException {
        String email = Request.getCurrent().getClientInfo().getUser().toString();
        Optional<List<Consults>> consultsList;
//        LOGGER.info("+ ---- Retrieve doctor's consults!");
//        setExisting(consultsList.isPresent());
        try {
            consultsList = consultsRepository.findDoctorConsults(email);
            List<ConsultsRepresentation> result = new ArrayList<>();

            consultsList.get().forEach(consult -> result.add(new ConsultsRepresentation(consult)));
            return result;
        } catch (Exception e) {
            throw new NotFoundException("consults not found");
        }
    }
}
