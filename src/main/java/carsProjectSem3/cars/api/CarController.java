package carsProjectSem3.cars.api;

import carsProjectSem3.cars.dto.CarRequest;
import carsProjectSem3.cars.dto.CarResponse;
import carsProjectSem3.cars.service.CarService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/cars")
public class CarController {
  CarService carService;

  public CarController(CarService carService) {
    this.carService = carService;
  }

  //Security --> ADMIN ONLY
  @PostMapping
  public CarResponse addMember(@RequestBody CarRequest body) {
    return carService.addCar(body, true);
  }

  //Admin
  @PutMapping("/{carId}")
  public void editCar(@RequestBody CarRequest body, @PathVariable int carId) {
    carService.editCar(body, carId);
  }

  //Security ADMIN
  @PatchMapping("/rentalprice/{carId}/{newPrice}")
  public void setNewPrice(@PathVariable int carId, @PathVariable double newPrice) {
    carService.setPricePrDay(carId, newPrice);
  }

  //Security ALL (Not Authenticated)
  @GetMapping
  public List<CarResponse> getCars() {
    return carService.getCars(false);
  }

  //Security ADMIN
  @GetMapping("/admin")
  public List<CarResponse> getCarsWithAllInfo() {
    return carService.getCars(true);
  }

  //Security USER
  @GetMapping(path = "/{carId}")
  public CarResponse getCarById(@PathVariable int carId) throws Exception {
    CarResponse response = carService.findCarById(carId, false);
    return response;
  }
  //Security ADMIN
  @GetMapping(path = "/admin/{carId}")
  public CarResponse getCarByIdWithAllInfo(@PathVariable int carId) throws Exception {
    CarResponse response = carService.findCarById(carId, true);
    return response;
  }
}