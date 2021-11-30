package com.co.ke.paymentservice.moneypal.service;

import com.co.ke.paymentservice.moneypal.dto.PaymentDto;
import com.co.ke.paymentservice.moneypal.model.Payment;
import com.co.ke.paymentservice.moneypal.repository.PaymentRepository;
import com.co.ke.paymentservice.moneypal.wrapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PaymentServices {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${userServiceUrl}")
    public String userServiceUrl;

    @Value("${walletServiceUrl}")
    public String walletServiceUrl;

    @Value("${billingServiceUrl}")
    public String billingServiceUrl;

    @Value("${emailServiceUrl}")
    public String emailServiceUrl;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AmqpTemplate rabbitTemplate1;

    @Value("${exchange}")
    String exchange;

    @Value("${bindingKey}")
    private String routingkey;

    @Value("${bindingKey1}")
    private String routingkey1;
    // make payment
    public GeneralResponseWrapper paymentToRQM(PaymentDto paymentDto) {
        GeneralResponseWrapper generalResponseWrapper = new GeneralResponseWrapper();
        Payment payment = new Payment();
        try{
            if(paymentDto.getPaidTo_email().isEmpty() || paymentDto.getPaidTo_email().equals("") ){
                // save payment then send data to RQM for wallet top
                //update Status of payment
                payment.setPayment_amount(paymentDto.getAmountPaid());
                payment.setStatus(true);
                //  payment.setUserID_paidBy(b1.getId());
                payment.setUserEmail_paidBy(paymentDto.getPaidBy_email().trim().toUpperCase());
                payment.setUserEmail_paidTo(null);
                paymentRepository.save(payment);

                //send data to RMQ
                rabbitTemplate1.convertAndSend(exchange, routingkey,payment);
                System.out.println("++++++++++ RMQ section");
                System.out.println("Payment Details sent to RMQ for Wallet Top UP: "+payment);
                //====================================

            }
            else{
                //SAVE PAYYMENT  SEND DATA TO RQM for Billing service
                payment.setPayment_amount(paymentDto.getAmountPaid());
                payment.setStatus(true);
                payment.setUserEmail_paidBy(paymentDto.getPaidBy_email().trim().toUpperCase());
                payment.setUserEmail_paidTo(paymentDto.getPaidTo_email().trim().toUpperCase());
                paymentRepository.save(payment);

                //send data to RQM
                rabbitTemplate1.convertAndSend(exchange, routingkey1,payment);
                System.out.println("++++++++++ RMQ section");
                System.out.println("Payment Details sent to RMQ for Billing Service: "+payment);
                //====================================

            }

            generalResponseWrapper.setResponseCode(200);
            generalResponseWrapper.setResponseMessage("Payment Made successfully");
            generalResponseWrapper.setResponseBody(payment);
        }
        catch(Exception ex){
            log.error(ex.getMessage());
            ex.printStackTrace();
            generalResponseWrapper.setResponseCode(500);
            generalResponseWrapper.setResponseMessage("Internal Server Error");
            generalResponseWrapper.setResponseBody(null);
        }
        return generalResponseWrapper;
    }

    public GeneralResponseWrapper payment(PaymentDto paymentDto){
        GeneralResponseWrapper generalResponseWrapper=new GeneralResponseWrapper();
        Payment payment=new Payment();
        try {
            log.info("Check if User with that ID exist");
            String userName1=paymentDto.getPaidBy_email();
            String userName2=paymentDto.getPaidTo_email();
            double amount=paymentDto.getAmountPaid();
        //    String url=userServiceUrl + "/validateUserName/";
            //validate user by email
            String url=userServiceUrl + "/getUser/";
            ResponseEntity<Wrapper2> generalResponseWrapper1 = restTemplate.getForEntity(url+ userName1, Wrapper2.class);
            int res=generalResponseWrapper1.getStatusCodeValue();
            if(res == 200 && generalResponseWrapper1.getBody() != null){
                Wrapper2 wrapper2 =generalResponseWrapper1.getBody();
                Body1Response b1=wrapper2.getResponseBody();
                log.info("ID : {}",b1.getId());
                if(userName2.isEmpty() || userName2.equals("")){
                    // upload on wallet service
                    String url2=walletServiceUrl +"/" +userName1 +"/"+amount;
                    ResponseEntity<WalletResponseWrapper> generalResponseWrapper2 = restTemplate.getForEntity(url2, WalletResponseWrapper.class);
                    int res1=generalResponseWrapper2.getStatusCodeValue();
                    if(res1 == 200 || res1==201 && generalResponseWrapper2.getBody() != null){
                        WalletRes walletRes=generalResponseWrapper2.getBody().getResponseBody();
                       //update Status of payment
                        payment.setPayment_amount(amount);
                        payment.setStatus(true);
                      //  payment.setUserID_paidBy(b1.getId());
                        payment.setUserEmail_paidBy(paymentDto.getPaidBy_email().trim().toUpperCase());
                       // payment.setUserID_paidTo(null);
                        //save the payment details
                        paymentRepository.save(payment);


                        //===========================new implementation
                        rabbitTemplate1.convertAndSend(exchange, routingkey,payment);
                        System.out.println("++++++++++ RMQ section");
                        System.out.println("Payment Details sent to RMQ: "+payment);
                        //====================================

                        generalResponseWrapper.setResponseCode(200);
                        generalResponseWrapper.setResponseMessage("Payment Made successfully");
                        generalResponseWrapper.setResponseBody(payment);

                    }
                    else{
                        payment.setPayment_amount(amount);
                        payment.setStatus(false);
//                        payment.setUserID_paidBy(b1.getId());
//                        payment.setUserID_paidTo(null);
                        payment.setUserEmail_paidBy(paymentDto.getPaidBy_email().trim().toUpperCase());
                        //save the payment details
                        paymentRepository.save(payment);
                        log.info("updating the wallet failed hence payment failed ");
                        generalResponseWrapper.setResponseCode(res1);
                        generalResponseWrapper.setResponseMessage("Payment Failed");
                        generalResponseWrapper.setResponseBody(payment);
                    }
                }
                else{
                    // notify the Billing service
                    BillingWrapper billingWrapper=new BillingWrapper();
                    billingWrapper.setBillAmount(paymentDto.getAmountPaid());
                    billingWrapper.setEmail_from(paymentDto.getPaidBy_email().trim().toUpperCase());
                    billingWrapper.setEmail_to(paymentDto.getPaidTo_email().trim().toUpperCase());
                    String url1=billingServiceUrl + "save";
                    ResponseEntity<BillingWrapperResponse> generalResponseWrapper2 = restTemplate.postForEntity(url1,billingWrapper, BillingWrapperResponse.class);
                    int res1=generalResponseWrapper2.getStatusCodeValue();
                    if(res1==200 && generalResponseWrapper2.getBody() != null){
                        BillingWrapperResponse billingWrapperResponse= generalResponseWrapper2.getBody();
                        int respCode=billingWrapperResponse.getResponseCode();
                        if(respCode==200){
                            payment.setStatus(true);
                        }
                        else{
                            payment.setStatus(false);
                        }
                        payment.setPayment_amount(amount);
                       // payment.setUserID_paidBy(b1.getId());
                        payment.setUserEmail_paidBy(paymentDto.getPaidBy_email().trim().toUpperCase());
                        payment.setUserEmail_paidTo(paymentDto.getPaidTo_email().trim().toUpperCase());
                        //save the payment details
                        paymentRepository.save(payment);


                        generalResponseWrapper.setResponseMessage(billingWrapperResponse.getResponseMessage());
                        generalResponseWrapper.setResponseCode(respCode);
                        generalResponseWrapper.setResponseBody(payment);

                        if(respCode==200){
                            //TODO :   CALL EMAIL SERVICE TO SEND EMAIL
                        }
                    }
                    else{
                        payment.setPayment_amount(amount);
                        payment.setStatus(false);
                       // payment.setUserID_paidBy(b1.getId());
                        payment.setUserEmail_paidBy(paymentDto.getPaidBy_email().trim().toUpperCase());
                        payment.setUserEmail_paidTo(paymentDto.getPaidTo_email().trim().toUpperCase());
                        //save the payment details
                        paymentRepository.save(payment);
                        generalResponseWrapper.setResponseCode(res1);
                        generalResponseWrapper.setResponseMessage("Payment Failed");
                        generalResponseWrapper.setResponseBody(payment);
                    }


                    // call email service

                }

            }
            else{
                generalResponseWrapper.setResponseCode(res);
                generalResponseWrapper.setResponseMessage("User NOT Found with :"+userName1);
                generalResponseWrapper.setResponseBody(payment);
            }
        }
        catch(Exception ex){
            log.error(ex.getMessage());
            ex.printStackTrace();
            generalResponseWrapper.setResponseCode(500);
            generalResponseWrapper.setResponseMessage("Internal Server Error");
            generalResponseWrapper.setResponseBody(null);
        }
        return generalResponseWrapper;
    }

    public GeneralResponseWrapper getAll(){
        GeneralResponseWrapper generalResponseWrapper=new GeneralResponseWrapper();
        try{
            List<Payment> all=paymentRepository.findAll();
            generalResponseWrapper.setResponseCode(200);
            generalResponseWrapper.setResponseMessage("Successful");
            generalResponseWrapper.setResponseBody(all);
        }
        catch(Exception ex){
            log.error(ex.getMessage());
            ex.printStackTrace();
            generalResponseWrapper.setResponseCode(500);
            generalResponseWrapper.setResponseMessage("Internal Server Error");
            generalResponseWrapper.setResponseBody(null);
        }
        return generalResponseWrapper;
    }
}
