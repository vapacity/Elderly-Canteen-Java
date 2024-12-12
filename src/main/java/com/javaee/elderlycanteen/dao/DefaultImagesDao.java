package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.DefaultImages;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface DefaultImagesDao {
    @Select("SELECT * FROM Images")
    List<DefaultImages> findAll(); //  查询所有图片

    @Select("SELECT * FROM Images WHERE id = #{id}")
    DefaultImages findById(@Param("id") Integer id); //  查询单个图片

    @Insert("INSERT INTO Images (id, imageUrl) VALUES (#{id}, #{imageUrl})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(DefaultImages images); //  插入图片

    @Update("UPDATE Images SET imageUrl = #{imageUrl} WHERE id = #{id}")
    Integer update(DefaultImages images); //  修改图片

    @Delete("DELETE FROM Images WHERE id = #{id}")
    Integer delete(@Param("id") Integer id); //  删除图片


}