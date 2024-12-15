package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.DeliverOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface DeliverOrderDao {
    @Select("SELECT * FROM DeliverOrder")
    List<DeliverOrder> getAllDeliverOrder();

    @Insert("INSERT INTO DeliverOrder(orderId, deliverPhone, customerPhone, cusAddress, deliverStatus, cartId) VALUES (#{orderId}, #{deliverPhone}, #{customerPhone}, #{cusAddress}, #{deliverStatus}, #{cartId})")
    @Options(useGeneratedKeys = true, keyProperty = "orderId")
    Integer insertDeliverOrder(DeliverOrder deliverOrder);

    @Update("UPDATE DeliverOrder SET deliverAddress = #{deliverAddress}, totalPrice = #{totalPrice}, orderStatus = #{orderStatus}, deliveryTime = #{deliveryTime} WHERE orderId = #{orderId}")
    Integer updateDeliverOrder(DeliverOrder deliverOrder);

    @Delete("DELETE FROM DeliverOrder WHERE orderId = #{orderId}")
    Integer deleteDeliverOrder(@Param("orderId") String orderId);

    @Select("SELECT * FROM DeliverOrder WHERE orderId = #{orderId}")
    DeliverOrder getDeliverOrderByOrderId(@Param("orderId") Integer orderId);

    @Update("UPDATE DeliverOrder SET deliverStatus = #{deliverStatus} WHERE orderId = #{orderId}")
    Integer updateDeliverDeliverStatus(@Param("orderId")Integer orderId, @Param("deliverStatus") String deliverStatus);

    @Select("SELECT * FROM DeliverOrder WHERE deliverStatus=#{deliverStatus}")
    List<DeliverOrder> getDeliverOrderByDeliverStatus(@Param("deliverStatus") String deliverStatus);

    @Update("UPDATE DeliverOrder SET deliverPhone = #{deliverPhone} WHERE orderId = #{orderId}")
    Integer updateDeliverDeliverPhone(@Param("orderId")Integer orderId, @Param("deliverPhone") String deliverPhone);
}