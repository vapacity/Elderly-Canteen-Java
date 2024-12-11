package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.AdministratorDao;
import com.javaee.elderlycanteen.entity.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministratorService {

    private final AdministratorDao administratorDao;

    @Autowired
    public AdministratorService(AdministratorDao administratorDao) {
        this.administratorDao = administratorDao;
    }

    public Administrator getAdminById(String accountId) {
        Administrator administrator = administratorDao.GetAdminById(accountId);
        return administrator;
    }

    public List<Administrator> searchAdmin(String email, String position) {
        return administratorDao.SearchAdmin(email, position);
    }

    public Integer updateAdmin(String accountId, String email, String position) {
        return administratorDao.UpdateAdmin(accountId, email, position);
    }

    public Integer addAdmin(String accountId, String email, String position) {
        return administratorDao.AddAdmin(accountId, email, position);
    }

    public Integer deleteAdmin(String accountId) {
        return administratorDao.DeleteAdmin(accountId);
    }
}