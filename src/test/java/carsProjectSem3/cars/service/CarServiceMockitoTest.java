package carsProjectSem3.cars.service;

import carsProjectSem3.cars.dto.CarRequest;
import carsProjectSem3.cars.dto.CarResponse;
import carsProjectSem3.cars.entity.Car;
import carsProjectSem3.cars.repository.CarRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CarServiceMockitoTest {
  @Mock
  CarRepository carRepository;

  @Autowired
  CarService carService;

  @BeforeEach
  public void setup() {
    carService = new CarService(carRepository);
  }

  @Test
  void addCar() {
    Car carWithId = new Car("Volvo","V70",100,100);
    carWithId.setId(1000);
    Mockito.when(carRepository.save(any(Car.class))).thenReturn(carWithId);
    CarRequest request = new CarRequest(carWithId.getBrand(),carWithId.getModel(),carWithId.getPricePrDay(),10);
    CarResponse res = carService.addCar(request,false);
    assertEquals(1000,res.getId());
  }

  @Test
  void getCars() {
    Mockito.when(carRepository.findAll()).thenReturn(List.of(
            new Car("Volvo","V70",100,10),
            new Car("WW","Polo",100,10)
    ));
    List<CarResponse> cars = carService.getCars(false);
    assertEquals(2,cars.size());
  }

  @Test
  void findCarById() throws Exception {
    Car car = new Car("Volvo","V70",100,10);
    car.setId(1000);
    Mockito.when(carRepository.findById(1000)).thenReturn(Optional.of(car));
    CarResponse carRes = carService.findCarById(1000,false);
    assertEquals("Volvo",carRes.getBrand());
  }

  @Test
  void findCarByIdThrows() throws Exception {
    Mockito.when(carRepository.findById(12345)).thenReturn(Optional.empty());
    ResponseStatusException ex =
            Assertions.assertThrows(ResponseStatusException.class,()-> carService.findCarById(12345,false));
    assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
  }





}