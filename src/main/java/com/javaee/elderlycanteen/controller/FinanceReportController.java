package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.service.FinanceReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 财务高级报表接口。
 *
 * SEC-LAB-A1：sort/order 由请求参数进入 Service，最终进入 JdbcTemplate。
 */
@RestController
@RequestMapping("api/finances")
public class FinanceReportController {

    private final FinanceReportService financeReportService;

    public FinanceReportController(FinanceReportService financeReportService) {
        this.financeReportService = financeReportService;
    }

    @GetMapping("/report")
    public ResponseEntity<List<Map<String, Object>>> report(
            @RequestParam(value = "sort", defaultValue = "financeDate") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order) {
        return ResponseEntity.ok(financeReportService.report(sort, order));
    }
}
