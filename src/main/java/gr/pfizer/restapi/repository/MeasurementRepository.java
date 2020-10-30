package gr.pfizer.restapi.repository;

import gr.pfizer.restapi.model.Consults;
import gr.pfizer.restapi.model.Measurement;

import javax.persistence.EntityManager;
import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MeasurementRepository extends Repository{


    public MeasurementRepository(EntityManager entityManager) {
        super(entityManager);
    }

    public Optional<Measurement> findById(Long id) {
        Measurement measurement = entityManager.find(Measurement.class, id);
        return measurement != null ? Optional.of(measurement) : Optional.empty();
    }

    public List<Measurement> findAll() {
        return entityManager.createQuery("from Measurement").getResultList();
    }

    public Optional<String> getGlucoseLevelAvg(String email, Date from, Date to){
        Optional<String> avg = Optional.of(entityManager.createQuery("SELECT AVG(m.glucose_level) " +
                "FROM Measurement m, MyUser u " +
                "WHERE m.measurement_created_date BETWEEN :from AND :to " +
                "AND m.patient_email = :email")
                .setParameter("email", email)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult().toString());
        return avg != null ? avg : Optional.empty();
    }

    public Optional<String> getCarbLevelAvg(String email, Date from, Date to){
        Optional<String> avg = Optional.of(entityManager.createQuery("SELECT AVG(m.carb_intake) " +
                "FROM Measurement m, MyUser u " +
                "WHERE m.measurement_created_date BETWEEN :from AND :to " +
                "AND m.patient_email = :email")
                .setParameter("email", email)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult().toString());
        return avg != null ? avg : Optional.empty();
    }

    public List<Measurement> findAllByEmail(String email){
        return entityManager.createQuery("select b " +
                "from Measurement b " +
                "where b.patient_email = :email")
                .setParameter("email", email)
                .getResultList();
    }

    public List<Measurement> findAllByEmail(String email, Date from, Date to){
        return entityManager.createQuery("select b " +
                "from Measurement b " +
                "where b.patient_email = :email " +
                "AND b.measurement_created_date BETWEEN :from AND :to")
                .setParameter("email", email)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

    public List<Measurement> findAllPatientMeasurementsById(long id){
        return entityManager.createQuery("select b " +
                "from Measurement b " +
                "where b.myUser.user_id = :id")
                .setParameter("id", id)
                .getResultList();
    }

    public Optional<Measurement> findByName(String firstName) {
        Measurement measurement = entityManager.createQuery("SELECT b FROM Measurement b WHERE b.carb_intake = :carb_intake", Measurement.class)
                .setParameter("firstName", firstName)
                .getSingleResult();
        return measurement != null ? Optional.of(measurement) : Optional.empty();
    }

    @Override
    public Class<Measurement> getEntityClass() {
        return Measurement.class;
    }

    @Override
    public String getEntityClassName() {
        return Measurement.class.getName();
    }

    //find carb_intake
    public Optional<Measurement> findByNameNamedQuery(String carb_intake) {
        Measurement measurement = entityManager.createNamedQuery("Measurement.findByName", Measurement.class)
                .setParameter("carb_intake", carb_intake)
                .getSingleResult();
        return measurement != null ? Optional.of(measurement) : Optional.empty();
    }


    public Optional<Measurement> save(Measurement measurement){

        try {
            entityManager.getTransaction().begin();
            entityManager.persist (measurement);
            entityManager.getTransaction().commit();
            return Optional.of(measurement);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    public Optional<Measurement> update(Measurement measurement) {

        Measurement in = entityManager.find(Measurement.class, measurement.getMeasurement_id());
        in.setCarb_intake(measurement.getCarb_intake());
        in.setGlucose_level(measurement.getGlucose_level());
        try {
            entityManager.getTransaction().begin();
            entityManager.persist (in);
            entityManager.getTransaction().commit();
            return Optional.of(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean remove(Long id){
        Optional<Measurement> omeasurement = findById(id);
        if (omeasurement.isPresent()){
            Measurement p = omeasurement.get();

            try{
                entityManager.getTransaction().begin();
                entityManager.remove(p);
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }


}
