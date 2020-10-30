package gr.pfizer.restapi.resource;

import gr.pfizer.restapi.exception.BadEntityException;
import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.model.Measurement;
import gr.pfizer.restapi.model.MyUser;
import gr.pfizer.restapi.repository.MeasurementRepository;
import gr.pfizer.restapi.repository.UserRepository;
import gr.pfizer.restapi.repository.util.JpaUtil;
import gr.pfizer.restapi.representation.MeasurementRepresentation;
import gr.pfizer.restapi.resource.interfaces.MeasurementResourceList;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.security.SecretVerifier;
import org.restlet.security.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MeasurementResourceListImpl
        extends ServerResource implements MeasurementResourceList {

    public static final Logger LOGGER = Engine.getLogger(MeasurementResourceListImpl.class);
    private MeasurementRepository measurementRepository;
    private UserRepository userRepository;

    /**
     * Initializes the measurement resource
     */
    @Override
    protected void doInit() {
        LOGGER.info("Initialising measurement resource starts");
        try {
            measurementRepository =
                    new MeasurementRepository(JpaUtil.getEntityManager());
            userRepository = new UserRepository(JpaUtil.getEntityManager());
        } catch (Exception e) {
        }
        LOGGER.info("Initialising product resource ends");
    }

    /**
     *
     * @return all measurements
     * @throws NotFoundException
     */

    public List<MeasurementRepresentation> getMeasurements() throws NotFoundException {
        Request request = Request.getCurrent();
        String email = request.getClientInfo().getUser().toString();
        LOGGER.finer("Select all measurements.");
        // Check authorization [doctor for testing]
        // ResourceUtils.checkRole(this, Shield.ROLE_DOCTOR);
        try {
            List<Measurement> measurements = measurementRepository.findAllByEmail(email);
            List<MeasurementRepresentation> result = new ArrayList<>();
            measurements.forEach(measurement -> result.add(new MeasurementRepresentation(measurement)));

            //small test method calls
            System.out.println(averageOfCarbIntake(measurements));
            System.out.println(averageOfGlucose(measurements));
            //end of test, methods must be moved appropriately and called with date parameters

            return result;
        } catch (Exception e) {
            throw new NotFoundException("measurements not found");
        }
    }

    /**
     *
     * @param measurementRepresentation
     * @return a representation of a measurement object
     * @throws BadEntityException
     */

    @Override
    public MeasurementRepresentation add(MeasurementRepresentation measurementRepresentation)
            throws BadEntityException {
        LOGGER.finer("Add a new measurement.");
        // Check authorization
//        ResourceUtils.checkRole(this, Shield.ROLE_DOCTOR);
//        LOGGER.finer("User allowed to add a measurement.");

        // Check entity
//        ResourceValidator.notNull(measurementRepresentation);
//        ResourceValidator.validate(measurementRepresentation);
//        LOGGER.finer("Measurement checked");

        try {

            String email = Request.getCurrent().getClientInfo().getUser().toString();
            Measurement measurementIn = measurementRepresentation.createMeasurement();
            //set user id when adding a measurement
            measurementIn.setMyUser(userRepository.findByEmail(email).get());

            Optional<Measurement> measurementOut = measurementRepository.save(measurementIn);

            if (!measurementOut.isPresent())
                throw new BadEntityException(" Measurement has not been created");

            MeasurementRepresentation result = new MeasurementRepresentation(measurementOut.get());

            getResponse().setLocationRef(result.getUri());
            getResponse().setStatus(Status.SUCCESS_CREATED);

            LOGGER.finer("Measurement successfully added.");
            return result;
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error when adding a measurement", ex);
            throw new ResourceException(ex);
        }
    }


    public double averageOfCarbIntake(List<Measurement> measurements) {
        double sumOfCarbIntake, averageOfCarbIntake;
        int n = measurements.size();
        sumOfCarbIntake = measurements.stream().mapToDouble(Measurement::getCarb_intake).sum();
        averageOfCarbIntake = sumOfCarbIntake / n;
        return averageOfCarbIntake;
    }

    public double averageOfGlucose(List<Measurement> measurements) {
        double sumOfGlucoseLevel, averageOfGlucoseLevel;
        int n = measurements.size();
        sumOfGlucoseLevel = measurements.stream().mapToDouble(Measurement::getGlucose_level).sum();
        averageOfGlucoseLevel = sumOfGlucoseLevel / n;
        return averageOfGlucoseLevel;
    }
}
