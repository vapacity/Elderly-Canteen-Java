package com.javaee.elderlycanteen.controller;

import com.javaee.elderlycanteen.dto.repository.*;
import com.javaee.elderlycanteen.entity.TokenInfo;
import com.javaee.elderlycanteen.exception.InvalidInputException;
import com.javaee.elderlycanteen.service.RepositoryService;
import com.javaee.elderlycanteen.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/repo")
public class RepositoryController {

    private final RepositoryService repositoryService;

    @Autowired
    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    // 在这里添加特定的API端点
    @GetMapping("/search")
    public AllRepoResponseDto getAllIngredients(@RequestParam(value = "name", required = false) String name) {
        return repositoryService.searchAllRepos(name);
    }

    @PutMapping("/update")
    public RepoResponseDto updateRepo(RepoRequestDto dto){
        return repositoryService.updateRepo(dto);
    }

    @DeleteMapping("/delete")
    public RepoResponseDto deleteRepo(Integer ingreId, Date expiry){
        return repositoryService.deleteRepo(ingreId, expiry);
    }

    // 进货
    @PostMapping("/restock")
    public RestockResponseDto addRepo(@RequestHeader(name = "Authorization", required = false) String token, @RequestBody RestockRequestDto dto) throws ParseException {
        token = token.replace("Bearer ", "");
        TokenInfo tokenInfo = JWTUtils.getTokenInfo(token);
        if(tokenInfo == null){
            throw new InvalidInputException("Invalid token");
        }
        Integer accountId = tokenInfo.getAccountId();

        return repositoryService.restock(dto,accountId);
    }

    @GetMapping("/search/restock")
    public AllRestockResponseDto getRepo(){
        return repositoryService.getAllRestocks();
    }

    @PostMapping("/setStock")
    public ResponseEntity<List<ReduceResponseDto>> setStock(@RequestParam(value = "date")
                                         @DateTimeFormat(pattern = "yyyy-MM-dd")
                                         Date date,
                                                            @RequestParam(value = "stock")
                                     Integer stock) throws ParseException {

        return ResponseEntity.ok(repositoryService.replenishStock(date, stock));
    }

}