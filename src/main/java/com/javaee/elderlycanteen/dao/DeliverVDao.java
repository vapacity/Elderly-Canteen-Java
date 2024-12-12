package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.DeliverV;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface DeliverVDao {
    @Select("SELECT * FROM Deliver")
    List<DeliverV> getAllDeliverV();

    @Select("SELECT * FROM Deliver WHERE deliverId = #{deliverId}")
    DeliverV getDeliverVById(@Param("deliverId") Integer deliverId);

    @Insert("INSERT INTO Deliver (deliverId, deliverAddress, deliverTime, deliverStatus, deliverFee, deliverTotal, deliverUserId) VALUES (#{deliverId}, #{deliverAddress}, #{deliverTime}, #{deliverStatus}, #{deliverFee}, #{deliverTotal}, #{deliverUserId})")
    @Options(useGeneratedKeys = true, keyProperty = "deliverId")
    Integer insertDeliverV(DeliverV deliverV);

    @Update("UPDATE Deliver SET deliverAddress = #{deliverAddress}, deliverTime = #{deliverTime}, deliverStatus = #{deliverStatus}, deliverFee = #{deliverFee}, deliverTotal = #{deliverTotal}, deliverUserId = #{deliverUserId} WHERE deliverId = #{deliverId}")
    Integer updateDeliverV(DeliverV deliverV);

    @Delete("DELETE FROM Deliver WHERE deliverId = #{deliverId}")
    Integer deleteDeliverV(@Param("deliverId") Integer deliverId);

    // 以下是示例代码，请根据需要自行编写
}