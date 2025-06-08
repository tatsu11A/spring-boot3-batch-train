package com.example.sb3bt.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.sb3bt.common.entity.Users;

@Mapper // このインターフェースが MyBatis のマッパーであることを示す
public interface UsersMapper {

    /**
     * ユーザー情報を1件登録するSQL。
     * Usersオブジェクトのプロパティが#{...}にマッピングされてINSERTされる。
     *
     * @param users 登録するユーザー情報
     */
    @Insert("insert into users(id, name, department, created_at) values(#{id}, #{name}, #{department}, #{createdAt})")
    void regist(Users users);

    /**
     * ユーザー情報を複数件一括登録するSQL。
     * MyBatisの<foreach>構文を使って、複数レコードを一気にINSERTする。
     *
     * @param list 登録するユーザーのリスト
     */
    @Insert({
        "<script>", // 動的SQLの開始
        "insert into users (id, name, department, created_at) values ",
        "<foreach collection='list' item='user' separator=','>", // listをループして、1件ずつuserとして取り出す
        "(#{user.id}, #{user.name}, #{user.department}, #{user.createdAt})",
        "</foreach>",
        "</script>" // 動的SQLの終了
    })
    void bulkinsert(List<? extends Users> list);

    /**
     * usersテーブルのデータを全て削除するSQL。
     * truncateはDELETEより高速で、IDの連番もリセットされる場合がある。
     */
    @Delete("truncate table users")
    void truncate();

    /**
     * usersテーブルの全件を取得するSQL。
     *
     * @return 全ユーザー情報
     */
    @Select("select * from users")
    Users selectAll(); 
}
