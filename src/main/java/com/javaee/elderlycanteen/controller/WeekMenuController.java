package com.javaee.elderlycanteen.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.javaee.elderlycanteen.config.DateDeserializer;
import com.javaee.elderlycanteen.config.DateSerializer;

import com.javaee.elderlycanteen.dto.weekMenu.*;
import com.javaee.elderlycanteen.service.WeekMenuService;
import org.checkerframework.common.util.report.qual.ReportUnqualified;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;

@RestController
@RequestMapping("api/menu")
public class WeekMenuController {

    private final WeekMenuService weekMenuService;

    @Autowired
    public WeekMenuController(WeekMenuService weekMenuService) {
        this.weekMenuService = weekMenuService;
    }

    @PostMapping("/add")
    public ResponseEntity<WMResponseDto> addWeekMenu(@RequestBody WMRequestDto weekMenu) throws ParseException {
        return ResponseEntity.ok(weekMenuService.addWeekMenu(weekMenu));
    }
    @DeleteMapping("/remove")
    public ResponseEntity<WMResponseDto> removeWeekMenu(@RequestBody WMRequestDto weekMenu) {
        return ResponseEntity.ok(weekMenuService.removeWeekMenu(weekMenu));
    }
    @GetMapping("")
    public ResponseEntity<AllWeekMenuResponseDto> getWeekMenu(@RequestParam("date")
                                                                  @DateTimeFormat(pattern="yyyy-MM-dd") Date date){
        return ResponseEntity.ok(weekMenuService.getWeekMenu(date));
    }
    @PutMapping("/uploadDiscount")
    public ResponseEntity<DiscountResponseDto> uploadDiscount(@RequestBody DiscountRequestDto weekMenu){
        return ResponseEntity.ok(weekMenuService.uploadDiscount(weekMenu));
    }
    @PutMapping("batch-discount")
    public ResponseEntity<BatchResponseDto> batchDiscount(@RequestBody BatchRequestDto weekMenu){
        return ResponseEntity.ok(weekMenuService.batchDiscount(weekMenu));
    }

    @GetMapping("/getDiscount")
    public ResponseEntity<AllDiscountResponseDto> getAllDiscount(@RequestParam("date")
                                                                     @DateTimeFormat(pattern="yyyy-MM-dd")  Date date) {
        return ResponseEntity.ok(weekMenuService.getAllDiscount(date));
    }

}