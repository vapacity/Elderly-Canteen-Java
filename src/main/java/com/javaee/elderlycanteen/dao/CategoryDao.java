package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface CategoryDao {
    @Select("SELECT * FROM category")
    List<Category> findAll();

    //根据id查询单个类目
    @Select("SELECT * FROM category WHERE id = #{id}")
    Category findById(Integer id);

    //插入类目
    @Insert("INSERT INTO category (name, description) VALUES (#{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(Category category);

    //更新类目
    @Update("UPDATE category SET name = #{name}, description = #{description} WHERE id = #{id}")
    Integer update(Category category);

    //删除类目
    @Delete("DELETE FROM category WHERE id = #{id}")
    Integer delete(Integer id);

    //查询所有类目并按id降序排列
    @Select("SELECT * FROM category ORDER BY id DESC")
    List<Category> findAllByOrderByIdDesc();

    //查询所有类目并按id升序排列
    @Select("SELECT * FROM category ORDER BY id ASC")
    List<Category> findAllByOrderIdAsc();

    //查询所有类目并按name升序排列
    @Select("SELECT * FROM category ORDER BY name ASC")
    List<Category> findAllByNameAsc();

    //查询所有类目并按name降序排列
    @Select("SELECT * FROM category ORDER BY name DESC")
    List<Category> findAllByNameDesc();

    //查询类目数量
    @Select("SELECT COUNT(*) FROM category")
    Integer count();

    //根据类目名称查询类目
    @Select("SELECT * FROM category WHERE name = #{name}")
    Category findByName(String name);

    //根据类目id查询类目
    @Select("SELECT * FROM category WHERE id IN (#{ids})")
    List<Category> findByIds(@Param("ids") List<Integer> ids);

    //根据类目id查询类目并按id降序排列
    @Select("SELECT * FROM category WHERE id IN (#{ids}) ORDER BY id DESC")
    List<Category> findByIdsAndOrderByIdDesc(@Param("ids") List<Integer> ids);

    //根据类目id查询类目并按id升序排列
    @Select("SELECT * FROM category WHERE id IN (#{ids}) ORDER BY id ASC")
    List<Category> findByIdsAndOrderByOrderIdAsc(@Param("ids") List<Integer> ids);



}