package carsProjectSem3.cars.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import carsProjectSem3.cars.entity.Car;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarResponse {
  int id;
  String brand;
  String model;
  double pricePrDay;
  Double bestDiscount;  //To allow for null
  @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss",shape = JsonFormat.Shape.STRING)
  LocalDateTime created;
  @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss",shape = JsonFormat.Shape.STRING)
  LocalDateTime edited;

  public CarResponse(Car carEntity, boolean includeAll) {
    this.id = carEntity.getId();
    this.brand = carEntity.getBrand();
    this.model = carEntity.getModel();
    this.pricePrDay = carEntity.getPricePrDay();
    if(includeAll){
      this.bestDiscount =carEntity.getBestDiscount();
      this.created = carEntity.getCreated();
      this.edited = carEntity.getEdited();
    }
  }
}
