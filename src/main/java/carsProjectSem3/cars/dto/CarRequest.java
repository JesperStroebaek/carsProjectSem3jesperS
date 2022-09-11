package carsProjectSem3.cars.dto;
import carsProjectSem3.cars.entity.Car;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarRequest {
  String brand;
  String model;
  double pricePrDay;
  double bestDiscount;

  public static Car getCarEntity(CarRequest cr){
    return Car.builder()
            .brand(cr.brand)
            .model(cr.model)
            .pricePrDay(cr.pricePrDay)
            .bestDiscount(cr.bestDiscount)
            .build();
  }

  public CarRequest(Car car){
    this.brand = car.getBrand();
    this.model = car.getModel();
    this.pricePrDay = car.getPricePrDay();
    this.bestDiscount = car.getBestDiscount();
  }

}
