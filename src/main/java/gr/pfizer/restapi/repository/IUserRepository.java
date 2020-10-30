package gr.pfizer.restapi.repository;

import gr.pfizer.restapi.model.MyUser;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IUserRepository {

    Optional<MyUser> update(long id, MyUser myUser);

    Optional<MyUser> disableUser(long id, MyUser myUser);

    Optional<MyUser> findByEmail(String email);

    Optional<List<MyUser>> findPatientsWithoutDoctor();

    Optional<List<MyUser>> findAllPatientsWithoutConsult();

    Optional<List<MyUser>> findInactiveUsers(Date from, Date to);

    Optional<List<MyUser>> findPatientsWithoutConsultByDoctorsEmail(String email);

    Optional<List<MyUser>> findDoctorPatients(String doctorEmail);
}
