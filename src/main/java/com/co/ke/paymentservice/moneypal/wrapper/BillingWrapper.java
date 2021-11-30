package com.co.ke.paymentservice.moneypal.wrapper;

import lombok.Data;

@Data
public class BillingWrapper {
    //    private UUID userId_from;
//    private UUID userId_to;
    private String email_from;
    private String email_to;
    private float billAmount;
}
