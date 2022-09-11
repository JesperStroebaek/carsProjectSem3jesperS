package carsProjectSem3.cars.service;

import carsProjectSem3.cars.dto.CarRequest;
import carsProjectSem3.cars.dto.CarResponse;
import carsProjectSem3.cars.entity.Car;
import carsProjectSem3.cars.repository.CarRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarService {

  CarRepository carRepository;

  public CarService(CarRepository carRepository) {
    this.carRepository = carRepository;
  }

  public CarResponse addCar(CarRequest carRequest, boolean includeAll){
    Car newCar = CarRequest.getCarEntity(carRequest);
    newCar = carRepository.save(newCar);
    return new CarResponse(newCar, includeAll);
  }

  public void editCar(CarRequest body, int carId){
    Car car = carRepository.findById(carId).orElseThrow(
            ()->  new ResponseStatusException(HttpStatus.BAD_REQUEST,"Car with this ID dont exist"));
    car.setBrand(body.getBrand());
    car.setModel(body.getModel());
    car.setPricePrDay(body.getPricePrDay());
    car.setBestDiscount(body.getBestDiscount());
    carRepository.save(car);
  }

  public List<CarResponse> getCars(boolean includeAll) {
    List<Car> cars = carRepository.findAll();
    List<CarResponse> response  = new ArrayList<>();
    for(Car car : cars){
      response.add(new CarResponse(car,includeAll));
    }
    //See MemberService for how to do this with streams and map
    return response;
  }

  public CarResponse findCarById(int id, boolean includeAll) throws Exception {
    Car found = carRepository.findById(id).orElseThrow(()->
            new ResponseStatusException(HttpStatus.NOT_FOUND,"Car not found"));
    return new CarResponse(found,includeAll);
  }

  public void setPricePrDay(int carId, double price) {
    Car car = carRepository.findById(carId).orElseThrow(()->
            new ResponseStatusException(HttpStatus.BAD_REQUEST,"Member with this username already exist"));
    car.setPricePrDay(price);
    carRepository.save(car);
  }

  //Dont implement this, you would (almost) NEVER delete a car in a real application
  //public void deleteById(int carId) {};

}
