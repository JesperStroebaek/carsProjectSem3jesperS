package carsProjectSem3.cars.service;

import carsProjectSem3.cars.dto.ReservationResponse;
import carsProjectSem3.cars.entity.Car;
import carsProjectSem3.cars.entity.Member;
import carsProjectSem3.cars.entity.Reservation;
import carsProjectSem3.cars.repository.CarRepository;
import carsProjectSem3.cars.repository.MemberRepository;
import carsProjectSem3.cars.repository.ReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
  MemberRepository memberRepository;
  CarRepository carRepository;
  ReservationRepository reservationRepository;

  public ReservationService(MemberRepository memberRepository, CarRepository carRepository, ReservationRepository reservationRepository) {
    this.memberRepository = memberRepository;
    this.carRepository = carRepository;
    this.reservationRepository = reservationRepository;
  }

  public void reserveCar(String memberId, int carId, LocalDate dateToReserve){
    Member member = memberRepository.findById(memberId).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"No member with this id found"));
    //Observe in the following, this strategy requires two round trips to the database
    Car car = carRepository.findById(carId).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"No car with this id found"));
    if(reservationRepository.existsByCarIdAndRentalDate(car.getId(),dateToReserve)){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Car is already reserved on this date");
    }
    Reservation reservation = new Reservation(member,car,dateToReserve);
    reservationRepository.save(reservation);
  }

  public void reserveCarV2(String memberId, int carId, LocalDate dateToReserve){
    Member member = memberRepository.findById(memberId).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"No member with this id found"));
    //Observe in the following, this strategy requires only ONE round trip to the database
    Car car = carRepository.findCarByIdIfNotAlreadyReserved(carId, dateToReserve).orElseThrow(()->
       new ResponseStatusException(HttpStatus.BAD_REQUEST,"Car is already reserved on this date"));
    Reservation reservation = new Reservation(member,car,dateToReserve);
    reservationRepository.save(reservation);
  }

  public List<ReservationResponse> getReservations(){
    List<Reservation> reservations = reservationRepository.findAll();
    List<ReservationResponse> response = reservations.stream().map(res-> new ReservationResponse(res)).collect(Collectors.toList());
    return response;

  }
}
