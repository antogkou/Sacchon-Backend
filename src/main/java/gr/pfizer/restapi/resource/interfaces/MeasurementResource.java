package gr.pfizer.restapi.resource.interfaces;

import gr.pfizer.restapi.exception.BadEntityException;
import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.representation.MeasurementRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

/**
 * This module provides the CRUD capabilities for the Measurement object.
 *
 * @version 1.0
 * @since 1.0
 */
public interface MeasurementResource {

    /**
     * Returns a measurement and its values.
     *
     * @return A measurement object else throws exception.
     * @throws NotFoundException Unable to locate the measurement.
     */
    @Get("json")
    public MeasurementRepresentation getMeasurement() throws NotFoundException;

    /**
     * Add a new measurement to the system. A user submits a new Measurement in a json format and submits. Needs
     * authentication to be able to use this capability.
     *
     * @param productReprIn The new measurement.
     * @return The new measurement if succeeds else throws exception
     * @throws NotFoundException
     * @throws BadEntityException Unable to connect to DB
     */
    @Put("json")
    public MeasurementRepresentation store(MeasurementRepresentation productReprIn)
            throws NotFoundException, BadEntityException;

    /**
     * Removes an existing Measurement from the system
     *
     * @throws NotFoundException
     */
    @Delete
    public void remove() throws NotFoundException;
}
