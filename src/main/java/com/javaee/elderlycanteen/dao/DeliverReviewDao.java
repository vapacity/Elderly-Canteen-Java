package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.DeliverReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface DeliverReviewDao {
    @Select("SELECT * FROM DeliverReview")
    List<DeliverReview> getAllDeliverReviews();

    @Select("SELECT * FROM DeliverReview WHERE deliverOrderId = #{deliverOrderId}")
    DeliverReview getDeliverReviewByDeliverOrderId(@Param("deliverOrderId") String deliverOrderId);

    @Insert("INSERT INTO DeliverReview (deliverOrderId, reviewContent, reviewScore) VALUES (#{deliverOrderId}, #{reviewContent}, #{reviewScore})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insertDeliverReview(DeliverReview deliverReview);

    @Update("UPDATE DeliverReview SET reviewContent = #{reviewContent}, reviewScore = #{reviewScore} WHERE deliverOrderId = #{deliverOrderId}")
    Integer updateDeliverReview(DeliverReview deliverReview);

    @Delete("DELETE FROM DeliverReview WHERE deliverOrderId = #{deliverOrderId}")
    Integer deleteDeliverReview(@Param("deliverOrderId") String deliverOrderId);


}