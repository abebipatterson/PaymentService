package com.co.ke.paymentservice.moneypal;

import com.co.ke.paymentservice.moneypal.dto.PaymentDto;
import com.co.ke.paymentservice.moneypal.model.Payment;
import com.co.ke.paymentservice.moneypal.repository.PaymentRepository;
import com.co.ke.paymentservice.moneypal.service.PaymentServices;
import com.co.ke.paymentservice.moneypal.wrapper.GeneralResponseWrapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
class MoneypalApplicationTests {

    @Autowired
    private PaymentServices paymentServices;

    @MockBean
    private PaymentRepository paymentRepository;

    @Test
    void contextLoads() {

    }
//@Test
//public void getAllPaymentTest(){
//        when(paymentRepository.findAll()).thenReturn(Stream.of(
//                new Payment((long) 1,null,null,500.00,true,"nelson@gmail.com",null),
//                new Payment((long) 2,null,null,700.00,true,"vincent@gmail.com",null),
//                new Payment((long) 1,null,null,5000.00,true,"nelson45@gmail.com","vin@gmail.com")
//                )
//                .collect(Collectors.toList()));
//    GeneralResponseWrapper g=paymentServices.getAll();
//    List<Payment> p= (List<Payment>) g.getResponseBody();
//        assertEquals(3,p.size());
//
//}
//
//    @Test
//    public void createAndSavePaymentTest(){
//        Payment p=new Payment((long) 1,null,null,500.00,true,"nelson@gmail.com",null);
//        when(paymentRepository.save(p)).thenReturn(p);
//        PaymentDto paymentDto=new PaymentDto();
//        paymentDto.setAmountPaid((float) 500.00);
//        paymentDto.setPaidBy_email("nelson@gmail.com");
//       Payment p1= (Payment) paymentServices.payment(paymentDto).getResponseBody();
//        assertEquals(p.getUserEmail_paidBy(),p.getUserEmail_paidBy());
//        //assertNotNull(p1.getPayment_amount());
//
//    }



}
