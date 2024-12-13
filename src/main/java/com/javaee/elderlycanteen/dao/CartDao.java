package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Cart;
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
public interface CartDao {
    @Select("SELECT * FROM Cart")
    List<Cart> findAll();

    @Insert("INSERT INTO Cart (accountId, createdTime, updatedTime) VALUES (#{accountId}, #{createdTime}, #{updatedTime})")
    @Options(useGeneratedKeys = true, keyProperty = "cartId")
    Integer insert(Cart cart);

    @Delete("DELETE FROM Cart WHERE cartId = #{cartId}")
    Integer deleteById(@Param("cartId") Integer cartId);

    // 检查购物车是否存在
    @Select("SELECT * FROM Cart WHERE cartId = #{cartId}")
    Cart checkCartExists(@Param("cartId") Integer cartId);

    //验证cartId是否存在且属于accountId
    @Select("SELECT * FROM Cart WHERE cartId = #{cartId} AND accountId = #{accountId}")
    Cart getCartByCartIdAndAccountId(@Param("cartId") Integer cartId, @Param("accountId") Integer accountId);

    // 更新购物车更新时间
    @Update("UPDATE Cart SET updatedTime = #{updatedTime} WHERE cartId = #{cartId}")
    Integer updateCartUpdatedTime(@Param("cartId") Integer cartId, @Param("updatedTime") Date updatedTime);

}