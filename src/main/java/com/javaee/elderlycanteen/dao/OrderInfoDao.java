package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.OrderInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderInfoDao {

    // 基于主码id查询
    @Select("SELECT * FROM order_info WHERE orderId = #{orderId}")
    OrderInfo getOrderInfoById(Integer orderId);

    // 查询所有数据
    @Select("SELECT * FROM order_info")
    List<OrderInfo> getAllOrderInfos();

    // 插入数据，主码id自增
    @Insert("INSERT INTO order_info (deliverOrDining, financeId, cartId, status, bonus, remark) " +
            "VALUES (#{deliverOrDining}, #{financeId}, #{cartId}, #{status}, #{bonus}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "orderId", keyColumn = "orderId")
    Integer insertOrderInfo(OrderInfo orderInfo);

    // 更新数据
    @Update("UPDATE order_info SET deliverOrDining = #{deliverOrDining}, financeId = #{financeId}, cartId = #{cartId}, " +
            "status = #{status}, bonus = #{bonus}, remark = #{remark} WHERE orderId = #{orderId}")
    Integer updateOrderInfo(OrderInfo orderInfo);

    // 删除数据
    @Delete("DELETE FROM order_info WHERE orderId = #{orderId}")
    Integer deleteOrderInfo(Integer orderId);
}
