package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface CartItemDao {
    @Select("SELECT * FROM CartItem")
    List<CartItem> getAllCartItems();

    @Select("SELECT * FROM CartItem WHERE id = #{id}")
    CartItem getCartItemById(@Param("id") int id);

    @Insert("INSERT INTO CartItem (itemId, userId, quantity) VALUES (#{itemId}, #{userId}, #{quantity})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer addCartItem(@Param("itemId") int itemId, @Param("userId") String userId, @Param("quantity") int quantity);

    @Update("UPDATE CartItem SET quantity = #{quantity} WHERE id = #{id}")
    Integer updateCartItemQuantity(@Param("id") int id, @Param("quantity") int quantity);

    @Delete("DELETE FROM CartItem WHERE id = #{id}")
    Integer deleteCartItem(@Param("id") int id);



}