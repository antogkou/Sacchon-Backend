package gr.pfizer.restapi.resource.util;

import gr.pfizer.restapi.exception.BadEntityException;
import gr.pfizer.restapi.resource.ConsultsListResourceImpl;
import org.restlet.engine.Engine;

import java.util.Date;
import java.util.logging.Logger;

public class InitParams {
    public static final Logger LOGGER = Engine.getLogger(InitParams.class);

    static public String initializeEmailParam(String email) throws BadEntityException {
        try {
            email = email;
        }
        catch(Exception e) {
            LOGGER.info("Couldn't initialize the email");
            throw new BadEntityException("Couldn't find the email");
        }
        return email;
    }

    static public Date initializeDatesParam(String startDateString) throws BadEntityException {
        Date date;
        try{
            String[] words = startDateString.split("-");

            date = new Date(Integer.parseInt(words[0])-1900,
                    Integer.parseInt(words[1]) - 1, Integer.parseInt(words[2]));
        }catch (Exception e){
            throw new BadEntityException("Couldn't find the dates");
        }
        return date;
    }
}
