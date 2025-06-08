package com.example.sb3bt.batch.sample.master.user.receive;

import org.springframework.batch.item.Chunk; // バッチでまとめて処理される単位（リスト）
import org.springframework.batch.item.ItemWriter; // 書き込みインターフェース
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.example.sb3bt.common.entity.Users;
import com.example.sb3bt.common.mapper.UsersMapper;

import lombok.RequiredArgsConstructor;

/**
 * バッチ処理で変換された Users データをデータベースに登録する Writer クラス。
 */
@Component // Spring によってコンポーネントとして登録される
@RequiredArgsConstructor // final フィールドに自動でコンストラクタを生成（DI用）
public class ImportUsersWriter implements ItemWriter<Users> {
    
    // MyBatis の Mapper を通して DB 操作を行う
    private final UsersMapper usersMapper;

    /**
     * バッチでまとめて渡された Users データを DB に書き込む処理。
     * 
     * @param list Chunk（一定件数の Users データリスト）
     */
    @Override
    public void write(@NonNull Chunk<? extends Users> list) throws Exception {

        // 一括登録（パフォーマンス重視）
        usersMapper.bulkinsert(list.getItems());

        // ↓以下は参考例：1件ずつ登録する場合
        // for (Users users: list) {
        //     // DBに登録する
        //     usersMapper.regist(users);
        // }
    }
}
