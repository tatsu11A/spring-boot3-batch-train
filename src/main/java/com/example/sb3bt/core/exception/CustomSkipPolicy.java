package com.example.sb3bt.core.exception;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

/**
 * 独自のスキップポリシーを定義するクラス。
 * SkipPolicyインターフェースを実装することで、バッチ処理中に発生した例外に対し、
 * スキップ可能かどうかの判断ロジックを定義する。
 */
public class CustomSkipPolicy implements SkipPolicy {

    /**
     * このメソッドは、各レコードの処理時に例外が発生した場合に呼ばれ、
     * その例外をスキップするかどうかを判定する。
     *
     * @param t 例外オブジェクト（発生したエラー）
     * @param skipCount 現時点でスキップされたアイテムの数
     * @return true の場合、その例外をスキップ（処理を継続）、false の場合は停止（エラーとして扱う）
     * @throws SkipLimitExceededException スキップ回数制限を超えた場合に投げられる例外
     */
    @Override
    public boolean shouldSkip(@SuppressWarnings("null") Throwable t, long skipCount) throws SkipLimitExceededException {
        // 例外が SkipException 型ならスキップ対象とする
        return t instanceof SkipException;
    }
}
