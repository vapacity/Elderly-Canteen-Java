package com.javaee.elderlycanteen.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static Date getCurrentDate() throws ParseException {
        Date now = new Date();
        //格式化日期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 获取当前日期并格式化为"yyyy-MM-dd"格式的字符串
        String formattedDate = dateFormat.format(now);
        // 将格式化后的字符串再次转换为Date对象
        Date date = dateFormat.parse(formattedDate);
        return date;
    }
}
