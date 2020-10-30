package gr.pfizer.restapi.resource.interfaces;

import gr.pfizer.restapi.exception.BadEntityException;
import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.representation.MeasurementRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import java.util.List;

/**
 *
 *
 * @version 1.0
 * @since 1.0
 */
public interface MeasurementResourceList {

    /**
     *
     *
     * @return
     * @throws NotFoundException
     */
    @Get("json")
    public List<MeasurementRepresentation> getMeasurements() throws NotFoundException;

    /**
     *
     *
     * @param measureRepIn
     * @return
     * @throws BadEntityException
     */
    @Post("json")
    public MeasurementRepresentation add(MeasurementRepresentation measureRepIn)
            throws BadEntityException;
}
