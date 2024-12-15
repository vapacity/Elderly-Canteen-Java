package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.dto.admin.*;
import com.javaee.elderlycanteen.entity.TokenInfo;
import com.javaee.elderlycanteen.exception.NotFoundException;
import com.javaee.elderlycanteen.service.AdministratorService;
import com.javaee.elderlycanteen.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/administrators")
public class AdministratorController {

    private final AdministratorService administratorService;

    @Autowired
    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @GetMapping("/{accountId}")
    public AdminResponseDto getAdminById (Integer accountId){
        return administratorService.getAdminById(accountId);
    }

    @PutMapping("/{accountId}")
    public AdminResponseDto updateAdmin(Integer accountId, AdminInfoChangeDto request){
        return administratorService.updateAdmin(accountId, request);
    }

    @PostMapping("/add")
    public AdminResponseDto addAdmin(AdminRegisterDto admin) throws ParseException {
        return administratorService.addAdmin(admin);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAdmin(Integer accountId){
        return administratorService.deleteAdmin(accountId);
    }

    @GetMapping("/search")
    public AdminSearchDto searchAdmin(String name , String position){
        return administratorService.searchAdmin(name, position);
    }

    @GetMapping("/getAdminInfo")
    public AdminInfoGetDto getAdminInfo(@RequestHeader(name="Authorization", required = false) String token){
        // 获取accountId
        if( token == null ){
            throw new NotFoundException("token is null");
        }
        token = token.substring(7);
        TokenInfo tokenInfo = JWTUtils.getTokenInfo(token);
        Integer accountId = tokenInfo.getAccountId();
        return administratorService.getAdminInfo(accountId);
    }

}