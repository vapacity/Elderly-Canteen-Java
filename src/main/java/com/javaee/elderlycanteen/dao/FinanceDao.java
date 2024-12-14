package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Finance;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Mapper
public interface FinanceDao {

    // 查询基于主码id（financeId）的记录
    @Select("SELECT * FROM Finance WHERE financeId = #{financeId}")
    Finance getFinanceById(int financeId);

    // 查询所有的记录
    @Select("SELECT * FROM Finance")
    List<Finance> getAllFinances();

    // 插入一条记录，主码id自增
    @Insert("INSERT INTO Finance(financeType, financeDate, price, inOrOut, accountId, administratorId, proof, status) " +
            "VALUES (#{financeType}, #{financeDate}, #{price}, #{inOrOut}, #{accountId}, #{administratorId}, #{proof}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "financeId")
    Integer insertFinance(@RequestBody Finance finance);

    // 更新记录
    @Update("UPDATE Finance SET financeType = #{financeType}, financeDate = #{financeDate}, price = #{price}, " +
            "inOrOut = #{inOrOut}, accountId = #{accountId}, administratorId = #{administratorId}, proof = #{proof}, " +
            "status = #{status} WHERE financeId = #{financeId}")
    Integer updateFinance(Finance finance);

    // 删除记录
    @Delete("DELETE FROM Finance WHERE financeId = #{financeId}")
    Integer deleteFinance(int financeId);

    @Select("SELECT * FROM Finance WHERE accountId = #{accountId} AND financeType = #{financeType}")
    List<Finance> getFinanceByAccountIdAndFinanceType(Integer accountId, String financeType);


    //查询是否存在管理员
    @Select("SELECT COUNT(*) FROM Finance WHERE administratorId = #{administratorId}")
    boolean existsByAdministratorId(Integer administratorId);


    @Select("SELECT * FROM Finance ORDER BY financeId DESC LIMIT 1")
    Finance getLatestFinance();

    List<Finance> getAllFinanceInfo(@Param("financeType") String financeType,
                                    @Param("inOrOut") String inOrOut,
                                    @Param("financeDate") String financeDate,
                                    @Param("financeId") String financeId,
                                    @Param("accountId") String accountId,
                                    @Param("status") String status);

}
