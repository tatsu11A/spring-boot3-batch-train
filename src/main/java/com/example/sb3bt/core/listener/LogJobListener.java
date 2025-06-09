package com.example.sb3bt.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

/**
 * ジョブ実行後に、入力件数とスキップ件数をログ出力するリスナー。
 */
@Component
public class LogJobListener implements JobExecutionListener {

    // ロガーの初期化
    private final Logger logger = LoggerFactory.getLogger(LogJobListener.class);

    /**
     * ジョブ終了時に呼び出されるメソッド。
     * ジョブ全体の読み込み件数とスキップ件数をログ出力する。
     */
    @Override
    public void afterJob(@SuppressWarnings("null") JobExecution jobExecution) {
        int inputCount = 0;
        int skipCount = 0;

        // すべてのステップの実行結果を集計
        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
            inputCount += stepExecution.getReadCount() + stepExecution.getReadSkipCount(); // 読み込み件数（スキップ含む）
            skipCount += stepExecution.getSkipCount(); // スキップ件数（処理・書き込み含む）
        }

        // ログ出力
        logger.info("AfterJob:input={}, skip={}", inputCount, skipCount);
    }
}
