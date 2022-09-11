package carsProjectSem3.cars.service;

import carsProjectSem3.cars.entity.Car;
import carsProjectSem3.cars.entity.Member;
import carsProjectSem3.cars.entity.Reservation;
import carsProjectSem3.cars.repository.CarRepository;
import carsProjectSem3.cars.repository.MemberRepository;
import carsProjectSem3.cars.repository.ReservationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
class ReservationServiceTest {

  ReservationService reservationService;

  @Autowired
  ReservationRepository reservationRepository;
  @Autowired
  CarRepository carRepository;

  @Autowired
  MemberRepository memberRepository;

  static int carId;

  @BeforeAll
  static void setupData(@Autowired ReservationRepository reservationRepository, @Autowired CarRepository carRepository, @Autowired MemberRepository memberRepository){
    reservationRepository.deleteAll();
    memberRepository.deleteAll();
    carRepository.deleteAll();
    Car volvo = Car.builder().brand("Volvo").model("V70").pricePrDay(700).bestDiscount(30).build();
    volvo = carRepository.save(volvo);
    carId = volvo.getId();
    Member m1 = memberRepository.save(new Member("m1", "pw", "m1@a.dk", "fm", "ln", "vej 2", "Lynby", "2800"));
    //Reserve the Car
    reservationRepository.save(new Reservation(m1,volvo, LocalDate.of(2022,5,5)));
  }

  @BeforeEach
  void setupReservationService(){
    reservationService = new ReservationService(memberRepository,carRepository, reservationRepository);
  }

  @Test
  void testReserveCar_Available() {
    reservationService.reserveCar("m1",carId,LocalDate.of(2022,11,11));
    Member member = memberRepository.findById("m1").get();
    assertEquals(2,member.getReservations().size());
  }
  @Test
  void testReserveCar_UnAvailableCarThrows() {
    Assertions.assertThrows(ResponseStatusException.class,()->
     reservationService.reserveCar("m1",carId,LocalDate.of(2022,5,5)));
  }
  @Test
  void testReserveCarV2_Available() {
    reservationService.reserveCarV2("m1",carId,LocalDate.of(2022,11,11));
    Member member = memberRepository.findById("m1").get();
    assertEquals(2,member.getReservations().size());
  }
  @Test
  void testReserveCarV2_UnAvailableCarThrows() {
    Assertions.assertThrows(ResponseStatusException.class,()->
     reservationService.reserveCarV2("m1",carId,LocalDate.of(2022,5,5)));
  }

}