package gr.pfizer.restapi.resource.interfaces;

import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.representation.AvgMeasurementsRepresentation;
import org.restlet.resource.Get;

/**
 * Interface of the average measurements of a user's account.
 *
 * @version 1.0
 * @since 1.0
 */
public interface AnalyticsResource {

    /**
     * Method that used to get the user's measurements and the average values of them. This retrieved by the two URL
     * params for the period. The period set by URL params "from" and "to".
     *
     * E.g http://localhost:9000/v1/team6/sacchon/myaccount/avg?from=2020-05-12&to=2020-07-12
     *
     * @return AvgMeasurementsRepresentation object which contain averages and measurements of the user.
     * @throws NotFoundException
     */
    @Get("json")
    AvgMeasurementsRepresentation getAverageMeasurements() throws NotFoundException;
}
