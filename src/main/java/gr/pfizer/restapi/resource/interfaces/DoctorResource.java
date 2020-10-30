package gr.pfizer.restapi.resource.interfaces;

import gr.pfizer.restapi.exception.BadEntityException;
import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.representation.ConsultsRepresentation;
import gr.pfizer.restapi.representation.MyUserRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;

import java.util.List;

/**
 * This module provides facilities for doctor retrieving lists of objects that are
 *
 * @version 1.0
 * @since 1.0
 */
public interface DoctorResource {

    /**
     * Retrieve the patients that are attended by a doctor.
     *
     * E.g http://localhost:9000/v1/team6/sacchon/my-patients
     *
     * @return A list of the doctor's patients
     * @throws BadEntityException
     */
    @Get("json")
    public List<MyUserRepresentation> getDoctorPatients() throws BadEntityException;

    /**
     * Retrieve a patient's consults.
     *
     * @return The consults that are submitted to a patient.
     */
    @Get("?consults-of-patient")
    List<ConsultsRepresentation> getMyPatientConsults();
}
