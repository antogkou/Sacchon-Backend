package gr.pfizer.restapi.resource.interfaces;

import gr.pfizer.restapi.exception.BadEntityException;
import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.model.Consults;
import gr.pfizer.restapi.representation.ConsultsRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.util.List;

/**
 * Interface that is about Consults object separating the endpoints which have the same endpoints.
 *
 * @version 1.0
 * @since 1.0
 */
public interface ConsultsListResource {

    /**
     * Returns a list of all consults existing in the system.
     *
     * @return A list of Consults objects
     * @throws NotFoundException
     */
    @Get("json")
    public List<ConsultsRepresentation> getConsults() throws NotFoundException;

    /**
     * Adds a new consult to the system.
     *
     * @param consultsRepresentation The new Consult in a json format.
     * @return The new consult if succeeds or throws the exception.
     * @throws BadEntityException Unable to connect with the DB.
     */
    @Post("json")
    public ConsultsRepresentation add(ConsultsRepresentation consultsRepresentation)
        throws BadEntityException;
}
