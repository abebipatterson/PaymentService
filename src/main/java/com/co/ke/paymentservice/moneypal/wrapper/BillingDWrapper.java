package com.co.ke.paymentservice.moneypal.wrapper;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class BillingDWrapper {
    private UUID id;
    //    private UUID userId_from;
//    private UUID userId_to;
    private String userEmail_from;
    private String userEmail_to;
    private int invoiceNumber;
    private float billAmount;
    private boolean status;
    private Date date;
}
