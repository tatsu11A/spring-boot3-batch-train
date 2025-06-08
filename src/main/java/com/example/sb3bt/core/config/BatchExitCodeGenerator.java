package com.example.sb3bt.core.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.autoconfigure.batch.JobExecutionEvent;
import org.springframework.context.ApplicationListener;

/**
 * バッチ処理の終了コードをカスタマイズし、
 * ジョブ実行イベントを監視するクラス。
 */
public class BatchExitCodeGenerator implements ExitCodeGenerator, ApplicationListener<JobExecutionEvent> {

    // 実行されたジョブの情報を格納するリスト
    private final List<JobExecution> executions = new ArrayList<>();

    /**
     * ジョブ実行イベントを受け取ったときに呼ばれるメソッド。
     * @param event JobExecutionEvent：バッチジョブの実行結果を含むイベント
     * 
     * このメソッドは、バッチジョブが実行されるたびに呼ばれ、
     * 実行結果（JobExecutionオブジェクト）を保持するリストに追加します。
     */
    @Override
    public void onApplicationEvent(@SuppressWarnings("null") JobExecutionEvent event) {
        executions.add(event.getJobExecution());
    }
    
    /**
     * Spring Bootのアプリケーション終了時に呼ばれ、
     * アプリケーションの終了コードを返すためのメソッド。
     * 
     * @return int 終了コード（0: 正常終了, 1: エラーあり）
     * 
     * このメソッドでは、記録された全てのバッチジョブの
     * ステップごとの実行結果を確認し、失敗例外が一つでもあれば
     * 終了コード1を返します。失敗がなければ終了コード0を返します。
     */
    @Override
    public int getExitCode() {
        // 全ジョブの実行結果を順に調査
        for (JobExecution execution : executions) {
            // それぞれのジョブのステップ実行結果をチェック
            for (StepExecution stepExecution : execution.getStepExecutions()) {
                // ステップで例外が発生しているかを確認
                if (!stepExecution.getFailureExceptions().isEmpty()) {
                    // 例外があれば終了コード1を返す（エラーを示す）
                    return 1;
                }
            }
        }
        // 例外が一つもなければ正常終了の終了コード0を返す
        return 0;
    }
}
