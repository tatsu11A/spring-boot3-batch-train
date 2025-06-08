package com.example.sb3bt.batch.sample.master.user.receive;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.example.sb3bt.common.entity.Users;
import com.example.sb3bt.core.exception.SkipException;

/**
 * ImportUsersItem を Users に変換するバッチ用プロセッサ。
 * 営業部のデータは登録対象外としてスキップする。
 */
@Component // Spring によりコンポーネントとして自動登録される
public class ImportUsersProcessor implements ItemProcessor<ImportUsersItem, Users> {

    /**
     * 1件のデータを処理するメソッド。
     * 入力（ImportUsersItem） → 出力（Users）への変換を行う。
     *
     * @param item 読み込んだ1行分のユーザーデータ（CSV等）
     * @return Users オブジェクト（DB登録用）
     * @throws Exception 処理に失敗した場合（スキップも含む）
     */
    @Override
    public Users process(@NonNull ImportUsersItem item) throws Exception {

        // 「営業」部門のデータは処理対象外とする（スキップ）
        if ("営業".equals(item.getDepartment())) {
            // 独自定義のスキップ用例外をスロー（Spring Batch がこの例外を検知してスキップ）
            throw new SkipException("営業は登録できません");
        }

        // 部門が「営業」以外の場合は、Users オブジェクトに変換して返す
        return item.toUsers();
    }
}
