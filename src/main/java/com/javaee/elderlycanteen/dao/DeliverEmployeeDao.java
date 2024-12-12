package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.DeliverEmployee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface DeliverEmployeeDao {
    @Select("SELECT * FROM DeliverEmployee")
    List<DeliverEmployee> getAll();

    @Select("SELECT * FROM DeliverEmployee WHERE id = #{id}")
    DeliverEmployee getById(@Param("id") int id);

    @Insert("INSERT INTO DeliverEmployee (name, phone, address, employeeId) VALUES (#{name}, #{phone}, #{address}, #{employeeId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(DeliverEmployee deliverEmployee);

    @Update("UPDATE DeliverEmployee SET name = #{name}, phone = #{phone}, address = #{address}, employeeId = #{employeeId} WHERE id = #{id}")
    Integer update(DeliverEmployee deliverEmployee);

    @Delete("DELETE FROM DeliverEmployee WHERE id = #{id}")
    Integer deleteById(@Param("id") int id);


}