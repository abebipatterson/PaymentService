package com.co.ke.paymentservice.moneypal.wrapper;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class GeneralResponseWrapper<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int responseCode;
    private String responseMessage;
    private Date date=new Date();
    private T responseBody;
}
