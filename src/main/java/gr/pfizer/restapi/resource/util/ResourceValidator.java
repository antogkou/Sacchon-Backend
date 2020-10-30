package gr.pfizer.restapi.resource.util;
import gr.pfizer.restapi.exception.BadEntityException;
import gr.pfizer.restapi.representation.ConsultsRepresentation;
import gr.pfizer.restapi.representation.MeasurementRepresentation;
import gr.pfizer.restapi.representation.MyUserRepresentation;

public class ResourceValidator {
    /**
     * Checks that the given entity is not null.
     *
     * @param entity
     *            The entity to check.
     * @throws BadEntityException
     *             In case the entity is null.
     */
    public static void notNull(Object entity) throws BadEntityException {
        if (entity == null) {
            throw new BadEntityException("No input entity");
        }
    }

    /**
     * Checks that the given company is valid.
     *
     * @param myUserRepresentation
     * @throws BadEntityException
     */
    public static void validate(MyUserRepresentation myUserRepresentation)
            throws BadEntityException {
        if (myUserRepresentation.getEmail() == null) {
            throw new BadEntityException("User email cannot be null");
        }
    }


    /**
     * Checks that the given company is valid.
     *
     * @param consultRepresentation
     * @throws BadEntityException
     */
    public static void validate(ConsultsRepresentation consultRepresentation)
            throws BadEntityException {
        if ( consultRepresentation.getConsultText()==null) {
            throw new BadEntityException(
                    "Consult name cannot be null");

    /**
     * Checks that the given measurement is valid.
     *
     * @param measurementRepresentation
     * @throws BadEntityException
     */
//     public static void validate(MeasurementRepresentation measurementRepresentation)
//             throws BadEntityException {
//         if ( measurementRepresentation.getCreated_date()==null) {
//             throw new BadEntityException(
//                     "measurement name cannot be null");

        }
    }
}
