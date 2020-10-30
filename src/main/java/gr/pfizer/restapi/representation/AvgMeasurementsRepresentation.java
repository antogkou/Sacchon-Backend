package gr.pfizer.restapi.representation;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AvgMeasurementsRepresentation {
    List<MeasurementRepresentation> measurements;
    private Double avgCarb;
    private Double avgGlucose;
}
