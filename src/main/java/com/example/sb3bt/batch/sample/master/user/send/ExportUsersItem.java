package com.example.sb3bt.batch.sample.master.user.send;

import java.time.LocalDate;

import com.example.sb3bt.common.entity.Users;

import lombok.Data;

@Data // Getter, Setter, toString などを自動生成するLombokのアノテーション
public class ExportUsersItem {

    // ユーザーID
    private Integer id;

    // 名前
    private String name;

    // 部署名
    private String department;

    // 登録日（文字列として保持）
    private String createdAt;

    /**
     * ExportUsersItem → Usersエンティティへ変換するメソッド
     * @return Usersエンティティ（createdAt は LocalDate 型）
     */
    public Users toUser() {
        // Usersエンティティのインスタンスを作成
        Users user = new Users();

        // 各フィールドをセット（文字列を LocalDate に変換してセット）
        user.setId(id);
        user.setName(name);
        user.setDepartment(department);
        user.setCreatedAt(LocalDate.parse(createdAt)); 

        return user;
    }
}
