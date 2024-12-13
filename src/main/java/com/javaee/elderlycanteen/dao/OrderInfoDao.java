package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.OrderInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Mapper
public interface OrderInfoDao {

    // 基于主码id查询
    @Select("SELECT * FROM order_info WHERE orderId = #{orderId}")
    OrderInfo getOrderInfoById(Integer orderId);

    // 查询所有数据
    @Select("SELECT * FROM OrderInfo")
    List<OrderInfo> getAllOrderInfos();

    // 插入数据，主码id自增
    @Insert("INSERT INTO OrderInfo (deliverOrDining, financeId, cartId, status, bonus, remark) " +
            "VALUES (#{deliverOrDining}, #{financeId}, #{cartId}, #{status}, #{bonus}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "orderId", keyColumn = "orderId")
    Integer insertOrderInfo(OrderInfo orderInfo);

    // 更新数据
    @Update("UPDATE OrderInfo SET deliverOrDining = #{deliverOrDining}, financeId = #{financeId}, cartId = #{cartId}, " +
            "status = #{status}, bonus = #{bonus}, remark = #{remark} WHERE orderId = #{orderId}")
    Integer updateOrderInfo(OrderInfo orderInfo);

    // 删除数据
    @Delete("DELETE FROM OrderInfo WHERE orderId = #{orderId}")
    Integer deleteOrderInfo(Integer orderId);

    //根据cartId查找OrderInfo
    @Select("SELECT * FROM OrderInfo WHERE cartId = #{cartId}")
    List<OrderInfo> getOrderInfoByCartId(Integer cartId);
}
