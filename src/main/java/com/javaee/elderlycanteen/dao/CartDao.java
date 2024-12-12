package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface CartDao {
    @Select("SELECT * FROM Cart")
    List<Cart> findAll();

    @Select("SELECT * FROM Cart WHERE cartId = #{cartId}")
    Cart getById(@Param("cartId") Long cartId);

    @Insert("INSERT INTO Cart (cartId, userId, foodId, quantity) VALUES (#{cartId}, #{userId}, #{foodId}, #{quantity})")
    @Options(useGeneratedKeys = true, keyProperty = "cartId")
    Integer insert(Cart cart);

    @Update("UPDATE Cart SET userId = #{userId}, foodId = #{foodId}, quantity = #{quantity} WHERE cartId = #{cartId}")
    Integer update(Cart cart);

    @Delete("DELETE FROM Cart WHERE cartId = #{cartId}")
    Integer deleteById(@Param("cartId") Long cartId);


}