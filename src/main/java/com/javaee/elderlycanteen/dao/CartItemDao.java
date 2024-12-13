package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Cart;
import com.javaee.elderlycanteen.entity.CartItem;
import com.javaee.elderlycanteen.entity.WeekMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;

import java.util.Date;
import java.util.List;

@Mapper
public interface CartItemDao {
    @Select("SELECT * FROM CartItem")
    List<CartItem> getAllCartItems();

    @Select("SELECT * FROM CartItem WHERE cartId = #{cartId}")
    List<CartItem> getCartItemByCartId(@Param("cartId") Integer cartId);

    @Insert("INSERT INTO CartItem (cartId, dishId, week, quantity) VALUES (#{cartId}, #{dishId}, #{week},#{quantity})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer addCartItem(CartItem cartItem);

    @Update("UPDATE CartItem SET quantity = #{quantity} WHERE cartId = #{cartId} AND dishId=#{dishId} AND week = #{week}")
    Integer updateCartItemQuantity(@Param("cartId") Integer cartId, @Param("dishId") Integer dishId,@Param("week") Date week,@Param("quantity") Integer quantity);

    @Select("SELECT * FROM CartItem WHERE cartId = #{cartId} AND dishId = #{dishId}")
    CartItem getCartItemByCartIdAndDishId(@Param("cartId") Integer cartId,@Param("dishId") Integer dishId);

    @Select("SELECT * FROM CartItem WHERE cartId = #{cartId} AND dishId = #{dishId} AND week = #{week}")
    CartItem getCartItemByPrimaryKey(@Param("cartId") Integer cartId,@Param("dishId") Integer dishId,@Param("week") Date week);

    @Delete("DELETE FROM CartItem WHERE cartId = #{cartId} AND dishId = #{dishId} AND week = #{week}")
    Integer deleteCartItemByPrimaryKey(@Param("cartId")Integer cartId, @Param("dishId")Integer dishId, @Param("week") Date week);
}