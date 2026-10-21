package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.FinanceReportDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 财务高级报表服务。
 *
 * SEC-LAB-A1：排序字段先做“前缀白名单”，再拼接到 ORDER BY。
 * 前缀校验并不能阻止 "financeDate,(SELECT ...)" 这类注入。
 */
@Service
public class FinanceReportService {

    private final FinanceReportDao financeReportDao;

    public FinanceReportService(FinanceReportDao financeReportDao) {
        this.financeReportDao = financeReportDao;
    }

    public List<Map<String, Object>> report(String sort, String order) {
        String safeSort = normalizeSort(sort);
        String safeOrder = "asc".equalsIgnoreCase(order) ? "asc" : "desc";
        String sql = "SELECT financeId, financeType, financeDate, price, inOrOut, accountId, administratorId, status "
                + "FROM Finance ORDER BY " + safeSort + " " + safeOrder;
        return financeReportDao.query(sql);
    }

    private String normalizeSort(String sort) {
        if (sort == null) {
            return "financeDate";
        }
        String candidate = sort.trim();
        if (candidate.startsWith("financeId")
                || candidate.startsWith("financeDate")
                || candidate.startsWith("price")
                || candidate.startsWith("status")) {
            return candidate;
        }
        return "financeDate";
    }
}
