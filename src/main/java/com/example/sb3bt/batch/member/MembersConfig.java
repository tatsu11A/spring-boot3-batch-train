package main.java.com.example.sb3bt.batch.member;

import com.example.sb3bt.common.entity.Members;
import com.example.sb3bt.common.mapper.MembersMapper;
import com.example.sb3bt.core.exception.CustomSkipPolicy;
import com.example.sb3bt.core.listener.LogChunkListener;
import com.example.sb3bt.core.listener.LogJobListener;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class MembersConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final LogChunkListener logChunkListener;
    private final LogJobListener logJobListener;

    private final MembersProcessor membersProcessor;
    private final MembersWriter membersWriter;
    private final MembersMapper membersMapper;

    @Bean
    public FlatFileItemReader<MembersItem> reader() {
        FlatFileItemReader<MembersItem> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource("input-data/members.csv"));

        reader.setLineMapper(new DefaultLineMapper<MembersItem>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames("contractId", "memberId", "productId", "cardNumber", "registrationDate");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<MembersItem>() {{
                setTargetType(MembersItem.class);
            }});
        }});

        return reader;
    }

    @Bean
    public Tasklet truncateTasklet() {
        return (contribution, chunkContext) -> {
            membersMapper.truncate();
            return null;
        };
    }

    @Bean
    public Job importMembersJob() {
        return new JobBuilder("importMembersJob", jobRepository)
                .start(step1Truncate())
                .next(step2ImportCsv())
                .listener(logJobListener)
                .build();
    }

    @Bean
    public Step step1Truncate() {
        return new StepBuilder("importMembersStep1", jobRepository)
                .tasklet(truncateTasklet(), platformTransactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step step2ImportCsv() {
        return new StepBuilder("importMembersStep2", jobRepository)
                .<MembersItem, Members>chunk(10, platformTransactionManager)
                .reader(reader())
                .processor(membersProcessor)
                .writer(membersWriter)
                .faultTolerant()
                .skipPolicy(new CustomSkipPolicy())
                .listener(logChunkListener)
                .allowStartIfComplete(true)
                .build();
    }
}
