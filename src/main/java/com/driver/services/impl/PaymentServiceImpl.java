package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation=reservationRepository2.findById(reservationId).get();
        Payment payment=null;

        if(amountSent<reservation.getNumberOfHours()*reservation.getSpot().getPricePerHour())
            throw new Exception("Insufficient Amount");

        if(mode.equalsIgnoreCase("cash")){
            payment=new Payment();
            payment.setPaymentMode(PaymentMode.CASH);
            payment.setPaymentCompleted(true);

            reservation.getSpot().setOccupied(true);
            reservation.setPayment(payment);

            payment.setReservation(reservation);

            reservationRepository2.save(reservation);

        }
        else if(mode.toLowerCase().equals("upi")){
            payment=new Payment();
            payment.setPaymentMode(PaymentMode.UPI);
            payment.setPaymentCompleted(true);

            reservation.getSpot().setOccupied(true);
            reservation.setPayment(payment);

            payment.setReservation(reservation);
            reservationRepository2.save(reservation);
        }
        else if (mode.toLowerCase().equals("card")) {
            payment=new Payment();
            payment.setPaymentMode(PaymentMode.CARD);
            payment.setPaymentCompleted(true);

            reservation.getSpot().setOccupied(true);
            reservation.setPayment(payment);

            payment.setReservation(reservation);

            reservationRepository2.save(reservation);
        }
        else{
            throw new Exception("Payment mode not detected");
        }

        return payment;
    }
}
