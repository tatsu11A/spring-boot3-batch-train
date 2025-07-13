package com.example.sb3bt.batch.member;

import com.example.sb3bt.common.entity.Members;
import org.springframework.batch.item.ItemProcessor;
import java.time.LocalDate;


public class MembersProcessor implements ItemProcessor<MembersItem, Members> {

    @Override
    public Members process(MembersItem item) throws Exception {
        Members member = new Members();
        member.setContractId(item.getContractId());
        member.setMemberId(item.getMemberId());
        member.setProductId(item.getProductId());
        member.setCardNumber(item.getCardNumber());
        member.setRegistrationDate(LocalDate.parse(item.getRegistrationDate()));
        return member;
    }
}
