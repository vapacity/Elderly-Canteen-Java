package com.javaee.elderlycanteen.config;

import com.javaee.elderlycanteen.utils.DateUtils;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Date;

@Profile("demo")
@Configuration
public class DemoData {
    private static final String SAMPLE_ADDRESS = "同济新村测试楼 101 室（虚构地址）";

    private static final Object[][] DISHES = {
        {1, "清炒小青菜", 6.0, 1, "greens", 5.0, 80},
        {2, "红烧鸡腿", 16.0, 2, "chicken", 13.0, 80},
        {3, "白米饭", 2.0, 3, "rice", 0.0, 100},
        {4, "番茄蛋汤", 6.0, 4, "soup", 0.0, 60},
        {5, "无糖豆浆", 4.0, 5, "drink", 0.0, 60},
        {6, "香菇蒸鸡", 18.0, 2, "limited", 15.0, 1}
    };

    @Bean
    ApplicationRunner demoSeed(JdbcTemplate db) {
        return args -> {
            String demoPassword = System.getenv("CANTEEN_DEMO_PASSWORD");
            if (demoPassword == null || demoPassword.isBlank()) {
                throw new IllegalStateException("Set CANTEEN_DEMO_PASSWORD before starting the demo");
            }
            String[] categories = {"素菜", "荤菜", "主食", "汤", "饮品"};
            for (int i = 0; i < categories.length; i++) {
                db.update("MERGE INTO Category (cateId, cateName) KEY(cateId) VALUES (?, ?)", i + 1, categories[i]);
            }
            for (int i = 1; i <= 11; i++) {
                seedUser(db, i, demoPassword, i == 10 ? 1 : 120, SAMPLE_ADDRESS);
            }
            seedUser(db, 13, demoPassword, 120, "");
            for (int i = 21; i <= 30; i++) {
                seedUser(db, i, demoPassword, 120, SAMPLE_ADDRESS);
            }
            String adminPassword = System.getenv("CANTEEN_ADMIN_PASSWORD");
            if (adminPassword != null && !adminPassword.isBlank()) {
                db.update("""
                    INSERT INTO Account (accountName,password,phoneNum,identity,money,address)
                    SELECT '演示管理员', ?, '19900000012', 'admin', 0, ''
                    WHERE NOT EXISTS (SELECT 1 FROM Account WHERE phoneNum = '19900000012')
                    """, adminPassword);
                db.update("UPDATE Account SET password = ? WHERE phoneNum = '19900000012' AND identity = 'admin'",
                        adminPassword);
            }
            LocalDate today = LocalDate.now(ZoneId.of("Asia/Shanghai"));
            for (Object[] dish : DISHES) {
                db.update("MERGE INTO Dish (dishId,dishName,price,cateId,imageUrl) KEY(dishId) VALUES (?,?,?,?,?)",
                        dish[0], dish[1], dish[2], dish[3], "/demo-img/" + dish[4] + ".svg");
                for (int offset = 0; offset < 90; offset++) {
                    Date date = Date.valueOf(today.plusDays(offset));
                    db.update("""
                        INSERT INTO WeekMenu (dishId,week,stock,sales,disPrice,day)
                        SELECT ?, ?, ?, 0, ?, ? WHERE NOT EXISTS (SELECT 1 FROM WeekMenu WHERE dishId=? AND week=?)
                        """, dish[0], date, dish[6], dish[5], DateUtils.getDayOfWeek(date), dish[0], date);
                }
            }
        };
    }

    private static void seedUser(JdbcTemplate db, int number, String password, int balance, String address) {
        String phone = "199000000%02d".formatted(number);
        db.update("""
            INSERT INTO Account (accountName,password,phoneNum,identity,money,address)
            SELECT ?, ?, ?, 'User', ?, ?
            WHERE NOT EXISTS (SELECT 1 FROM Account WHERE phoneNum = ?)
            """, "测试账号 %02d".formatted(number), password, phone, balance, address, phone);
    }
}
