package repository;

import gr.pfizer.restapi.model.MyUser;
import gr.pfizer.restapi.repository.util.JpaUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import gr.pfizer.restapi.repository.UserRepository;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class UserRepositoryTest {

    private static UserRepository userRepository;
    private static MyUser testUser;
    private Integer id;

    @BeforeAll
    static void beforeAll(){
        System.out.println("|   Start Unit Testing -- User repository");
        userRepository = new UserRepository(JpaUtil.getEntityManager());
        MyUser myUser = new MyUser();
        List list = userRepository.findAll();
        Optional<MyUser> temp = userRepository.findByEmail("unit@test");
        if (!temp.isPresent()){
            myUser.setAddress("Delfwn");
            myUser.setCity("Thessaloniki");
            myUser.setEmail("unit@test");
            myUser.setPassword("Pasww0rd1994");
            myUser.setFirstName("Unit");
            myUser.setLastName("Test");
            myUser.setIsActive(1);
            myUser.setZipCode("54664");
            myUser.setUserRole("patient");
            myUser.setPhoneNumber("6955961055");
            myUser.setHasActiveDoctor(0);
            testUser = myUser;
            userRepository.save(myUser);
        }
         else {
             testUser = temp.get();
        }
        System.out.println("|   End Unit Testing -- User repository");
    }

    @AfterAll
    static void afterAll(){
        userRepository.getEntityManager().close();
    }

    @Test
    void update() {
        System.out.println("|       Start Unit Testing -- update User repository");
        MyUser myUser = userRepository.getEntityManager().find(MyUser.class, testUser.getUser_id());
        myUser.setPhoneNumber("1020304050");
        userRepository.update(myUser.getUser_id(), myUser);
        myUser = userRepository.getEntityManager().find(MyUser.class, testUser.getUser_id());
        assertEquals("1020304050", myUser.getPhoneNumber());
        System.out.println("|       Start Unit Testing -- End update User repository");
    }

    @Test
    void disableUser() {
    }

    @Test
    void disableUserDoctor() {
    }

    @Test
    void findByEmail() {

        Optional<MyUser> temp = userRepository.findByEmail("unit@test");
        if(temp.isPresent()){
            MyUser test = temp.get();
            assertEquals(testUser, test);
        }
        else {
            fail();
        }
    }

    @Test
    void findPatientsWithoutDoctor() {
        List<MyUser> test = userRepository.findPatientsWithoutDoctor().get();
        assertTrue(test.contains(testUser));
    }

    @Test
    void findAllPatientsWithoutConsult() {
    }

    @Test
    void findInactiveUsers() {
    }

    @Test
    void findPatientsWithoutConsultByDoctorsEmail() {
    }

    @Test
    void findDoctorPatients() {
    }

    @Test
    void setHasActiveDoctor() {
    }
}