package carsProjectSem3.cars.configuration;

import carsProjectSem3.cars.entity.Car;
import carsProjectSem3.cars.entity.Member;
import carsProjectSem3.cars.repository.CarRepository;
import carsProjectSem3.cars.repository.MemberRepository;
import carsProjectSem3.cars.service.ReservationService;
import carsProjectSem3.security.entity.Role;
import carsProjectSem3.security.entity.UserWithRoles;
import carsProjectSem3.security.repository.UserWithRolesRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Controller
public class SetupDevUsers implements ApplicationRunner {

  UserWithRolesRepository userWithRolesRepository;
  MemberRepository memberRepository;
  CarRepository carRepository;
  ReservationService reservationService;

  String passwordUsedByAll;

  public SetupDevUsers(UserWithRolesRepository userWithRolesRepository,
                       MemberRepository memberRepository,
                       CarRepository carRepository,
                       ReservationService reservationService) {
    this.userWithRolesRepository = userWithRolesRepository;
    this.memberRepository = memberRepository;
    this.carRepository = carRepository;
    this.reservationService = reservationService;
    passwordUsedByAll = "test12";
  }

  @Override
  public void run(ApplicationArguments args) {
    Member m1 = new Member("member1", passwordUsedByAll, "memb1@a.dk", "Kurt", "Wonnegut", "Lyngbyvej 2", "Lynbby", "2800");
    memberRepository.save(m1);

    Car car1 = Car.builder()
            .brand("Volvo")
            .model("V70")
            .pricePrDay(700)
            .bestDiscount(30.0)
            .build();

    carRepository.save(car1);

    //Reserve the car
    reservationService.reserveCar(m1.getUsername(), car1.getId(), LocalDate.of(2022, 10,10));
    try {
      reservationService.reserveCar(m1.getUsername(), car1.getId(), LocalDate.of(2022, 10, 10));
    } catch (ResponseStatusException ex){
      System.out.println("Car was already reserved");
    }

    setupUserWithRoleUsers();
  }

  /*****************************************************************************************
   NEVER  COMMIT/PUSH CODE WITH DEFAULT CREDENTIALS FOR REAL
   iT'S ONE OF THE TOP SECURITY FLAWS YOU CAN DO
   *****************************************************************************************/
  private void setupUserWithRoleUsers() {
    System.out.println("******************************************************************************");
    System.out.println("******* NEVER  COMMIT/PUSH CODE WITH DEFAULT CREDENTIALS FOR REAL ************");
    System.out.println("******* REMOVE THIS BEFORE DEPLOYMENT, AND SETUP DEFAULT USERS DIRECTLY  *****");
    System.out.println("**** ** ON YOUR REMOTE DATABASE                 ******************************");
    System.out.println("******************************************************************************");
    UserWithRoles user1 = new UserWithRoles("user1", passwordUsedByAll, "user1@a.dk");
    UserWithRoles user2 = new UserWithRoles("user2", passwordUsedByAll, "user2@a.dk");
    UserWithRoles user3 = new UserWithRoles("user3", passwordUsedByAll, "user3@a.dk");
    user1.addRole(Role.USER);
    user1.addRole(Role.ADMIN);
    user2.addRole(Role.USER);
    user3.addRole(Role.ADMIN);
    userWithRolesRepository.save(user1);
    userWithRolesRepository.save(user2);
    userWithRolesRepository.save(user3);
  }
}
