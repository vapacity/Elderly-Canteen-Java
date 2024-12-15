package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.dto.finance.FinanceResponseDto;
import com.javaee.elderlycanteen.dto.finance.FinanceTotalsResponseDto;
import com.javaee.elderlycanteen.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/finances")
public class FinanceController {

    private final FinanceService financeService;

    @Autowired
    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @GetMapping("/financial-records")
    public ResponseEntity<FinanceResponseDto> getFinances(
            @RequestParam(required = false) String financeType,
            @RequestParam(required = false) String inOrOut,
            @RequestParam(required = false) String financeDate,
            @RequestParam(required = false) String financeId,
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) String status) {

        // 调用 FinanceService 获取过滤后的财务信息
        FinanceResponseDto result = financeService.getAllFinanceInfo(financeType, inOrOut, financeDate, financeId, accountId, status);
        return ResponseEntity.ok(result);
    }
    @PostMapping("financial-records/{id}/status")
    public ResponseEntity<FinanceResponseDto> auditFinance(@PathVariable Integer id,String status) {
        return ResponseEntity.ok(financeService.auditFinance(id,status));
    }

    @GetMapping("/getTotal")
    public ResponseEntity<FinanceTotalsResponseDto> getTotal() {
        return ResponseEntity.ok(financeService.getTotal());
    }
}