package com.co.ke.paymentservice.moneypal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue
    private Long id;
//    private UUID userID_paidBy;
//    private UUID userID_paidTo;
    private double payment_amount;
    private boolean status=false;
    private String userEmail_paidBy;
    private String userEmail_paidTo;
}
