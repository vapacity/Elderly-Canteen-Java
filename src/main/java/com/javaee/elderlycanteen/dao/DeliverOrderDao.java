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

    @Select("SELECT * FROM DeliverOrder WHERE orderId = #{orderId}")
    DeliverOrder getDeliverOrderById(@Param("orderId") String orderId);

    @Insert("INSERT INTO DeliverOrder(orderId, orderDate, deliverAddress, totalPrice, orderStatus, deliveryTime) VALUES (#{orderId}, #{orderDate}, #{deliverAddress}, #{totalPrice}, #{orderStatus}, #{deliveryTime})")
    @Options(useGeneratedKeys = true, keyProperty = "orderId")
    Integer insertDeliverOrder(DeliverOrder deliverOrder);

    @Update("UPDATE DeliverOrder SET deliverAddress = #{deliverAddress}, totalPrice = #{totalPrice}, orderStatus = #{orderStatus}, deliveryTime = #{deliveryTime} WHERE orderId = #{orderId}")
    Integer updateDeliverOrder(DeliverOrder deliverOrder);

    @Delete("DELETE FROM DeliverOrder WHERE orderId = #{orderId}")
    Integer deleteDeliverOrder(@Param("orderId") String orderId);

    // Add your own methods here.

}