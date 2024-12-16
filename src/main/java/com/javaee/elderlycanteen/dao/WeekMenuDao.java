package com.javaee.elderlycanteen.dao;

import org.apache.ibatis.annotations.*;
import com.javaee.elderlycanteen.entity.WeekMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface WeekMenuDao {

    // 根据 dishId 查询单个菜单对象
    @Select("SELECT * FROM WeekMenu WHERE dishId = #{dishId}")
    WeekMenu selectByDishId(Integer dishId);

    // 查询所有菜单对象
    @Select("SELECT * FROM WeekMenu")
    List<WeekMenu> selectAll();

    // 插入菜单对象
    @Insert("INSERT INTO WeekMenu(dishId,week, stock, sales, disPrice, day) " +
            "VALUES(#{dishId}, #{week}, #{stock}, #{sales}, #{disPrice}, #{day})")


    Integer insert(WeekMenu weekMenu);

    // 更新菜单对象
    @Update("UPDATE WeekMenu SET " +
            "stock = #{stock}, " +
            "sales = #{sales}, " +
            "disPrice = #{disPrice}, " +
            "day = #{day} " +
            "WHERE dishId = #{dishId} and week = #{week}")
    Integer update(WeekMenu weekMenu);

    // 根据 dishId 删除菜单对象
    @Delete("DELETE FROM WeekMenu WHERE dishId = #{dishId}")
    Integer deleteByDishId(Integer dishId);

    // 检查菜品是否在今日菜单中
    @Select("SELECT * FROM WeekMenu WHERE week = #{week} AND dishId = #{dishId}")
    WeekMenu findWeekMenuByWeekAndDishId(@Param("week") Date week, @Param("dishId") Integer dishId);
    @Delete("DELETE FROM WeekMenu WHERE week = #{week} AND dishId = #{dishId}")
    void deleteWeekMenuByWeekAndDishId(@Param("week") Date week, @Param("dishId") Integer dishId);
    // 检查库存是否足够
    @Select("SELECT stock FROM WeekMenu WHERE dishId = #{dishId} AND week = #{week}")
    Integer findStockByDishIdAndWeek(@Param("dishId") Integer dishId, @Param("week") Date week);

    @Select("SELECT * FROM WeekMenu WHERE week = #{week} AND day = #{day}")
    List<WeekMenu> findWeekMenuByWeek(@Param("week") Date week,@Param("day") String day);

    @Update("UPDATE WeekMenu SET stock=#{stock} WHERE dishId = #{dishId} AND week = #{week}")
    Integer updateWeekMenuStock(@Param("stock") Integer stock, @Param("dishId") Integer dishId, @Param("week") Date week);

    @Update("UPDATE WeekMenu SET sales=#{sales} WHERE dishId = #{dishId} AND week = #{week}")
    Integer updateWeekMenuSales(@Param("sales") Integer sales, @Param("dishId") Integer dishId, @Param("week") Date week);

    @Select("SELECT * FROM WeekMenu WHERE dishId = #{dishId} AND week = #{week} AND day = #{day}")
    WeekMenu findWeekMenuByDishIdAndWeekAndDay(@Param("dishId") Integer dishId, @Param("week") Date week, @Param("day") String day);

    @Delete("Delete from WeekMenu WHERE week = #{date} AND dishId = #{requestDishId}")
    void deleteByWeekAndDishId(Date date, Integer requestDishId);
}