package com.example.sb3bt.batch.sample.master.user.send;

import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.stereotype.Component;

@Component // Springにより自動的にコンポーネントとして認識される
public class ExportUsersFieldExtractor implements FieldExtractor<ExportUsersItem> {

    /**
     * ExportUsersItem の各フィールドを Object 配列に取り出して返す。
     * → この配列が 1 行分のデータとして CSV に出力される。
     *
     * @param item 1件分のデータ（ExportUsersItem）
     * @return 各フィールドの値を並べた Object 配列
     */
    @SuppressWarnings("null") // コンパイル時の警告抑制
    @Override
    public Object[] extract(ExportUsersItem item) {
        return new Object[] {
            item.getId().toString(),     // IDを文字列として取得
            item.getName(),              // 名前
            item.getDepartment(),        // 部署
            item.getCreatedAt()          // 登録日（文字列として保持されている）
        };
    }
}
