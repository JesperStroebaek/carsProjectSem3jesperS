package carsProjectSem3.cars.service;

import carsProjectSem3.cars.dto.CarRequest;
import carsProjectSem3.cars.dto.CarResponse;
import carsProjectSem3.cars.entity.Car;
import carsProjectSem3.cars.repository.CarRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CarServiceMockWithH2Test {

  public CarService carService;
  public static CarRepository carRepository;

  static int car1Id,car2Id;  //Store ID's for subsequent tests

  @BeforeAll
  public static void initTestData(@Autowired CarRepository car_Repository){
    carRepository = car_Repository;
    carRepository.deleteAll();
    Car car1 = new Car("WW","Golf",500,30);
    Car car2 = new Car("Toyota","Aygo",500,25);
    car1 = carRepository.save(car1);
    car2 = carRepository.save(car2);
    //Now the objects are managed and have gotten their auto-generated IDs
    car1Id = car1.getId();
    car2Id = car1.getId();
    carRepository = car_Repository;
  }
  @BeforeEach
  public void initCarService(){
    carService = new CarService(carRepository);
  }

  @Test
  void addMember() {
    CarRequest request = new CarRequest("Volvo","V70",600,40);
    CarResponse response = carService.addCar(request,true);
    assertTrue(response.getId() > 0);
    assertTrue(response.getCreated().isBefore(LocalDateTime.now()));
  }

  @Test
  void editCar() {
    Car carToEditEntity = carRepository.findById(car1Id).get();
    LocalDateTime previousEdited = carToEditEntity.getEdited();
    carToEditEntity.setPricePrDay(1000);
    carToEditEntity.setBestDiscount(50);
    CarRequest request = new CarRequest(carToEditEntity);
    carService.editCar(request,car1Id);
    //Load car again and verify changes
    Car edited = carRepository.findById(car1Id).get();
    assertEquals(1000,edited.getPricePrDay());
    assertEquals(50,edited.getBestDiscount());
    assertTrue(edited.getEdited().isBefore(LocalDateTime.now()));
  }

  @Test
  void getCars() {
    List<CarResponse> cars = carService.getCars(false);
    assertEquals(2,cars.size());
    assertNull(cars.get(0).getBestDiscount());
  }

  @Test
  void findCarByIdSimple() throws Exception {
    CarResponse response = carService.findCarById(car1Id,false);
    assertEquals(car1Id,response.getId());
    assertNull(response.getBestDiscount());
    assertNull(response.getCreated());
  }
  @Test
  void findCarByIdAll() throws Exception {
    CarResponse response = carService.findCarById(car1Id,true);
    assertEquals(car1Id,response.getId());
    assertEquals(30,response.getBestDiscount());
    assertNotNull(response.getCreated());
  }
  @Test
  void findCarByIdThrows() throws Exception {
    ResponseStatusException ex = Assertions.assertThrows(
            ResponseStatusException.class,()-> carService.findCarById(12345,false));
    assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
  }

  @Test
  void setPricePrDay() throws Exception {
    carService.setPricePrDay(car1Id,111);
    //Load the car and verify change
    CarResponse response = carService.findCarById(car1Id,false);
    assertEquals(111,response.getPricePrDay());
  }
 }