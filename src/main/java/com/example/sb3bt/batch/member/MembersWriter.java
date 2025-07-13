package main.java.com.example.sb3bt.batch.member;

import com.example.sb3bt.common.entity.Members;
import com.example.sb3bt.common.mapper.MembersMapper;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class MembersWriter implements ItemWriter<Members> {

    private final MembersMapper membersMapper;

    public MembersWriter(MembersMapper membersMapper) {
        this.membersMapper = membersMapper;
    }

    @Override
    public void write(List<? extends Members> items) throws Exception {
        membersMapper.bulkInsert(items);
    }
}
