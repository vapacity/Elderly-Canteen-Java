package com.javaee.elderlycanteen.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 财务高级报表 DAO。
 *
 * SEC-LAB-A1：这里使用 JdbcTemplate 执行上层拼好的 SQL，
 * 用于演示 Spring JDBC 数据流与排序字段注入。
 */
@Repository
public class FinanceReportDao {

    private final JdbcTemplate jdbcTemplate;

    public FinanceReportDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> query(String sql) {
        return jdbcTemplate.queryForList(sql);
    }
}
