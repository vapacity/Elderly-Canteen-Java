package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.PayWage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PayWageDao {

    // 基于主码id查询
    @Select("SELECT * FROM pay_wage WHERE financeId = #{financeId}")
    PayWage getPayWageById(Integer financeId);

    // 查询所有数据
    @Select("SELECT * FROM pay_wage")
    List<PayWage> getAllPayWages();

    // 插入数据，主码id自增
    @Insert("INSERT INTO pay_wage (financeId, employeeId, administratorId) " +
            "VALUES (#{financeId}, #{employeeId}, #{administratorId})")
    @Options(useGeneratedKeys = true, keyProperty = "financeId", keyColumn = "financeId")
    Integer insertPayWage(PayWage payWage);

    // 更新数据
    @Update("UPDATE pay_wage SET employeeId = #{employeeId}, administratorId = #{administratorId} " +
            "WHERE financeId = #{financeId}")
    Integer updatePayWage(PayWage payWage);

    // 删除数据
    @Delete("DELETE FROM pay_wage WHERE financeId = #{financeId}")
    Integer deletePayWage(Integer financeId);
}
