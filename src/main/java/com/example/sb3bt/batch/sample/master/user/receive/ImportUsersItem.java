package com.example.sb3bt.batch.sample.master.user.receive;

import java.time.LocalDate; // 文字列の日付を LocalDate に変換するために使用
import com.example.sb3bt.common.entity.Users; // 変換先のユーザーエンティティ

import lombok.Data; // Lombokを使ってgetter/setterなどを自動生成

/**
 * ファイルから読み込んだユーザーデータを保持する中間クラス
 */
@Data
public class ImportUsersItem {

    // ユーザーID
    private Integer id;

    // ユーザー名
    private String name;

    // 所属部署
    private String department;

    // 登録日
    private String createdAt;

    /**
     * このImportUsersItemを Usersエンティティに変換するメソッド
     *
     * @return Users オブジェクト（データベース登録用）
     */
    public Users toUsers() {

        // 空の Users オブジェクトを作成
        Users users = new Users();

        // 各フィールドをコピー
        users.setId(id);
        users.setName(name);
        users.setDepartment(department);

        // 文字列の日付を LocalDate に変換してセット
        users.setCreatedAt(LocalDate.parse(createdAt));

        // 完成した Users オブジェクトを返す
        return users;
    }
}
