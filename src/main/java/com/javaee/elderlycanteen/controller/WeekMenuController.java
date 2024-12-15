package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.dto.weekMenu.*;
import com.javaee.elderlycanteen.service.WeekMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/weekMenus")
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
    public ResponseEntity<AllWeekMenuResponseDto> getWeekMenu(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date){
        return ResponseEntity.ok(weekMenuService.getWeekMenu(date));
    }
    @PutMapping("/uploadDiscount")
    public ResponseEntity<DiscountResponseDto> uploadDiscount(DiscountRequestDto weekMenu){
        return ResponseEntity.ok(weekMenuService.uploadDiscount(weekMenu));
    }
    @PutMapping("batch-discount")
    public ResponseEntity<BatchResponseDto> batchDiscount(BatchRequestDto weekMenu){
        return ResponseEntity.ok(weekMenuService.batchDiscount(weekMenu));
    }

    @GetMapping("/getDiscount")
    public ResponseEntity<AllDiscountResponseDto> getAllDiscount(Date date) {
        return ResponseEntity.ok(weekMenuService.getAllDiscount(date));
    }
}