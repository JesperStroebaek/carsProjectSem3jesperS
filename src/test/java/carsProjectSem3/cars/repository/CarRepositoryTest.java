package carsProjectSem3.cars.repository;

import carsProjectSem3.cars.entity.Car;
import carsProjectSem3.cars.entity.Member;
import carsProjectSem3.cars.entity.Reservation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class CarRepositoryTest {
  @Autowired
  CarRepository carRepository;

  //The two repositories below are necessary to create a reservation
  @Autowired
  ReservationRepository reservationRepository;
  @Autowired
  MemberRepository memberRepository;

  int carId1, carId2;
  @BeforeEach
  void setupBeforeEach(){
    reservationRepository.deleteAll();
    carRepository.deleteAll();
    memberRepository.deleteAll();

    Car volvo = Car.builder().brand("Volvo").model("V70").pricePrDay(700).bestDiscount(30).build();
    Car ww = Car.builder().brand("VW").model("Up").pricePrDay(250).bestDiscount(10).build();
    carRepository.save(volvo);
    carRepository.save(ww);
    carId1 = volvo.getId();
    carId2 = ww.getId();

    //Reserve the Volvo

    Member m1 = memberRepository.save(new Member("m1", "pw", "m1@a.dk", "fm", "ln", "vej 2", "Lynby", "2800"));
    reservationRepository.save(new Reservation(m1,volvo,LocalDate.of(2022,5,5)));
  }

  @Test
  void findCarByBrand() {
    List<Car> cars = carRepository.findCarByBrand("Volvo");
    assertEquals(1,cars.size());
  }

  @Test
  void findCarByPricePrDayBetween() {
    List<Car> cars = carRepository.findCarByPricePrDayBetween(0,300);
    assertEquals(1,cars.size());

    cars = carRepository.findCarByPricePrDayBetween(251,800);
    assertEquals(1,cars.size());

    cars = carRepository.findCarByPricePrDayBetween(0,701);
    assertEquals(2,cars.size());
  }

  @Test
  void testFindCarWhenNotReserved(){
    Car car = carRepository.findCarByIdIfNotAlreadyReserved(carId1, LocalDate.of(2022, 10,10)).get();
    assertNotNull(car);
  }
  @Test
  void testThrowWhenCarReserved(){
    Assertions.assertThrows(Exception.class,
            ()-> carRepository.findCarByIdIfNotAlreadyReserved(carId1, LocalDate.of(2022,5,5)).orElseThrow());
  }
}