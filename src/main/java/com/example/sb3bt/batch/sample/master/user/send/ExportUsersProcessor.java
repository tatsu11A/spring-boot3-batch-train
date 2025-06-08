package com.example.sb3bt.batch.sample.master.user.send;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.sb3bt.common.entity.Users;

@Component // Spring がコンポーネントとして管理（DI対象になる）
public class ExportUsersProcessor implements ItemProcessor<Users, ExportUsersItem> {

    /**
     * Usersエンティティを、CSV出力用のExportUsersItemに変換する処理。
     * Spring Batchの「プロセッサ」として機能する。
     *
     * @param users DBから取得したユーザー情報（Usersエンティティ）
     * @return ExportUsersItem（CSV出力用オブジェクト）
     */
    @Override
    public ExportUsersItem process(@SuppressWarnings("null") Users users) throws Exception {

        // ExportUsersItem のインスタンスを生成
        ExportUsersItem item = new ExportUsersItem();

        // Usersエンティティの各フィールドをコピー
        item.setId(users.getId());
        item.setName(users.getName());
        item.setDepartment(users.getDepartment());

        // LocalDate型 → String型 に変換
        item.setCreatedAt(users.getCreatedAt().toString());

        return item;
    }
}
