package com.javaee.elderlycanteen.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Calendar;

@Aspect
@Component
public class DateAspect {
    // 对service包下的所有方法中的Date参数进行拦截
    // 定义一个切入点，匹配所有方法，适用于所有方法
    @Pointcut("execution(* com.javaee.elderlycanteen.service..*(..))")
    public void methodExecution() {}

    // 在方法执行前，扫描所有传入的参数
    @Before("methodExecution()")
    public void processDateArguments(JoinPoint joinPoint) {
        // 获取方法的所有参数
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg instanceof Date) {
                Date date = (Date) arg;
                clearTime(date); // 清除时间部分
            }
        }
    }

    private void clearTime(Date date) {
        if (date != null) {
            // 清除时间部分，只保留日期部分
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            date.setTime(cal.getTimeInMillis());
        }
    }
}
