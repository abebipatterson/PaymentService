package com.co.ke.paymentservice.moneypal.repository;

import com.co.ke.paymentservice.moneypal.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {

    List<Payment> findAll();

}
