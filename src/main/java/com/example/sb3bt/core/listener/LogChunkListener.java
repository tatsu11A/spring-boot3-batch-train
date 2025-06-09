package com.example.sb3bt.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

/**
 * チャンク実行後にログ出力するリスナー。
 * 読み込み件数・スキップ件数・コミット回数を記録する。
 */
@Component
public class LogChunkListener implements ChunkListener {

    // ロガーの初期化
    private final Logger logger = LoggerFactory.getLogger(LogChunkListener.class);

    /**
     * チャンク実行後に呼び出される。
     * 実行状況（読み込み、スキップ、コミット）をログ出力。
     */
    @Override
    public void afterChunk(@SuppressWarnings("null") ChunkContext context) {
        // ステップ実行情報を取得
        StepExecution stepExecution = context.getStepContext().getStepExecution();

        // 読み込み件数 = 読み込み成功 + 読み込みスキップ
        long inputCount = stepExecution.getReadCount() + stepExecution.getReadSkipCount();

        // 全体のスキップ件数
        long skipCount = stepExecution.getSkipCount();

        // チャンクのコミット回数
        long commitCount = stepExecution.getCommitCount();

        // ログ出力
        logger.info("AfterChunk:input={}, skip={}, commit={}", inputCount, skipCount, commitCount);
    }
}
