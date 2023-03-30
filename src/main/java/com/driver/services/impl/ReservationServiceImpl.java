package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        ParkingLot parkingLot;
        User user;
        Spot newSpot=null;
        Reservation reservation=null;
        Payment payment=new Payment();
        try{
            parkingLot=parkingLotRepository3.findById(parkingLotId).get();
            user=userRepository3.findById(userId).get();
        }catch (Exception e){
            throw new Exception("Cannot make reservation");
        }
        int min=Integer.MAX_VALUE;
        for(Spot spot:parkingLot.getSpotList()) {
            if (numberOfWheels == 2 && min >= spot.getPricePerHour()) {
                min = spot.getPricePerHour();
                newSpot = spot;
            } else if (numberOfWheels == 4 && (spot.getSpotType() == SpotType.FOUR_WHEELER || spot.getSpotType() == SpotType.OTHERS) && min >= spot.getPricePerHour()) {
                min = spot.getPricePerHour();
                newSpot = spot;
            } else if (numberOfWheels > 4 && (spot.getSpotType() == SpotType.OTHERS) && min >= spot.getPricePerHour()) {
                min = spot.getPricePerHour();
                newSpot = spot;
            }
        }
        if(newSpot==null) throw new Exception("1. Cannot make reservation");

        reservation=new Reservation();

        if(numberOfWheels==2)newSpot.setSpotType(SpotType.TWO_WHEELER);
        else if(numberOfWheels==4)newSpot.setSpotType(SpotType.FOUR_WHEELER);
        else newSpot.setSpotType(SpotType.OTHERS);

        reservation.setSpot(newSpot);
        reservation.setNumberOfHours(timeInHours);
        reservation.setPayment(payment);

        user.getReservationList().add(reservation);
        newSpot.getReservationList().add(reservation);

        reservationRepository3.save(reservation);
        userRepository3.save(user);
        spotRepository3.save(newSpot);

        return reservation;
    }
}
