package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.CategoryDao;
import com.javaee.elderlycanteen.dto.category.AllCateResponseDto;
import com.javaee.elderlycanteen.dto.category.CateDto;
import com.javaee.elderlycanteen.dto.category.CateRequestDto;
import com.javaee.elderlycanteen.dto.category.CateResponseDto;
import com.javaee.elderlycanteen.entity.Category;
import com.javaee.elderlycanteen.exception.NotFoundException;
import com.javaee.elderlycanteen.exception.ServiceException;
import com.sun.net.httpserver.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryDao categoryDao;

    @Autowired
    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public CateResponseDto addCategory(CateRequestDto dto) {
        Category existedCate = categoryDao.findByName(dto.getCateName());
        if (existedCate != null) {
            throw new ServiceException("Category already exists");
        }
        Category cate = new Category(dto.getCateId(), dto.getCateName());
        categoryDao.insert(cate);
        Integer cateId = categoryDao.findByName(dto.getCateName()).getCateId();
        CateDto cateDto = new CateDto();
        cateDto.setCateId(cateId);
        cateDto.setCateName(cate.getCateName());

        CateResponseDto cateResponseDto = new CateResponseDto();
        cateResponseDto.setCates(cateDto);

        return cateResponseDto;
    }

    public AllCateResponseDto searchCategories(String name) {
        AllCateResponseDto cateResponseDto = new AllCateResponseDto();
        List<Category> categories = new ArrayList<Category>();

        if (name == null || name.isEmpty()) {
            categories = categoryDao.findAll();
        } else {
            Category category = categoryDao.findByName(name);
            if (category != null) {
                categories.add(category);
            }
        }

        if (categories.isEmpty()) {
            throw new NotFoundException("No categories found");
        }

        List<CateDto> cateDtos = new ArrayList<CateDto>();
        for (Category category : categories) {
            CateDto cateDto = new CateDto();
            cateDto.setCateId(category.getCateId());
            cateDto.setCateName(category.getCateName());
            cateDtos.add(cateDto);
        }
        cateResponseDto.setCates(cateDtos);
        return cateResponseDto;
    }

    public CateResponseDto updateCategory(CateRequestDto dto) {
        Category existedCate = categoryDao.findById(dto.getCateId());
        if (existedCate == null) {
            throw new NotFoundException("Category not found");
        }
        Category cate = new Category(dto.getCateId(), dto.getCateName());
        categoryDao.update(cate);

        CateDto cateDto = new CateDto();
        cateDto.setCateId(cate.getCateId());
        cateDto.setCateName(cate.getCateName());

        CateResponseDto cateResponseDto = new CateResponseDto();
        cateResponseDto.setCates(cateDto);

        return cateResponseDto;
    }

    public CateResponseDto deleteCategory(Integer cateId) {
        Category existedCate = categoryDao.findById(cateId);
        if (existedCate == null) {
            throw new NotFoundException("Category not found");
        }
        categoryDao.delete(cateId);
        return new CateResponseDto(null, true, "Category deleted successfully");
    }
}



