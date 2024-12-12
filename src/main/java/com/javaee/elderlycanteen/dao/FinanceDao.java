package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Finance;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FinanceDao {

    // 查询基于主码id（financeId）的记录
    @Select("SELECT * FROM finance WHERE financeId = #{financeId}")
    Finance getFinanceById(int financeId);

    // 查询所有的记录
    @Select("SELECT * FROM finance")
    List<Finance> getAllFinances();

    // 插入一条记录，主码id自增
    @Insert("INSERT INTO finance(financeType, financeDate, price, inOrOut, accountId, administratorId, proof, status) " +
            "VALUES (#{financeType}, #{financeDate}, #{price}, #{inOrOut}, #{accountId}, #{administratorId}, #{proof}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "financeId")
    Integer insertFinance(Finance finance);

    // 更新记录
    @Update("UPDATE finance SET financeType = #{financeType}, financeDate = #{financeDate}, price = #{price}, " +
            "inOrOut = #{inOrOut}, accountId = #{accountId}, administratorId = #{administratorId}, proof = #{proof}, " +
            "status = #{status} WHERE financeId = #{financeId}")
    Integer updateFinance(Finance finance);

    // 删除记录
    @Delete("DELETE FROM finance WHERE financeId = #{financeId}")
    Integer deleteFinance(int financeId);
}
