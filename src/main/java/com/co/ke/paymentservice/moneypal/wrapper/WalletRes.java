package com.co.ke.paymentservice.moneypal.wrapper;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;
@Data
public class WalletRes {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("amount")
    private float amount;
    @JsonProperty("userid")
    private UUID userid;
    @JsonProperty("userid2")
    private String userid2;
}
