package com.example.sb3bt.batch.sample.master.user.receive;

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

import com.example.sb3bt.common.entity.Users;
import com.example.sb3bt.common.mapper.UsersMapper;
import com.example.sb3bt.core.exception.CustomSkipPolicy;
import com.example.sb3bt.core.listener.LogChunkListener;
import com.example.sb3bt.core.listener.LogJobListener;

import lombok.RequiredArgsConstructor;

@Configuration // Spring の設定クラス
@RequiredArgsConstructor // final な依存関係を自動で注入
public class ImportUsersConfig {

    // Spring Batch の各種依存コンポーネント
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final LogChunkListener logChunkListener;
    private final LogJobListener logJobListener;

    // バッチ処理用の独自コンポーネント
    private final ImportUsersProcessor userImportProcessor;
    private final ImportUsersWriter userImportWriter;
    private final UsersMapper usersMapper;

    /**
     * CSVファイルから1行ずつ読み込むリーダー
     */
    @Bean
    public FlatFileItemReader<ImportUsersItem> reader() {
        FlatFileItemReader<ImportUsersItem> reader = new FlatFileItemReader<>();

        // 最初の1行（ヘッダー）をスキップ
        reader.setLinesToSkip(1);
        
        // 読み込むCSVファイルの場所を指定
        reader.setResource(new FileSystemResource("input-data/user.csv"));

        // 1行の文字列をImportUsersItemオブジェクトに変換する設定
        reader.setLineMapper(new DefaultLineMapper<ImportUsersItem>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames("id", "name", "department", "createdAt"); // カラム名をマッピング
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<ImportUsersItem>() {{
                setTargetType(ImportUsersItem.class); // マッピング対象クラスを指定
            }});
        }});

        return reader;
    }

    /**
     * users テーブルのデータを削除するタスクレット（ステップ1で使用）
     */
    @Bean
    public Tasklet trancateTasklet() {
        return (contribution, chunkContext) -> {
            usersMapper.truncate(); // usersテーブルのデータを全削除
            return null; // 終了ステータス（nullは正常終了を意味する）
        };
    }

    /**
     * バッチジョブの定義（2ステップ構成）
     */
    @Bean
    public Job importUsersJob() {
        return new JobBuilder("importUsersJob", jobRepository)
                .start(importUsersStep1()) // ステップ1: truncate
                .next(importUsersStep2())  // ステップ2: CSV読み込み → DB登録
                .listener(logJobListener) // ジョブ単位のログ出力
                .build();
    }

    /**
     * ステップ1: テーブルを空にするタスク
     */
    @Bean
    public Step importUsersStep1() {
        return new StepBuilder("importUsersStep1", jobRepository)
                .tasklet(trancateTasklet(), platformTransactionManager)
                .allowStartIfComplete(true) // 何度でも再実行可能
                .build();
    }

    /**
     * ステップ2: CSV → Users → DB登録
     */
    @Bean
    public Step importUsersStep2() {
        return new StepBuilder("importUsersStep2", jobRepository)
                .<ImportUsersItem, Users>chunk(10, platformTransactionManager) // 10件単位で処理
                .reader(reader())                    // CSVから読み込み
                .processor(userImportProcessor)      // 加工
                .writer(userImportWriter)            // DB書き込み
                .allowStartIfComplete(true)          // 再実行可能
                .faultTolerant()                     // 耐障害設定
                .skipPolicy(new CustomSkipPolicy())  // スキップルール定義
                .listener(logChunkListener)          // チャンク単位のログ出力
                .build();
    }
}
