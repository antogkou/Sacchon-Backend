package gr.pfizer.restapi.resource;

import gr.pfizer.restapi.exception.BadEntityException;
import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.model.Consults;
import gr.pfizer.restapi.model.MyUser;
import gr.pfizer.restapi.repository.ConsultsRepository;
import gr.pfizer.restapi.repository.UserRepository;
import gr.pfizer.restapi.repository.util.JpaUtil;
import gr.pfizer.restapi.representation.ConsultsRepresentation;
import gr.pfizer.restapi.resource.interfaces.ConsultsListResource;
import org.restlet.Request;
import org.restlet.data.Status;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.security.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static gr.pfizer.restapi.resource.ConsultsResourceImpl.LOGGER;

public class ConsultsListResourceImpl extends ServerResource implements ConsultsListResource {

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

    /**
     *
     * @return a  list of representations of all consults objects
     * @throws NotFoundException
     */
    public List<ConsultsRepresentation> getConsults() throws NotFoundException {
        LOGGER.finer("Get consults");
        try {
            List<Consults> consults = consultsRepository.findAll();
            List<ConsultsRepresentation> result = new ArrayList<>();

            consults.forEach(consult -> result.add(new ConsultsRepresentation(consult)));
            return result;
        } catch (Exception e) {
            throw new NotFoundException("consults not found");
        }
    }

    /**
     *
     * @param consultsRepresentation
     * @return a representation of a consult represantation
     * @throws BadEntityException
     */

    public ConsultsRepresentation add(ConsultsRepresentation consultsRepresentation) throws BadEntityException {
        LOGGER.finer("Add a new consult");

        try {
            String doctorEmail = Request.getCurrent().getClientInfo().getUser().toString();

            Optional<Consults> consultsOutput = this.addConsult(consultsRepresentation, doctorEmail);

            if(!userRepository.setHasActiveDoctor(consultsRepresentation.getPatient_email()))
                throw new BadEntityException("Couldn't update users field -- hasActiveDoctor");

            Consults consults;
            if (!consultsOutput.isPresent())
                throw new BadEntityException("Consult has not been created");

            consults = consultsOutput.get();

            ConsultsRepresentation result = new ConsultsRepresentation(consults);

            getResponse().setLocationRef(result.getUri());
            getResponse().setStatus(Status.SUCCESS_CREATED);

            LOGGER.finer("Consult successfully added");
            return result;
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error when adding the consult", ex);
            throw new ResourceException(ex);
        }
    }

    private Optional<Consults> addConsult(ConsultsRepresentation consultsRepresentation, String doctorEmail){
        MyUser doctor = userRepository.findByEmail(doctorEmail).get();
        Consults consultsInput = new Consults();
        consultsInput.setConsultText(consultsRepresentation.getConsultText());
        consultsInput.setMyUser(doctor);
        consultsInput.setConsult_created_date(consultsRepresentation.getConsult_date());
        consultsInput.setDosage(consultsRepresentation.getDosage());
        consultsInput.setMedication(consultsRepresentation.getMedication());
        consultsInput.setPatient_email(consultsRepresentation.getPatient_email());
        return consultsRepository.save(consultsInput);
    }
}
