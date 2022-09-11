package carsProjectSem3.cars.repository;

import carsProjectSem3.cars.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car,Integer> {

  List<Car> findCarByBrand(String brand);
  List<Car> findCarByPricePrDayBetween(double min, double max);

  @Query("select c from Car c LEFT JOIN FETCH c.reservations res WHERE c.id= :carId AND (res IS empty  OR res.rentalDate <> :date)")
  Optional<Car> findCarByIdIfNotAlreadyReserved(int carId, LocalDate date);



}
