package gr.pfizer.restapi.resource.interfaces;

import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.representation.MeasurementRepresentation;
import org.restlet.resource.Get;

import java.util.List;

/**
 * This module provides facilities for retrieving Measurements objects.
 *
 * @version 1.0
 * @since 1.0
 */
public interface MeasurementByPatientResource {

    /**
     * Returns all the measurements of a patient.
     *
     * @return A list with the measurements of a patient.
     * @throws NotFoundException
     */
    @Get("json")
    public List<MeasurementRepresentation> getMeasurementByPatient() throws NotFoundException;
}
