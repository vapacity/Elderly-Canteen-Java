package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.OrderReview;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderReviewDao {

    // 基于主码id查询
    @Select("SELECT * FROM order_review WHERE orderId = #{orderId}")
    OrderReview getOrderReviewById(Integer orderId);

    // 查询所有数据
    @Select("SELECT * FROM order_review")
    List<OrderReview> getAllOrderReviews();

    // 插入数据，主码id自增
    @Insert("INSERT INTO order_review (orderId, cStars, cReviewText) " +
            "VALUES (#{orderId}, #{cStars}, #{cReviewText})")
    @Options(useGeneratedKeys = true, keyProperty = "orderId", keyColumn = "orderId")
    Integer insertOrderReview(OrderReview orderReview);

    // 更新数据
    @Update("UPDATE order_review SET cStars = #{cStars}, cReviewText = #{cReviewText} WHERE orderId = #{orderId}")
    Integer updateOrderReview(OrderReview orderReview);

    // 删除数据
    @Delete("DELETE FROM order_review WHERE orderId = #{orderId}")
    Integer deleteOrderReview(Integer orderId);
}
