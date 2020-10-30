package gr.pfizer.restapi.resource;


import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.model.Consults;
import gr.pfizer.restapi.model.Measurement;
import gr.pfizer.restapi.model.MyUser;
import gr.pfizer.restapi.repository.ConsultsRepository;
import gr.pfizer.restapi.repository.MeasurementRepository;
import gr.pfizer.restapi.repository.UserRepository;
import gr.pfizer.restapi.repository.util.JpaUtil;
import gr.pfizer.restapi.representation.ConsultsRepresentation;
import gr.pfizer.restapi.representation.MeasurementRepresentation;
import gr.pfizer.restapi.representation.MyUserRepresentation;
import gr.pfizer.restapi.resource.interfaces.AdminResource;
import gr.pfizer.restapi.resource.util.InitParams;
import org.restlet.engine.Engine;
import org.restlet.resource.ServerResource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * date params must be in format YYYY-MM-DD e.g.: 2020-05-12
 *
 *
 */
public class AdminResourceImpl extends ServerResource implements AdminResource {

    public static final Logger LOGGER = Engine.getLogger(AdminResourceImpl.class);
    private long u_id;
    private UserRepository userRepository;
    private MeasurementRepository measurementRepository;
    private ConsultsRepository consultsRepository;
    private String email;
    private Date startDate;
    private Date endDate;

    @Override
    protected void doInit() {
        LOGGER.info("+ ------ Initialising admin resource starts");
        try {
            userRepository = new UserRepository(JpaUtil.getEntityManager());
            measurementRepository = new MeasurementRepository(JpaUtil.getEntityManager());
            consultsRepository = new ConsultsRepository(JpaUtil.getEntityManager());
            email = InitParams.initializeEmailParam(getQueryValue("email"));
            startDate = InitParams.initializeDatesParam(getQueryValue("from"));
            endDate = InitParams.initializeDatesParam(getQueryValue("to"));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("+ ------ Initialising admin resource ends");
    }

    @Override
    public List<MeasurementRepresentation> getPatientData() throws NotFoundException {
        LOGGER.info("+ ------ Retrieve patient's data");

        // TODO admin previleges
        try{
            List<Measurement> measurements = measurementRepository.findAllByEmail(email);
            List<MeasurementRepresentation> result = new ArrayList<>();
            measurements.forEach(measurement -> result.add(new MeasurementRepresentation(measurement)));
            return result;
        }catch (Exception e){
            throw new NotFoundException("+ ------- Couldn't find user's measurements!!");
        }
    }

    @Override
    public List<Consults> getDoctorData() throws NotFoundException {
        LOGGER.info("+ ------ Retrieve doctor's data");

        // TODO admin privileges

        try{
            List<Consults> consults = consultsRepository.findAllDoctorConsultsByEmail(email);
            List<ConsultsRepresentation> result = new ArrayList<>();
            consults.forEach(consult -> result.add(new ConsultsRepresentation(consult)));
            return consults;
        }catch (Exception e){
            throw new NotFoundException("+ ------- Couldn't find doctor's data!!");
        }
    }

    @Override
    public List<MyUserRepresentation> getInactiveUsers() throws NotFoundException {

        LOGGER.info("+ ------ Retrieve inactive users!!");

        // TODO admin privileges
        try{
            Optional<List<MyUser>> users = userRepository.findInactiveUsers(startDate, endDate);
            if( users.isPresent()){
                LOGGER.info("+ ------ Inactive users retrieved!!");
                List<MyUserRepresentation> result = new ArrayList<>();
                users.get().forEach(user -> result.add( new MyUserRepresentation(user)));
                return result;
            }
            return null;
        }catch (Exception e){
            throw new NotFoundException("+ ------- Couldn't find users's data!!");
        }
    }

    @Override
    public List<MyUserRepresentation> getAllUsersWithoutConsult() throws NotFoundException {
        LOGGER.info("+ ------ Retrieve inactive users!!");

        // TODO admin privileges

        try{
            Optional<List<MyUser>> users = userRepository.findAllPatientsWithoutConsult();
            if(users.isPresent()){
                LOGGER.info("+ ------ All users without consult retrieved!!");
                List<MyUserRepresentation> result = new ArrayList<>();
                users.get().forEach(user -> result.add( new MyUserRepresentation(user)));
                return result;
            }
            else
                throw new NotFoundException("+ ------- There are no users who needs consultation!");
        }catch (Exception e){
            throw new NotFoundException("+ ------- Couldn't find users!!");
        }
    }
}
