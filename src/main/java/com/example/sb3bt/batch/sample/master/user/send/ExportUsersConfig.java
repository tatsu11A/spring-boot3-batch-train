package com.example.sb3bt.batch.sample.master.user.send;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.sb3bt.common.entity.Users;
import com.example.sb3bt.common.mapper.UsersMapper;
import com.example.sb3bt.core.exception.CustomSkipPolicy;
import com.example.sb3bt.core.listener.LogChunkListener;
import com.example.sb3bt.core.listener.LogJobListener;

import lombok.RequiredArgsConstructor;

@Configuration // Springの設定クラスとして登録
@RequiredArgsConstructor // コンストラクタインジェクション用のLombokアノテーション
public class ExportUsersConfig {

    // ジョブやステップを作成するためのリポジトリ
    private final JobRepository jobRepository;

    // ジョブ・チャンクのログ出力リスナー
    private final LogJobListener logJobListener;
    private final LogChunkListener logChunkListener;

    // MyBatisのSQLセッション（DB接続に必要）
    private final SqlSessionFactory sqlSessionFactory;

    // トランザクション管理（バッチ処理で使用）
    private final PlatformTransactionManager platformTransactionManager;

    // プロセッサ：DBデータ → CSV用の変換処理
    private final ExportUsersProcessor userExportProcessor;

    // CSV出力で使う「フィールドの取り出し」処理
    private final ExportUsersFieldExtractor userExportFieldExtractor;

    /**
     * ユーザデータをDBから取得するリーダー（MyBatis使用）
     */
    @Bean
    public ItemReader<? extends Users> userExportReader() {
        return new MyBatisCursorItemReaderBuilder<Users>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId(UsersMapper.class.getName() + ".selectAll") // MapperのselectAll()を呼び出す
                .build();
    }

    /**
     * CSVファイルへ出力するためのライター
     */
    @Bean
    public FlatFileItemWriter<ExportUsersItem> userExportWriter() {
        return new FlatFileItemWriterBuilder<ExportUsersItem>()
                .encoding("UTF-8") // 文字コード
                .name("userExportWriter") // ライターの識別名
                .saveState(false) // 再実行時の状態保存なし
                .resource(new PathResource("output-data/users.csv")) // 出力先ファイル
                .lineSeparator("\r\n") // 改行コード
                .shouldDeleteIfEmpty(false) // データが空でもファイルを削除しない
                .delimited() // 区切り文字設定（CSV）
                .delimiter(",") // カンマ区切り
                .fieldExtractor(userExportFieldExtractor) // フィールドを配列化するクラス
                .build();
    }

    /**
     * ユーザエクスポートジョブ（ジョブ全体の定義）
     */
    @Bean
    public Job exportUsersJob() {
        return new JobBuilder("exportUsersJob", jobRepository)
                .start(exportUsersStep1()) // Step1からスタート
                .listener(logJobListener) // ジョブ単位のログ出力
                .build();
    }

    /**
     * ステップ1：DB → 変換 → CSV出力
     */
    @Bean
    public Step exportUsersStep1() {
        return new StepBuilder("exportUsersStep1", jobRepository)
                .<Users, ExportUsersItem>chunk(10, platformTransactionManager) // 10件ごとに処理（チャンク）
                .reader(userExportReader()) // リーダー
                .processor(userExportProcessor) // プロセッサ（Users → ExportUsersItem）
                .writer(userExportWriter()) // ライター（CSVへ出力）
                .allowStartIfComplete(true) // 再実行を許可
                .faultTolerant() // 例外が出ても処理を続行
                .skipPolicy(new CustomSkipPolicy()) // Skip対象の例外定義
                .listener(logChunkListener) // チャンク単位のログ出力
                .build();
    }
}
