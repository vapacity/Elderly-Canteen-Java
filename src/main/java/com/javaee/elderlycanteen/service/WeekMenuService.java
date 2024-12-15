package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.CategoryDao;
import com.javaee.elderlycanteen.dao.DishDao;
import com.javaee.elderlycanteen.dao.WeekMenuDao;
import com.javaee.elderlycanteen.dto.register.Menu;
import com.javaee.elderlycanteen.dto.register.MenuResponseDto;
import com.javaee.elderlycanteen.dto.weekMenu.*;
import com.javaee.elderlycanteen.entity.CartItem;
import com.javaee.elderlycanteen.entity.Category;
import com.javaee.elderlycanteen.entity.Dish;
import com.javaee.elderlycanteen.entity.WeekMenu;
import com.javaee.elderlycanteen.exception.NotFoundException;
import com.javaee.elderlycanteen.exception.ServiceException;
import com.javaee.elderlycanteen.utils.DateUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.javaee.elderlycanteen.utils.DateUtils.getCurrentDate;
import static com.javaee.elderlycanteen.utils.DateUtils.getDayOfWeek;

@Service
public class WeekMenuService {

    private final DishDao dishDao;
    private final WeekMenuDao weekMenuDao;
    private final SystemLogsService systemLogsService;
    private final CategoryDao cateDao;

    @Autowired
    public WeekMenuService(DishDao dishDao, WeekMenuDao weekMenuDao, SystemLogsService systemLogsService, CategoryDao cateDao) {
        this.weekMenuDao = weekMenuDao;
        this.dishDao = dishDao;
        this.systemLogsService = systemLogsService;
        this.cateDao = cateDao;
    }

    public Integer updateWeekMenuStock(Integer stock, Integer dishId, Date week) {
        Integer ret = this.weekMenuDao.updateWeekMenuStock(stock, dishId, week);
        if (ret != 1) {
            throw new ServiceException("update week menu stock failed!");
        }
        return ret;
    }

    public Integer updateWeekMenuSales(Integer sales, Integer dishId, Date week) {
        Integer ret = this.weekMenuDao.updateWeekMenuSales(sales, dishId, week);
        if (ret != 1) {
            throw new ServiceException("update week menu sales failed!");
        }
        return ret;
    }

    public Boolean checkAndReduceRestock(Integer dishId, Date week, Integer quantity) throws ParseException {
        //验证菜品是否在当天菜单
        WeekMenu weekMenu = this.weekMenuDao.findWeekMenuByWeekAndDishId(week, dishId);
        if (weekMenu == null) {
            throw new ServiceException("dish not in weekMenu!");
        }
        //验证库存是否满足需求
        if (weekMenu.getStock() < quantity) {
            throw new ServiceException("restock is not enough!");
        }

        // 减少库存
        this.updateWeekMenuStock(weekMenu.getStock() - quantity, dishId, week);
        this.updateWeekMenuSales(weekMenu.getSales() + quantity, dishId, week);

        //如果减少后的库存低于或等于10，触发低库存通知
        if (weekMenu.getStock() > 0 && weekMenu.getStock() < 10) {
            this.systemLogsService.addSystemLog("Warning",
                    "The spare capacity of today's dish No." + weekMenu.getDishId() +
                            " is less than 10, currently" + weekMenu.getStock() +
                            "please replenish in time");
        } else if (weekMenu.getStock() == 0) {
            this.systemLogsService.addSystemLog("Danger",
                    "Today's dish No." + weekMenu.getDishId() +
                            " is sold out, please replenish it in time");
        }
        return true;
    }

    public MenuResponseDto getTodayMenu() throws ParseException {
        Date date = getCurrentDate();
        String day = getDayOfWeek(date);
        List<WeekMenu> weekMenus = this.weekMenuDao.findWeekMenuByWeek(date, day);
        List<Menu> menus = new ArrayList<>();
        for (WeekMenu weekMenu : weekMenus) {
            Dish dish = this.dishDao.getDishById(weekMenu.getDishId());
            if (dish != null) {
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
        if (!menus.isEmpty()) {
            responseDto.setMessage("today's menu");
            responseDto.setSuccess(true);
        } else {
            responseDto.setMessage("no today's menu");
            responseDto.setSuccess(false);
        }
        return responseDto;
    }

    public WMResponseDto addWeekMenu(WMRequestDto weekMenu) {
        Date date = weekMenu.getDate();
        Integer requestDishId = weekMenu.getDishId();
        // 验证日期是否合法
        if (!checkDateValidity(date)) {
            throw new ServiceException("date should be equal or later than today's date!");
        }
        // 验证是否已经存在该日期的菜单
        if (this.weekMenuDao.findWeekMenuByWeekAndDishId(date, requestDishId) != null) {
            throw new ServiceException("week menu already exists!");
        }
        // 验证菜品是否存在
        Dish dish = this.dishDao.getDishById(requestDishId);
        if (dish == null) {
            throw new ServiceException("dish not exists!");
        }
        Integer cateId = dish.getCateId();
        String cateName = cateDao.findById(cateId).getCateName();

        WeekMenu weekMenuEntity = new WeekMenu(requestDishId, date, 50, 0, dish.getPrice(), DateUtils.getDayOfWeek(date));
        weekMenuDao.insert(weekMenuEntity);
        return new WMResponseDto(new WMResponseDto.DishInfo(cateName, dish.getDishName()), "add week menu success!", true);
    }

    private Boolean checkDateValidity(Date date) {
        // 获取当前日期
        Date currentDate = new Date();

        // 比较传入的日期与当前日期
        // 如果传入的日期大于或等于当前日期，返回 true
        System.out.println(date);
        System.out.println(currentDate);
        System.out.println(date.before(currentDate));
        return !date.before(currentDate);
    }

    public WMResponseDto removeWeekMenu(WMRequestDto weekMenu) {
        Date date = weekMenu.getDate();
        Integer requestDishId = weekMenu.getDishId();
        // 验证日期是否合法
        if (!checkDateValidity(date)) {
            throw new ServiceException("date should be equal or later than today's date!");
        }
        // 验证是否存在该日期的菜单
        if (this.weekMenuDao.findWeekMenuByWeekAndDishId(date, requestDishId) == null) {
            throw new ServiceException("week menu not exists!");
        }
        // 删除该日期的菜单
        this.weekMenuDao.deleteByWeekAndDishId(date, requestDishId);
        return new WMResponseDto(null, "remove week menu success!", true);
    }


    public AllWeekMenuResponseDto getWeekMenu(Date date) {
        try {
            Calendar calendar = getMondayOfWeek(date);

            // 创建一个 AllWeekMenuResponseDto 对象来存储整个周的菜单
            AllWeekMenuResponseDto response = new AllWeekMenuResponseDto();
            List<List<AllWeekMenuResponseDto.WeekDish>> weekMenuList = new ArrayList<>();

            // 通过一个循环来获取每一天的菜单，避免冗余
            for (int i = 0; i < 7; i++) {
                Date currentDay = calendar.getTime();
                List<WeekMenu> dailyMenu = weekMenuDao.findWeekMenuByWeek(currentDay, getDayOfWeek(currentDay));  // 查询当天的菜单
                System.out.println(dailyMenu.toString());

                List<AllWeekMenuResponseDto.WeekDish> dishes = convertToWeekDishList(dailyMenu); // 将数据库返回的菜单转换为 WeekDish 对象

                // 将每天的菜单添加到 weekMenuList 中
                weekMenuList.add(dishes);

                // 移动到下一天
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }

            // 将 weekMenuList 设置到响应对象
            response.setWeekMenuList(weekMenuList);

            return response;

        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
            return null;
        }
    }

    // 将从数据库查询到的菜单数据转换为 WeekDish 对象
    private List<AllWeekMenuResponseDto.WeekDish> convertToWeekDishList(List<WeekMenu> weekMenuList) {
        List<AllWeekMenuResponseDto.WeekDish> weekDishList = new ArrayList<>();
        for (WeekMenu weekMenu : weekMenuList) {
            Dish dish = dishDao.getDishById(weekMenu.getDishId());
            Category category = cateDao.findById(dish.getCateId());


            AllWeekMenuResponseDto.WeekDish weekDish = new AllWeekMenuResponseDto.WeekDish(
                    category.getCateName(),
                    dish.getDishId(),
                    dish.getDishName(),
                    dish.getPrice(),
                    weekMenu.getDisPrice()
            );
            weekDishList.add(weekDish);
        }
        return weekDishList;
    }

    public DiscountResponseDto uploadDiscount(DiscountRequestDto weekMenu) {
        Date date = weekMenu.getDate();
        Integer dishId = weekMenu.getDishId();
        Dish dish = this.dishDao.getDishById(dishId);
        if (dish == null) {
            throw new ServiceException("dish not exists!");
        }
        WeekMenu existedWeekMenu = weekMenuDao.findWeekMenuByWeekAndDishId(date, dishId);
        if (existedWeekMenu == null) {
            throw new ServiceException("dish in this date not exists!");
        }
        if (!checkDateValidity(date)) {
            throw new ServiceException("date should be equal or later than today's date!");
        }
        Double discount = weekMenu.getDiscount();
        if (discount < 0 || discount > 1) {
            throw new ServiceException("discount should be between 0 and 1!");
        }
        existedWeekMenu.setDisPrice(dish.getPrice() * discount);
        weekMenuDao.update(existedWeekMenu);
        return new DiscountResponseDto(dishId, "successfully update", true, dish.getPrice() * discount);
    }

    @Transactional
    public BatchResponseDto batchDiscount(BatchRequestDto weekMenu) {

        List<Integer> dishIds = weekMenu.getDishIds();
        Date date = weekMenu.getDate();
        if (!checkDateValidity(date)) {
            throw new ServiceException("date should be equal or later than today's date!");
        }
        Double discount = weekMenu.getDiscount();
        if (discount < 0 || discount > 1) {
            throw new ServiceException("discount should be between 0 and 1!");
        }
        List<DiscountDish> discountDishes = new ArrayList<DiscountDish>();
        for (Integer dishId : dishIds) {
            Dish dish = this.dishDao.getDishById(dishId);
            if (dish == null) {
                throw new ServiceException("dish not exists!");
            }
            WeekMenu existedWeekMenu = weekMenuDao.findWeekMenuByWeekAndDishId(date, dishId);
            if (existedWeekMenu == null) {
                throw new ServiceException("dish in this date not exists!");
            }
            existedWeekMenu.setDisPrice(dish.getPrice() * discount);
            discountDishes.add(new DiscountDish(dish.getPrice() * discount, dishId, dish.getDishName()));
            weekMenuDao.update(existedWeekMenu);
        }
        return new BatchResponseDto(discountDishes, "successfully update", true);

    }

    public AllDiscountResponseDto getAllDiscount(Date date) {
        List<AllDiscountResponseDto.DishDto> discountDishes = new ArrayList<AllDiscountResponseDto.DishDto> ();
        Calendar calendar = getMondayOfWeek(date);
        for (int i = 0; i < 7; i++) {
            Date currentDay = calendar.getTime();
            List<WeekMenu> dailyMenu = weekMenuDao.findWeekMenuByWeek(currentDay, getDayOfWeek(currentDay));  // 查询当天的菜单
            for (WeekMenu weekMenu : dailyMenu) {
                Dish dish = dishDao.getDishById(weekMenu.getDishId());
                String cateName = cateDao.findById(dish.getCateId()).getCateName();
                if(!Objects.equals(weekMenu.getDisPrice(), dish.getPrice())) {
                    discountDishes.add(new AllDiscountResponseDto.DishDto(
                            cateName,
                            weekMenu.getDisPrice(),
                            dish.getDishId(),
                            dish.getDishName(),
                            dish.getPrice()
                    ));
                }
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        if(discountDishes.isEmpty()) {
            throw new NotFoundException("no discount dishes!");
        }
        return new AllDiscountResponseDto(discountDishes);
    }
    private Calendar getMondayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // 计算当前日期是星期几 (1-7: Sunday-Saturday)
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println(dayOfWeek);

        // 计算该周的开始日期（假设周一是每周的第一天）
        int diffToMonday = (dayOfWeek == Calendar.SUNDAY) ? 6 : (dayOfWeek - Calendar.MONDAY);
        calendar.add(Calendar.DAY_OF_YEAR, -diffToMonday);  // 将日期调整到周一
        return calendar;
    }
}