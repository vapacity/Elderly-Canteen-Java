package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.DonationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DonationService {

    private final DonationDao donationDao;

    @Autowired
    public DonationService(DonationDao donationDao) {
        this.donationDao = donationDao;
    }

    // 在这里添加特定的业务逻辑方法
}