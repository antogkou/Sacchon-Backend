package gr.pfizer.restapi.repository;

import gr.pfizer.restapi.model.Consults;
import gr.pfizer.restapi.security.Role;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * ConsultsRepository is a class which is used to connect the application with the DB specifically the consults table.
 * Implements all necessary queries for Consults object. Uses at most the Consults object as well as others that are in DB
 * to get more complex results.
 *
 * @author Anastasios Louvoulinas
 * @author Eleni Belogianni
 * @version 1.0
 * @since 1.0
 */
public class ConsultsRepository extends Repository{

    /**
     * Constructor that initializes the inherited variable from Repository.
     *
     * @param entityManager
     */
    public ConsultsRepository(EntityManager entityManager){
        super(entityManager);
    }

    /**
     * Finds all consults of a doctor by his email.
     *
     * @param email Doctor's email to retrieve
     * @return A list of doctor's consults
     */
    public List<Consults> findAllDoctorConsultsByEmail(String email){
        return entityManager.createQuery("select b " +
                "from Consults b " +
                "where b.myUser.email = :email " +
                "AND b.myUser.userRole = :role")
                .setParameter("email", email)
                .setParameter("role", Role.ROLE_DOCTOR.getRoleName())
                .getResultList();
    }

    /**
     * Finds all consults that added by all doctors of a patient by his email.
     *
     * @param email Patient's email to retrieve his consults
     * @return A list of consults
     */
    public List<Consults> findAllPatientsConsultsByEmail(String email){
        return entityManager.createQuery("select b " +
                "from Consults b " +
                "where b.patient_email = :email ")
                .setParameter("email", email)
                .getResultList();
    }

    /**
     * Removes a consult from the DB by its id.
     *
     * @param id Consult's id to be deleted
     * @return True if succeeds
     */
    public boolean remove(Long id){
        Optional<Consults> optionalConsults = findById(id);
        if(optionalConsults.isPresent()){
            Consults consults = optionalConsults.get();

            try{
                entityManager.getTransaction().begin();
                entityManager.remove(consults);
                entityManager.getTransaction().commit();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return true;
    }

    /**
     * Update a Consults object record. All the values of the object renew in the objects even if there is no change.
     *
     * @param consults A Consults object with values to update.
     * @return Optional<Consults> MyUser object if succeed, Optional.empty() otherwise
     */
    public Optional<Consults> update(Consults consults) {

        Consults in = entityManager.find(Consults.class, consults.getConsult_id());
        in.setConsultText(consults.getConsultText());
        in.setMedication(consults.getMedication());
        in.setDosage(consults.getDosage());
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

    @Override
    public Class<Consults> getEntityClass() {
        return Consults.class;
    }

    @Override
    public String getEntityClassName() {
        return Consults.class.getName();
    }

    /**
     * Finds consults written by a doctor to one of his patients.
     *
     * @param doctorEmail Doctor's email
     * @param patientEmail Patient's email
     * @return List of consults or empty if there aren't any submitted.
     */
    public Optional<List<Consults>> findAllDoctorConsultsByEmailForSpecifiedPatient(String doctorEmail, String patientEmail) {
        List<Consults> consults = entityManager.createQuery("select c " +
                "from Consults c " +
                "where c.myUser.email = :doctorEmail " +
                "AND c.patient_email = :patientEmail")
                .setParameter("doctorEmail", doctorEmail)
                .setParameter("patientEmail", patientEmail)
                .getResultList();
        return consults != null ? Optional.of(consults) : Optional.empty();
    }

    /**
     * Finds all consults that are submitted by a doctor by his email.
     *
     * @param email Doctor's email to retrieve his consults
     * @return List of consults, empty if there are any submitted.
     */
    public Optional<List<Consults>> findDoctorConsults(String email){
        List<Consults> consults = entityManager.createQuery("SELECT DISTINCT c "+
                "FROM MyUser d, Consults c " +
                "WHERE d.email = :email " +
                "AND d.userRole = :doctorRole " +
                "AND c.myUser.user_id = d.user_id")
                .setParameter("email" , email)
                .setParameter("doctorRole", Role.ROLE_DOCTOR.getRoleName()).
                getResultList();
        return consults != null ? Optional.of(consults) : Optional.empty();
    }

}
