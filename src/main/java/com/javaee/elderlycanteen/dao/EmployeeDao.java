package com.javaee.elderlycanteen.dao;

import com.javaee.elderlycanteen.entity.Employee;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface EmployeeDao {

    // 根据主键查询单个员工
    @Select("SELECT * FROM employee WHERE employeeId = #{employeeId}")
    Employee getEmployeeById(@Param("employeeId") Integer employeeId);

    // 查询所有员工
    @Select("SELECT * FROM Employee")
    List<Employee> getAllEmployees();

    // 插入一个新的员工
    @Insert("INSERT INTO employee (employee_name, phone_num, address, employee_position, salary, id_card, is_paid_this_month) " +
            "VALUES (#{employeeName}, #{phoneNum}, #{address}, #{employeePosition}, #{salary}, #{idCard}, #{isPaidThisMonth})")
    @Options(useGeneratedKeys = true, keyProperty = "employeeId")
    Integer insertEmployee(Employee employee);

    // 更新员工信息
    @Update("UPDATE employee SET employee_name = #{employeeName}, phone_num = #{phoneNum}, address = #{address}, " +
            "employee_position = #{employeePosition}, salary = #{salary}, id_card = #{idCard}, is_paid_this_month = #{isPaidThisMonth} " +
            "WHERE employee_id = #{employeeId}")
    Integer updateEmployee(Employee employee);

    // 删除员工
    @Delete("DELETE FROM employee WHERE employee_id = #{employeeId}")
    Integer deleteEmployee(@Param("employeeId") int employeeId);
}
