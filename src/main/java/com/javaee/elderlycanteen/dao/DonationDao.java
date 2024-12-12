package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Donation;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface DonationDao {
    @Select("SELECT * FROM donation WHERE id = #{donationId}")
    public Donation getDonationById(@Param("donationId") int donationId);

    @Select("Select * from donation")
    public List<Donation> getDonations();

    @Insert("Insert into donation(accountId,financeId,origin) values(#{accountId},#{financeId},#{origin})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    public Integer insertDonation(Donation donation);

    @Update("Update donation set accountId = #{accountId}, set financeId = #{financeId}, origin = #{origin} where id = #{id}")
    public Integer updateDonation(Donation donation);

    @Delete("Delete from donation where id = #{donationId}")
    public Integer deleteDonationById(@Param("donationId") int donationId);

}