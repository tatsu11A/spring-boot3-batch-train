package com.example.sb3bt.common.entity;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Members {
    private Long contractId;
    private Long memberId;
    private Long productId;
    private String cardNumber;
    private LocalDate registrationDate;
}