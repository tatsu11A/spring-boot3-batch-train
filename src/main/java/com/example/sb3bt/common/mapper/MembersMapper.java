package main.java.com.example.sb3bt.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.example.sb3bt.common.entity.Members;

@Mapper
public interface MembersMapper {

    /**
     * 会員登録情報を1件登録するSQL。
     *
     * @param member 登録する会員情報
     */
    @Insert("INSERT INTO member_registration(contract_id, member_id, product_id, card_number, registration_date) " +
            "VALUES(#{contractId}, #{memberId}, #{productId}, #{cardNumber}, #{registrationDate})")
    void insert(Members member);

    /**
     * 会員登録情報を複数件一括登録するSQL。
     *
     * @param list 登録する会員情報のリスト
     */
    @Insert({
        "<script>",
        "INSERT INTO member_registration(contract_id, member_id, product_id, card_number, registration_date) VALUES ",
        "<foreach collection='list' item='m' separator=','>",
        "(#{m.contractId}, #{m.memberId}, #{m.productId}, #{m.cardNumber}, #{m.registrationDate})",
        "</foreach>",
        "</script>"
    })
    void bulkInsert(List<Members> list);

    /**
     * membersテーブルを全件削除するSQL。
     */
    @Delete("TRUNCATE TABLE member_registration")
    void truncate();
}