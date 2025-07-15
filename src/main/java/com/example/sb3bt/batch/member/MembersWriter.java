package com.example.sb3bt.batch.member;

import org.springframework.batch.item.Chunk; 
import org.springframework.batch.item.ItemWriter; 
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.example.sb3bt.common.entity.Members;
import com.example.sb3bt.common.mapper.MembersMapper;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class MembersWriter implements ItemWriter<Members> {
    
    private final MembersMapper membersMapper;

    @Override
    public void write(@NonNull Chunk<? extends Members> list) throws Exception {
        membersMapper.bulkInsert(list.getItems());
;
    }
}
