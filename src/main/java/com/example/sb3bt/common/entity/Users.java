package com.example.sb3bt.common.entity;

import java.time.LocalDate; // 年月日を表すクラス（時間は含まない）

import lombok.Data; // Lombokのアノテーション。getter/setterなどを自動生成する

/**
 * ユーザー情報を表すエンティティクラス。
 * データベースやCSVファイルの1行に相当する。
 */
@Data 
// getter/setter/toString/equals/hashCodeなどを自動生成
public class Users {

    // ユーザーID
    private Integer id;

    // ユーザーの名前
    private String name;

    // 所属部署
    private String department;

    // 登録日
    private LocalDate createdAt;
}