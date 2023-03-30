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
        PaymentMode paymentMode;
        Payment payment=null;

        if(amountSent<reservation.getNumberOfHours()*reservation.getSpot().getPricePerHour())
            throw new Exception("Insufficient Amount");

        if(mode.toLowerCase().equals("cash")) paymentMode=PaymentMode.CASH;
        else if(mode.toLowerCase().equals("upi")) paymentMode=PaymentMode.UPI;
        else if (mode.toLowerCase().equals("card")) paymentMode=PaymentMode.CARD;
        else throw new Exception("Payment mode not detected ");

        payment=reservation.getPayment();
        payment.setPaymentMode(paymentMode);
        payment.setPaymentCompleted(true);
        payment.setReservation(reservation);

        reservation.getSpot().setOccupied(true);
        reservation.setPayment(payment);
        reservationRepository2.save(reservation);
        return payment;
    }
}
