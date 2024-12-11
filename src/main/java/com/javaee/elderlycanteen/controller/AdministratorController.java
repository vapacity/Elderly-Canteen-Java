package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.entity.Administrator;
import com.javaee.elderlycanteen.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administrators")
public class AdministratorController {

    private final AdministratorService administratorService;

    @Autowired
    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @GetMapping("/search/{accountId}")
    public ResponseEntity<Administrator> getAdminById(@PathVariable String accountId) {
        Administrator admin = administratorService.getAdminById(accountId);
        return admin != null ? ResponseEntity.ok(admin) : ResponseEntity.ok().build();
    }

    @GetMapping("/search/{email}/{position}")
    public ResponseEntity<List<Administrator>> searchAdmin(@PathVariable(required = false) String email,
                                                           @PathVariable(required = false) String position) {
        List<Administrator> admins = administratorService.searchAdmin(email, position);
        return ResponseEntity.ok(admins);
    }

    @PutMapping("/update/{accountId}/{email}/{position}")
    public ResponseEntity<Integer> updateAdmin(@PathVariable String accountId,
                                               @PathVariable String email,
                                               @PathVariable String position) {
        Integer result = administratorService.updateAdmin(accountId, email, position);
        return result > 0 ? ResponseEntity.ok(result) : ResponseEntity.badRequest().build();
    }

    @PostMapping("/add/{accountId}/{email}/{position}")
    public ResponseEntity<Integer> addAdmin(@PathVariable String accountId,
                                            @PathVariable String email,
                                            @PathVariable String position) {
        Integer result = administratorService.addAdmin(accountId, email, position);
        return result > 0 ? ResponseEntity.ok(result) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/delete/{accountId}")
    public ResponseEntity<Integer> deleteAdmin(@PathVariable String accountId) {
        Integer result = administratorService.deleteAdmin(accountId);
        return result > 0 ? ResponseEntity.ok(result) : ResponseEntity.badRequest().build();
    }
}