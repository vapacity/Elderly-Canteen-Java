package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.DishDao;
import com.javaee.elderlycanteen.dao.WeekMenuDao;
import com.javaee.elderlycanteen.dto.register.Menu;
import com.javaee.elderlycanteen.dto.register.MenuResponseDto;
import com.javaee.elderlycanteen.entity.CartItem;
import com.javaee.elderlycanteen.entity.Dish;
import com.javaee.elderlycanteen.entity.WeekMenu;
import com.javaee.elderlycanteen.exception.ServiceException;
import com.javaee.elderlycanteen.utils.DateUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.javaee.elderlycanteen.utils.DateUtils.getCurrentDate;
import static com.javaee.elderlycanteen.utils.DateUtils.getDayOfWeek;

@Service
public class WeekMenuService {

    private final DishDao dishDao;
    private final WeekMenuDao weekMenuDao;
    private final SystemLogsService systemLogsService;


    @Autowired
    public WeekMenuService(DishDao dishDao ,WeekMenuDao weekMenuDao,SystemLogsService systemLogsService) {
        this.weekMenuDao = weekMenuDao;
        this.dishDao = dishDao;
        this.systemLogsService = systemLogsService;
    }

    public Integer updateWeekMenuStock(Integer stock,Integer dishId,Date week) {
        Integer ret = this.weekMenuDao.updateWeekMenuStock(stock,dishId,week);
        if (ret !=1){
            throw new ServiceException("update week menu stock failed!");
        }
        return ret;
    }

    public Integer updateWeekMenuSales(Integer sales,Integer dishId,Date week) {
        Integer ret = this.weekMenuDao.updateWeekMenuSales(sales,dishId,week);
        if (ret !=1){
            throw new ServiceException("update week menu sales failed!");
        }
        return ret;
    }

    public Boolean checkAndReduceRestock(Integer dishId, Date week , Integer quantity) throws ParseException {
        //验证菜品是否在当天菜单
        WeekMenu weekMenu = this.weekMenuDao.findWeekMenuByWeekAndDishId(week,dishId);
        if(weekMenu == null){
            throw new ServiceException("dish not in weekMenu!");
        }
        //验证库存是否满足需求
        if(weekMenu.getStock() < quantity){
            throw new ServiceException("restock is not enough!");
        }

        // 减少库存
        this.updateWeekMenuStock(weekMenu.getStock() - quantity,dishId,week);
        this.updateWeekMenuSales(weekMenu.getSales() + quantity,dishId,week);

        //如果减少后的库存低于或等于10，触发低库存通知
        if(weekMenu.getStock() > 0 && weekMenu.getStock() < 10){
            this.systemLogsService.addSystemLog("Warning",
                    "The spare capacity of today's dish No."+weekMenu.getDishId()+
                            " is less than 10, currently"+weekMenu.getStock()+
                            "please replenish in time");
        }else if(weekMenu.getStock() == 0){
            this.systemLogsService.addSystemLog("Danger",
                    "Today's dish No."+weekMenu.getDishId()+
                            " is sold out, please replenish it in time");
        }
        return true;
    }

    public MenuResponseDto getTodayMenu() throws ParseException {
        Date date = getCurrentDate();
        List<WeekMenu> weekMenus = this.weekMenuDao.findWeekMenuByWeek(date,getDayOfWeek(date));
        List<Menu> menus = new ArrayList<>();
        for(WeekMenu weekMenu : weekMenus){
            Dish dish = this.dishDao.getDishById(weekMenu.getDishId());
            if(dish != null){
                Menu menu = new Menu();

                menu.setDishId(dish.getDishId());
                menu.setCategory(dish.getDishId());
                menu.setDishName(dish.getDishName());
                menu.setDishPrice(dish.getPrice());
                menu.setImageUrl(dish.getImageUrl());
                menu.setSales(weekMenu.getSales());
                menu.setStock(weekMenu.getStock());

                menus.add(menu);
            }
        }
        MenuResponseDto responseDto = new MenuResponseDto();
        responseDto.setMenu(menus);
        if(menus.size() > 0){
            responseDto.setMessage("today's menu");
            responseDto.setSuccess(true);
        }else{
            responseDto.setMessage("no today's menu");
            responseDto.setSuccess(false);
        }
        return responseDto;
    }

}