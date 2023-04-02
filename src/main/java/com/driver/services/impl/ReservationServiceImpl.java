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
        ParkingLot parkingLot=null;
        User user=null;
        Spot newSpot=null;
        Reservation reservation=null;
        try{
            parkingLot=parkingLotRepository3.findById(parkingLotId).get();
            System.out.println(parkingLot.getId()+" park");
        }catch (Exception e){
            throw new Exception("Cannot make reservation");
        }
        try{
            user=userRepository3.findById(userId).get();
            System.out.println(user.getId()+" user");
        }catch (Exception e){
            throw new Exception("Cannot make reservation");
        }
        int min=Integer.MAX_VALUE;
        for(Spot spot:parkingLot.getSpotList()) {
            if (numberOfWheels <= 2 && min >= spot.getPricePerHour() && !spot.getOccupied()) {
                min = spot.getPricePerHour();
                newSpot = spot;
            } else if (numberOfWheels <= 4 && (spot.getSpotType() == SpotType.FOUR_WHEELER || spot.getSpotType() == SpotType.OTHERS) && min >= spot.getPricePerHour()  && !spot.getOccupied()) {
                min = spot.getPricePerHour();
                newSpot = spot;
            } else if (numberOfWheels > 4 && (spot.getSpotType() == SpotType.OTHERS) && min >= spot.getPricePerHour()  && !spot.getOccupied()) {
                min = spot.getPricePerHour();
                newSpot = spot;
            }
        }
        System.out.println(parkingLot.getId()+" "+user.getId()+" "+newSpot.getId());
        if(newSpot==null) throw new Exception("Cannot make reservation");

        reservation=new Reservation();

        reservation.setSpot(newSpot);
        reservation.setNumberOfHours(timeInHours);

        user.getReservationList().add(reservation);
        newSpot.getReservationList().add(reservation);

        userRepository3.save(user);
        spotRepository3.save(newSpot);

        return reservation;
    }
}
