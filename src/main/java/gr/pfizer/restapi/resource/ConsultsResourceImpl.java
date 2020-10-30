package gr.pfizer.restapi.resource;

import gr.pfizer.restapi.exception.BadEntityException;
import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.model.Consults;
import gr.pfizer.restapi.repository.ConsultsRepository;
import gr.pfizer.restapi.repository.util.JpaUtil;
import gr.pfizer.restapi.representation.ConsultsRepresentation;
import gr.pfizer.restapi.resource.interfaces.ConsultResource;
import gr.pfizer.restapi.security.ResourceUtils;
import gr.pfizer.restapi.security.Shield;
import org.restlet.engine.Engine;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsultsResourceImpl
        extends ServerResource implements ConsultResource {

    private long consult_id;
    private ConsultsRepository consultsRepository;

    public static final Logger LOGGER = Engine.getLogger(ConsultsResourceImpl.class);


    /**
     * initializes consult resource
     */
    @Override
    protected void doInit(){
        LOGGER.info("Initialising consult resource starts");
        try{
            consultsRepository =
                    new ConsultsRepository(JpaUtil.getEntityManager());
            consult_id = Long.parseLong(getAttribute("consult_id"));
        }catch (Exception e){
            consult_id =-1;
        }

        LOGGER.info("Initialising consult resource ends");
    }

    /**
     *
     * @return a consult represantation
     * @throws NotFoundException
     */

    @Override
    public ConsultsRepresentation getConsult() throws NotFoundException{
        LOGGER.info("Retrieve a product");

        // Check authorization
        //ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);

        LOGGER.info("Retrieve a consult");
        ConsultsRepository consultsRepository = new ConsultsRepository(JpaUtil.getEntityManager());
        try{
            LOGGER.config("consult id does not exist:" + consult_id);

            Optional<Consults> optionalConsults = consultsRepository.findById(consult_id);
            setExisting(optionalConsults.isPresent());
            if(!isExisting()){
                throw new Exception();
            }else{
                return new ConsultsRepresentation(optionalConsults.get());
            }
        }catch (Exception e){
            throw new ResourceException(e);
        }
    }


    /**
     *
     * @param consultsRepresentation
     * @return a representation of a consult object
     * @throws NotFoundException
     * @throws BadEntityException
     */
    @Override
    public ConsultsRepresentation store(ConsultsRepresentation consultsRepresentation)
            throws NotFoundException, BadEntityException {

        LOGGER.finer("Update a consult");
        try{
            Consults consultsIn = consultsRepresentation.createConsult();
            consultsIn.setConsult_id(consult_id);

            Optional<Consults> consultsOutput;
            Optional<Consults> optionalConsults = consultsRepository.findById(consult_id);

            setExisting(optionalConsults.isPresent());
            if(isExisting()){
                LOGGER.finer("Update consult");

                consultsOutput = consultsRepository.update(consultsIn);

                if(!consultsOutput.isPresent()){
                    LOGGER.finer("The consult you want to update does not exist");
                    throw new NotFoundException(
                            "Consult with the following id does not exist" + consult_id);
                }
            }else {
                LOGGER.finer("Consult does not exist.");
                throw new NotFoundException("Consult not found " + consult_id);
            }

            LOGGER.finer("Consult successfully updated");
            return new ConsultsRepresentation(consultsOutput.get());
        }catch (Exception e){
            throw new ResourceException(e);
        }
    }

    /**
     * removes a consult object
     * @throws NotFoundException
     */
    @Override
    public void remove() throws NotFoundException{
        LOGGER.finer("Removal of consult");
        try{
            Boolean isDeleted = consultsRepository.remove(consult_id);

            if(!isDeleted){
                LOGGER.config("Consult id does not exist");
                throw new Exception(
                        "Consult not found"
                                + consult_id);
            }
            LOGGER.finer("Consult successfully removed.");
        }catch (Exception e){
            LOGGER.log(Level.WARNING, "Error when removing a consult", e);
            throw new ResourceException(e);
        }
    }
}
