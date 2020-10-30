package gr.pfizer.restapi.resource;

import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.model.Measurement;
import gr.pfizer.restapi.model.MyUser;
import gr.pfizer.restapi.repository.ConsultsRepository;
import gr.pfizer.restapi.repository.MeasurementRepository;
import gr.pfizer.restapi.repository.UserRepository;
import gr.pfizer.restapi.repository.util.JpaUtil;
import gr.pfizer.restapi.representation.AvgMeasurementsRepresentation;
import gr.pfizer.restapi.representation.MeasurementRepresentation;
import gr.pfizer.restapi.representation.MyUserRepresentation;
import gr.pfizer.restapi.resource.interfaces.AnalyticsResource;
import org.restlet.Request;
import org.restlet.engine.Engine;
import org.restlet.resource.ServerResource;
import org.restlet.security.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class AnalyticsResourceImpl extends ServerResource implements AnalyticsResource {
    public static final Logger LOGGER = Engine.getLogger(AdminResourceImpl.class);
    private MeasurementRepository measurementRepository;
    private Date startDate;
    private Date endDate;

    @Override
    protected void doInit() {
        LOGGER.info("+ ------ Initialising admin resource starts");
        try {
            measurementRepository = new MeasurementRepository(JpaUtil.getEntityManager());
            try {
                String startDateString = getQueryValue("from");
                String endDateString = getQueryValue("to");
                String[] words = startDateString.split("-");

                startDate = new Date(Integer.parseInt(words[0])-1900,
                        Integer.parseInt(words[1]) - 1, Integer.parseInt(words[2])  );

                words = endDateString.split("-");
                endDate = new Date(Integer.parseInt(words[0])-1900,
                        Integer.parseInt(words[1]) - 1, Integer.parseInt(words[2])  );
                System.out.println(startDate);
                System.out.println(endDate);
            }
            catch(Exception e) {
                startDate =null; endDate =null;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("+ ------ Initialising admin resource ends");
    }

    @Override
    public AvgMeasurementsRepresentation getAverageMeasurements() throws NotFoundException {
        LOGGER.info("+ ------ Retrieve account details");
        String email = Request.getCurrent().getClientInfo().getUser().toString();
        try{
            List<Measurement> measurements = measurementRepository.findAllByEmail(email, startDate, endDate);

            Optional<String> avgCarb = measurementRepository.getCarbLevelAvg(email, startDate, endDate);
            Optional<String> avgGlucose = measurementRepository.getGlucoseLevelAvg(email, startDate, endDate);
            if (!avgCarb.isPresent() || !avgGlucose.isPresent())
                throw new NotFoundException("There are no averages in these dates");

            List<MeasurementRepresentation> measurementRepresentation = new ArrayList<>();
            measurements.forEach(measurement -> measurementRepresentation.add(new MeasurementRepresentation(measurement)));

            return new AvgMeasurementsRepresentation(measurementRepresentation,
                            Double.valueOf(avgCarb.get()), Double.valueOf(avgGlucose.get()));
        }catch (Exception e){
            LOGGER.info("Couldn't retrieve average values");
            throw new NotFoundException("+ ------- Couldn't find the user");
        }
    }
}
