package com.co.ke.paymentservice.moneypal.wrapper;

import lombok.Data;

import java.util.Date;
@Data
public class WalletResponseWrapper {
    private static final long serialVersionUID = 1L;
    private int responseCode;
    private String responseMessage;
    private Date date=new Date();
    private WalletRes responseBody;
}
