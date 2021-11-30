package com.co.ke.paymentservice.moneypal.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentDto {
    private String paidBy_email;
    private String paidTo_email;
    private float amountPaid;

}
