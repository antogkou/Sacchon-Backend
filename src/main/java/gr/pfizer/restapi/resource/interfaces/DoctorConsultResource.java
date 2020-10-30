package gr.pfizer.restapi.resource.interfaces;

import gr.pfizer.restapi.representation.ConsultsRepresentation;
import javassist.NotFoundException;
import org.restlet.resource.Get;

import java.util.List;

/**
 * This module provides facilities for retrieving Consults objects.
 *
 * @version 1.0
 * @since 1.0
 */
public interface DoctorConsultResource {

    /**
     * Retrieve all Consults object that a user has already submitted in the system.
     * Need basic authentication in order to get user's email and retrieve his consults.
     *
     * E.g http://localhost:9000/v1/team6/sacchon/doctor/my-consults
     *
     * @return A list of the doctor's consults
     * @throws NotFoundException
     */
    @Get
    public List<ConsultsRepresentation> getDoctorConsults() throws NotFoundException;
}
