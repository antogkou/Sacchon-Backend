package gr.pfizer.restapi.repository;


import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.model.Consults;
import gr.pfizer.restapi.model.MyUser;
import gr.pfizer.restapi.security.Role;
import org.hibernate.type.TimestampType;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static gr.pfizer.restapi.Configuration.MINIMUM_MEASUREMENTS_PER_MONTH;

/**
 * UserRepository is a class which is used to connect the application with the DB.
 * Implements all necessary queries for MyUser object. Uses at most the MyUser object as well as others that are in DB
 * to get more complex results.
 * Some of the basic functionality of CRUD inherited from Repository object and implements the methods from IUserRepository
 * interface.
 * Some important points to consider are the return object is an Optional<T> which means that the users has to check for
 * the results by his self.
 *
 * @author Anastasios Louvoulinas
 * @author Eleni Belogianni
 * @version 1.0
 * @since 1.0
 */
public class UserRepository extends Repository implements IUserRepository{

    /**
     * Constructor that initializes the inherited variable from Repository.
     *
     * @param entityManager
     */
    public UserRepository(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * Update a MyUser object record. All the values of the object renew in the objects even if there is no change. Uses
     * the id of the object that consists in the DB and an object with the updated values.
     *
     * @param id The ID of the DB record
     * @param myUser A MyUser object with values to updated.
     * @return Optional<MyUser> MyUser object if succeed, Optional.empty() otherwise
     */
    @Override
    public Optional<MyUser> update(long id, MyUser myUser) {

        MyUser in = entityManager.find(MyUser.class, id);
        in.setFirstName(myUser.getFirstName());
        in.setLastName(myUser.getLastName());
        in.setAddress(myUser.getAddress());
        in.setCity(myUser.getCity());
        in.setZipCode(myUser.getZipCode());
        in.setPhoneNumber(myUser.getPhoneNumber());
        in.setEmail(myUser.getEmail());
        in.setPassword(myUser.getPassword());

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(in);
            entityManager.getTransaction().commit();
            return Optional.of(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     *
     *
     * @param id
     * @param myUser
     * @return
     */
    @Override
    public Optional<MyUser> disableUser(long id, MyUser myUser) {
        MyUser in = entityManager.find(MyUser.class, id);
        in.setIsActive(0);
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(in);
            entityManager.getTransaction().commit();
            return Optional.of(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<MyUser> disableUserDoctor(long id, MyUser myUser) {
        MyUser in = entityManager.find(MyUser.class, id);
        in.setHasActiveDoctor(0);
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(in);
            entityManager.getTransaction().commit();
            return Optional.of(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Searches a MyUser object in DB by his email value. Email value is unique for each user.
     *
     * @param email The email of a user in String format.
     * @return Optional<MyUser> if the object exists, Optional.empty() if doesn't exist.
     */
    @Override
    public Optional<MyUser> findByEmail(String email) {
        try {
            MyUser user  = entityManager.createQuery("SELECT b " +
                            "FROM MyUser b " +
                            "WHERE b.email = :email", MyUser.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(user);
        }catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Find all patients who don't have a doctor to get a consult. Also checks if the user is not deleted.
     *
     * @return Optional<list<MyUser>> of the patients who don't have doctor
     */
    @Override
    public Optional<List<MyUser>> findPatientsWithoutDoctor(){
        List<MyUser> users = entityManager.createQuery("SELECT b FROM MyUser b WHERE b.hasActiveDoctor = 0 AND b.isActive = 1 AND b.userRole= :patientrole", MyUser.class)
                .setParameter("patientrole", Role.ROLE_PATIENT.getRoleName())
                .getResultList();
        return users != null ? Optional.of(users) : Optional.empty();
    }

    /**
     * Finds all patients that have no consult and need it.
     *
     * At first detects the users that have more than the minimum number of measurements last month. If those exceeds the
     * minimum number then retrieve the last consult and checks if that was more than a month. If there aren't a consult
     * in the last month then added on the list that will be returned.
     *
     * @return Optional<List<MyUser>> of patients that need consults, Optional.empty() if there aren't any of them.
     */
    @Override
    public Optional<List<MyUser>> findAllPatientsWithoutConsult(){
        List<MyUser> users = entityManager.createQuery(
                "SELECT a " +
                        "FROM MyUser a " +
                        "WHERE a.id not in " +
                        " (SELECT a.id "+
                        "FROM MyUser a inner join Consults c "+
                        "ON c.patient_email = a.email "+
                        "AND c.consult_created_date BETWEEN :from AND :to ) "+
                        "AND a.userRole = :patient_role "+
                        "AND a.isActive = 1 ", MyUser.class)


                .setParameter("from", java.sql.Date.valueOf(LocalDate.now().minusMonths(1)))
                .setParameter("to", java.sql.Date.valueOf(LocalDate.now()))
                .setParameter("patient_role",Role.ROLE_PATIENT.getRoleName())
                .getResultList();
        return users != null ? Optional.of(users) : Optional.empty();
    }

    /**
     * Find all users(doctors, patients) that have no activity between the given dates. Patients checked in their
     * measurements records and doctors in their consults. Each of them checked in the created date if there are any on
     * the time slot of the given dates then the user is inactive.
     *
     * @param from Date object that is the starting date.
     * @param to Date object that is the ending date.
     * @return Optional<List<MyUser>> of patients that are inactive, Optional.empty() if there aren't any of them.
     */
    @Override
    public Optional<List<MyUser>> findInactiveUsers(Date from, Date to){
        List<MyUser> users = entityManager.createQuery("SELECT distinct b FROM MyUser b, Measurement m " +
                "WHERE b.isActive = 1 " +
                "AND b.userRole = :patient_role " +
                "AND m.patient_email = b.email " +
                "AND NOT(m.measurement_created_date BETWEEN :from AND :to) ", MyUser.class)
                .setParameter("patient_role", Role.ROLE_PATIENT.getRoleName())
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
        users.addAll(
                entityManager.createQuery("SELECT distinct b FROM MyUser b, Consults c " +
                "WHERE b.isActive = 1 " +
                "AND b.userRole = :doctor_role " +
                "AND c.myUser.user_id = b.user_id " +
                "AND NOT(c.consult_created_date BETWEEN :from AND :to) ", MyUser.class)
                .setParameter("doctor_role", Role.ROLE_DOCTOR.getRoleName())
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList());
        return users != null ? Optional.of(users) : Optional.empty();
    }

    /**
     * Finds the patients of a doctor that need consult.
     *
     * In this methods all the patients of a doctor are retrieved, given the email of the doctor. If there is
     * no consult in the last month from the doctor then the user has to be advised by the doctor.
     *
     * @param email The doctors email to retrieve all his patients.
     * @return List of the patients that are no consulted by the doctor in the more than a month.
     */
    @Override
    public Optional<List<MyUser>> findPatientsWithoutConsultByDoctorsEmail(String email){
        List<MyUser> users = entityManager.createQuery("SELECT distinct b " +
                "FROM MyUser a, MyUser b, Consults c " +
                "WHERE b.isActive = 1 AND a.isActive = 1 " +
                "AND a.email = :email AND a.userRole = :doctor_role " +
                "AND b.userRole = :patient_role " +
                "AND c.myUser.email = :email " +
                "AND NOT(c.consult_created_date BETWEEN :from AND :to) ", MyUser.class)
                .setParameter("patient_role", Role.ROLE_PATIENT.getRoleName())
                .setParameter("doctor_role", Role.ROLE_DOCTOR.getRoleName())
                .setParameter("from", LocalDate.now().minusMonths(1))
                .setParameter("to", LocalDate.now())
                .setParameter("email", email)
                .getResultList();
        return users != null ? Optional.of(users) : Optional.empty();
    }


    /**
     * Finds the doctor's patients.
     *
     * In this methods all the patients of a doctor are retrieved, given the email of the doctor.
     *
     * @param doctorEmail The doctors email to retrieve all his patients.
     * @return List of the doctor's patients.
     */
    @Override
    public Optional<List<MyUser>> findDoctorPatients(String doctorEmail) {
        List<MyUser> users = entityManager.createQuery("SELECT DISTINCT p " +
                "FROM MyUser d, MyUser p,Consults c " +
                "WHERE d.email = :email " +
                "AND p.isActive = 1 " +
                "AND p.userRole = :patientrole " +
                "AND p.hasActiveDoctor = 1 " +
                "AND c.patient_email = p.email " +
                "AND c.myUser.user_id = d.user_id ",
                MyUser.class)
                .setParameter("email", doctorEmail)
                .setParameter("patientrole", Role.ROLE_PATIENT.getRoleName())
                .getResultList();
        return users != null ? Optional.of(users) : Optional.empty();
    }

    /**
     * Method to set that a patient got a consult by a doctor which means that this doctor attends to the patient. The
     * field setHasActiveDoctor sets automatically the value '1' which means that the patient has a doctor.
     *
     * @param patient_email Patients email to change the value of setHasActiveDoctor to 1
     * @return True if succeeds, False otherwise
     * @throws NotFoundException
     */
    public boolean setHasActiveDoctor(String patient_email) throws NotFoundException {

        Optional<MyUser> patient = this.findByEmail(patient_email);
        if (!patient.isPresent())
            throw new NotFoundException("Patient Not found !");
        MyUser in = patient.get();
        in.setHasActiveDoctor(1);
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(in);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Optional<MyUser> findByFirstName(String name) {
        MyUser user = entityManager.createQuery("SELECT b FROM MyUser b WHERE b.firstName = :firstName", MyUser.class)
                .setParameter("firstName", name)
                .getSingleResult();
        return user != null ? Optional.of(user) : Optional.empty();
    }

    public Optional<MyUser> findByLastName(String name) {
        MyUser user = entityManager.createQuery("SELECT b FROM MyUser b WHERE b.lastName = :lastName", MyUser.class)
                .setParameter("lastName", name)
                .getSingleResult();
        return user != null ? Optional.of(user) : Optional.empty();
    }


    public Optional<MyUser> findByCreatedDate(Date date) {
        MyUser user = entityManager.createQuery("SELECT b FROM MyUser b WHERE b.createdDate = :createdDate", MyUser.class)
                .setParameter("createdDate", date)
                .getSingleResult();
        return user != null ? Optional.of(user) : Optional.empty();
    }

    @Override
    public Class<MyUser> getEntityClass() {
        return MyUser.class;
    }

    @Override
    public String getEntityClassName() {
        return MyUser.class.getName();
    }

}
