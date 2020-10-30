package gr.pfizer.restapi.resource.interfaces;

import gr.pfizer.restapi.exception.BadEntityException;
import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.representation.ConsultsRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

/**
 * This interface is about Consults object CRUD functionality. Consists of the Consults API functionality.
 * That is necessary for a user to interact with Consult object.
 *
 * @version 1.0
 * @since 1.0
 */
public interface ConsultResource {

    /**
     * Retrieves a user's consult and return it to the API. Given the id of the consult as a URL parameter retrieves all
     * the data that contains and return it as json object.
     * Needs basic authentication to get access to this method.
     *
     * http://localhost:9000/v1/team6/sacchon/consult/{consult_id}
     *
     * @return Consult object with all its data.
     * @throws NotFoundException
     */
    @Get("json")
    public ConsultsRepresentation getConsult() throws NotFoundException;

    /**
     * This method implements the functionality of Update for a Consult object. Given the id of the consult as a URL
     * parameter updates the values of that object and return it to the user as json object as well as saves it in the DB.
     *
     * http://localhost:9000/v1/team6/sacchon/consult/{consult_id}
     *
     * @param consultsRepresentation Json object that contains the new values
     * @return The object that contains the new values as Json
     * @throws NotFoundException
     * @throws BadEntityException
     */
    @Put("json")
    public ConsultsRepresentation store(ConsultsRepresentation consultsRepresentation)
            throws NotFoundException, BadEntityException;

    /**
     * This method removes a Consults object from the DB. Given the id of the consult as a URL parameter deletes
     * the object
     *
     * http://localhost:9000/v1/team6/sacchon/consult/{consult_id}
     *
     * @throws NotFoundException
     */
    @Delete
    public void remove() throws NotFoundException;

}
