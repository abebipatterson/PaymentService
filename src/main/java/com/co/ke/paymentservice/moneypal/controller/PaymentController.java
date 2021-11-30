package com.co.ke.paymentservice.moneypal.controller;

import com.co.ke.paymentservice.moneypal.dto.PaymentDto;
import com.co.ke.paymentservice.moneypal.service.PaymentServices;
import com.co.ke.paymentservice.moneypal.wrapper.GeneralResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

@Autowired
private PaymentServices paymentService;

    @PostMapping("/makePayment")
    public ResponseEntity<GeneralResponseWrapper> payment(@RequestBody PaymentDto paymentDto){
        GeneralResponseWrapper generalResponseWrapper=paymentService.payment(paymentDto);
        return ResponseEntity.ok().body(generalResponseWrapper);
    }

    @PostMapping("/makePayment/rqm")
    public ResponseEntity<GeneralResponseWrapper> paymentToRQM(@RequestBody PaymentDto paymentDto){
        GeneralResponseWrapper generalResponseWrapper=paymentService.paymentToRQM(paymentDto);
        return ResponseEntity.ok().body(generalResponseWrapper);
    }

    @GetMapping("/getAll")
    public ResponseEntity<GeneralResponseWrapper> getpayment(){
        GeneralResponseWrapper generalResponseWrapper=paymentService.getAll();
        return ResponseEntity.ok().body(generalResponseWrapper);
    }
}
