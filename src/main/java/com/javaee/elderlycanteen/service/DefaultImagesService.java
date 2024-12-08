package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.DefaultImagesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultImagesService {

    private final DefaultImagesDao defaultImagesDao;

    @Autowired
    public DefaultImagesService(DefaultImagesDao defaultImagesDao) {
        this.defaultImagesDao = defaultImagesDao;
    }

    // 在这里添加特定的业务逻辑方法
}