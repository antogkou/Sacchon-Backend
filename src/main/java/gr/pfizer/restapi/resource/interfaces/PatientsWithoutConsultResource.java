package gr.pfizer.restapi.resource.interfaces;

import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.representation.MyUserRepresentation;
import org.restlet.resource.Get;

import java.util.List;

public interface PatientsWithoutConsultResource {

    @Get("json")
    List<MyUserRepresentation> getPatientsWithoutConsult() throws NotFoundException;
}
