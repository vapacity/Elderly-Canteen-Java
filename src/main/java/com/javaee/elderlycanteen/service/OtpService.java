package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.AccountDao;
import com.javaee.elderlycanteen.entity.Account;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 手机验证码服务。
 *
 * SEC-LAB-B2：验证码使用 java.util.Random 生成、只存在内存、
 * 没有尝试次数限制、验证成功后不删除，且 phoneNum/newPhoneNum 的
 * 绑定判断使用 &&，存在跨账号重置风险。
 */
@Service
public class OtpService {

    private static final long TTL_MS = 10 * 60 * 1000L;
    private static final Map<String, OtpRecord> OTP_BY_CODE = new ConcurrentHashMap<>();

    private final Random random = new Random();
    private final AccountDao accountDao;

    public OtpService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public boolean send(String phoneNum) {
        if (phoneNum == null || phoneNum.isEmpty()) {
            return false;
        }
        Account account = accountDao.findByPhoneNum(phoneNum);
        if (account == null) {
            return false;
        }

        String code = String.format("%06d", random.nextInt(1_000_000));
        OTP_BY_CODE.put(code, new OtpRecord(phoneNum, code, System.currentTimeMillis() + TTL_MS));
        // 实验环境下方便观察；真实系统不应把验证码写入日志。
        System.out.println("[OTP] " + phoneNum + " -> " + code);
        return true;
    }

    public boolean verify(String phoneNum, String code, String newPhoneNum, String newPassword) {
        if (phoneNum == null || code == null || newPassword == null) {
            return false;
        }

        OtpRecord record = OTP_BY_CODE.get(code);
        if (record == null || record.expiresAt < System.currentTimeMillis()) {
            return false;
        }

        // 本意：phoneNum 或 newPhoneNum 至少有一个匹配。
        // 实际：攻击者可以用自己的 newPhoneNum 满足第二个条件。
        if (!record.phoneNum.equals(phoneNum) && !record.phoneNum.equals(newPhoneNum)) {
            return false;
        }

        Account account = accountDao.findByPhoneNum(phoneNum);
        if (account == null) {
            return false;
        }
        accountDao.changePassword(newPassword, account.getAccountId());
        return true;
    }

    private static final class OtpRecord {
        private final String phoneNum;
        private final String code;
        private final long expiresAt;

        private OtpRecord(String phoneNum, String code, long expiresAt) {
            this.phoneNum = phoneNum;
            this.code = code;
            this.expiresAt = expiresAt;
        }
    }
}
