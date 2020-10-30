package gr.pfizer.restapi.resource.interfaces;

import gr.pfizer.restapi.representation.MyUserRepresentation;
import org.restlet.resource.Get;

import java.util.List;

public interface MyUserResourceAllGet {

    @Get("json")
    public List<MyUserRepresentation> getAllUsers();

}
