package com.example.sb3bt.core.exception;

/**
 * バッチ処理中に発生するスキップ可能な例外を表すカスタム例外クラス。
 * RuntimeException を継承しているため、チェックされない例外（unchecked exception）。
 */
public class SkipException extends RuntimeException {

    /**
     * コンストラクタ：エラーメッセージを受け取って親クラスに渡す。
     *
     * @param message スキップの原因となるエラーメッセージ
     */
    public SkipException(String message) {
        super(message);
    }
}
