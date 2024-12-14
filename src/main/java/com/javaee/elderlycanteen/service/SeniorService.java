package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.SeniorDao;
import com.javaee.elderlycanteen.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeniorService {

    private final SeniorDao seniorDao;

    @Autowired
    public SeniorService(SeniorDao seniorDao) {
        this.seniorDao = seniorDao;
    }

    public Integer updateSeniorSubsidy(Integer accountId,Double subsidy){
        Integer ret = updateSeniorSubsidy(accountId,subsidy);
        if (ret!=1){
            throw new ServiceException("update senior subsidy failed!");
        }
        return ret;
    }
}