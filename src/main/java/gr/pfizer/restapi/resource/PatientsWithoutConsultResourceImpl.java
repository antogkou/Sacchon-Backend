package gr.pfizer.restapi.resource;

import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.model.MyUser;
import gr.pfizer.restapi.repository.UserRepository;
import gr.pfizer.restapi.repository.util.JpaUtil;
import gr.pfizer.restapi.representation.MyUserRepresentation;
import gr.pfizer.restapi.resource.interfaces.PatientsWithoutConsultResource;
import org.restlet.Request;
import org.restlet.engine.Engine;
import org.restlet.resource.ServerResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class PatientsWithoutConsultResourceImpl extends ServerResource implements PatientsWithoutConsultResource {

    public static final Logger LOGGER = Engine.getLogger(MyUserResourceImpl.class);
    private UserRepository userRepository;

    @Override
    protected void doInit() {
        LOGGER.info("+ ------ Initialising user resource starts");
        try {
            userRepository = new UserRepository(JpaUtil.getEntityManager());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("+ ------ Initialising user resource ends");
    }

    @Override
    public List<MyUserRepresentation> getPatientsWithoutConsult() throws NotFoundException {

        String doctorEmail = Request.getCurrent().getClientInfo().getUser().toString();

        try {
            Optional<List<MyUser>> patients = userRepository.findPatientsWithoutConsultByDoctorsEmail(doctorEmail);
            List<MyUserRepresentation> results = new ArrayList<>();
            if (patients.isPresent()){
                patients.get().forEach(patient -> results.add(new MyUserRepresentation(patient)));
            }else {
                throw new NotFoundException("Couldn't find the patients");
            }
            return results;
        }catch (Exception e){
            throw new NotFoundException("Couldn't find the patients - repository exception");
        }
    }
}
