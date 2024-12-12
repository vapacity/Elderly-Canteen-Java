package com.javaee.elderlycanteen.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.apache.ibatis.annotations.*;
import com.javaee.elderlycanteen.entity.WeekMenu;
import org.apache.ibatis.annotations.Mapper;

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
    @Insert("INSERT INTO WeekMenu(week, stock, sales, disPrice, day) " +
            "VALUES(#{weekMenu.getWeek()}, #{weekMenu.getStock()}, #{weekMenu.getSales()}, #{weekMenu.getDisPrice()}, #{weekMenu.getDay()})")
    @Options(useGeneratedKeys = true, keyProperty = "dishId")
    Integer insert(WeekMenu weekMenu);

    // 更新菜单对象
    @Update("UPDATE WeekMenu SET " +
            "week = #{weekMenu.getWeek()}, " +
            "stock = #{weekMenu.getStock()}, " +
            "sales = #{weekMenu.getSales()}, " +
            "disPrice = #{weekMenu.getDisPrice()}, " +
            "day = #{weekMenu.getDay()} " +
            "WHERE dishId = #{weekMenu.getDishId()}")
    Integer update(WeekMenu weekMenu);

    // 根据 dishId 删除菜单对象
    @Delete("DELETE FROM WeekMenu WHERE dishId = #{dishId}")
    Integer deleteByDishId(Integer dishId);

}