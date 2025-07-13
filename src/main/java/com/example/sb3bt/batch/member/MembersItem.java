package main.java.com.example.sb3bt.batch.member;

import lombok.Data;

@Data
public class MembersItem {
    private Long contractId;
    private Long memberId;
    private Long productId;
    private String cardNumber;
    private String registrationDate;
}
