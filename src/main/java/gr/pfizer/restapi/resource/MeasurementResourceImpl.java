package gr.pfizer.restapi.resource;

import gr.pfizer.restapi.exception.BadEntityException;
import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.model.Measurement;
import gr.pfizer.restapi.repository.MeasurementRepository;
import gr.pfizer.restapi.repository.util.JpaUtil;
import gr.pfizer.restapi.representation.MeasurementRepresentation;
import gr.pfizer.restapi.resource.interfaces.MeasurementResource;
import gr.pfizer.restapi.security.ResourceUtils;
import gr.pfizer.restapi.security.Role;
import gr.pfizer.restapi.security.Shield;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MeasurementResourceImpl
        extends ServerResource implements MeasurementResource {

    public static final Logger LOGGER = Engine.getLogger(MeasurementResourceImpl.class);
    private long id;
    private MeasurementRepository measurementRepository;

    /**
     * initializes measurement resource
     */
    @Override
    protected void doInit() {
        LOGGER.info("Initialising measurement resource starts");
        try {
            measurementRepository =
                    new MeasurementRepository(JpaUtil.getEntityManager()) ;
            id = Long.parseLong(getAttribute("id"));
        }
        catch(Exception e)
        {
            id =-1;
        }
        LOGGER.info("Initialising measurement resource ends");
    }

    /**
     *
     * @return a represantation of a measurement object
     * @throws NotFoundException
     */
    @Override
    public MeasurementRepresentation getMeasurement()
            throws NotFoundException {
        LOGGER.info("Retrieve a measurement");

        // Check authorization
        ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);

        // Initialize the persistence layer.
        MeasurementRepository measurmentRepository = new MeasurementRepository(JpaUtil.getEntityManager());
        Measurement measurement;
        try {
            Optional<Measurement> omeasurement = measurmentRepository.findById(id);
            setExisting(omeasurement.isPresent());
            if (!isExisting()) {
                LOGGER.config("measurement id does not exist:" + id);
                throw new NotFoundException("No measurement with  : " + id);
            } else {
                measurement = omeasurement.get();
                LOGGER.finer("User allowed to retrieve a measurement.");
                //System.out.println(  userId);
                MeasurementRepresentation result =
                        new MeasurementRepresentation(measurement);
                LOGGER.finer("Measurement successfully retrieved");
                return result;
            }
        } catch (Exception ex) {
            throw new ResourceException(ex);
        }
    }

    /**
     *
     * @param measureRepIn
     * @return a represantation of a measurement object
     * @throws NotFoundException
     * @throws BadEntityException
     */
    @Override
    public MeasurementRepresentation store(MeasurementRepresentation measureRepIn)
            throws NotFoundException, BadEntityException {
        LOGGER.finer("Update a product.");
        ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);
        LOGGER.finer("User allowed to update a product.");
//        ResourceValidator.notNull(measureRepIn);
//        ResourceValidator.validate(measureRepIn);
        try {
            Measurement measurementIn = measureRepIn.createMeasurement();
            measurementIn.setMeasurement_id(id);
            Optional<Measurement> measurementOut;
            Optional<Measurement> oMeasurement = measurementRepository.findById(id);
            setExisting(oMeasurement.isPresent());
            if (isExisting()) {
                LOGGER.finer("Update measurement.");
                measurementOut = measurementRepository.update(measurementIn);
                if (!measurementOut.isPresent()) {
                    LOGGER.finer("Measurement does not exist.");
                    throw new NotFoundException(
                            "Measurement with the following id does not exist: "
                                    + id);
                }
            } else {
                LOGGER.finer("Resource does not exist.");
                throw new NotFoundException(
                        "Measurement with the following id does not exist: " + id);
            }

            LOGGER.finer("Measurement successfully updated.");
            return new MeasurementRepresentation(measurementOut.get());
        } catch (Exception ex) {
            throw new ResourceException(ex);
        }
    }

    /**
     * removes a measurement object
     * @throws NotFoundException
     */

    @Override
    public void remove() throws NotFoundException {
        LOGGER.finer("Removal of measurement");
//        ResourceUtils.checkRole(this, Shield.ROLE_DOCTOR);
//        LOGGER.finer("User allowed to remove a product.");
        try {
            Boolean isDeleted = measurementRepository.remove(id);

            if (!isDeleted) {
                LOGGER.config("Measurement id does not exist");
                throw new NotFoundException(
                        "Measurement with the following identifier does not exist:"
                                + id);
            }
            LOGGER.finer("Measurement successfully removed.");
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error when removing a measurement", ex);
            throw new ResourceException(ex);
        }
    }
}
