package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.VolReview;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VolReviewDao {

    // 根据 applicationId 查询单个审核对象
    @Select("SELECT * FROM VolReview WHERE applicationId = #{applicationId}")
    VolReview selectByApplicationId(Integer applicationId);

    // 查询所有审核对象
    @Select("SELECT * FROM vol_review")
    List<VolReview> selectAll();

    // 插入审核对象
    @Insert("INSERT INTO VolReview(administratorId, status, reason, reviewDate) " +
            "VALUES(#{volReview.getAdministratorId()}, #{volReview.getStatus()}, #{volReview.getReason()}, #{volReview.getReviewDate()})")
    Integer insert(VolReview volReview);

    // 更新审核对象
    @Update("UPDATE VolReview SET " +
            "administratorId = #{volReview.getAdministratorId()}, " +
            "status = #{volReview.getStatus()}, " +
            "reason = #{volReview.getReason()}, " +
            "reviewDate = #{volReview.getReviewDate()} " +
            "WHERE applicationId = #{volReview.getApplicationId()}")
    Integer update(VolReview volReview);

    // 根据 applicationId 删除审核对象
    @Delete("DELETE FROM VolReview WHERE applicationId = #{applicationId}")
    Integer deleteByApplicationId(Integer applicationId);

}