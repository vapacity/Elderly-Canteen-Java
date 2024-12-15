package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.AccountDao;

import com.javaee.elderlycanteen.dao.SeniorDao;
import com.javaee.elderlycanteen.dto.account.AccountDto;
import com.javaee.elderlycanteen.dto.authentication.AuthenticationRequestDto;
import com.javaee.elderlycanteen.dto.authentication.AuthenticationResponseDto;
import com.javaee.elderlycanteen.dto.login.LoginRequestIdDto;
import com.javaee.elderlycanteen.dto.personInfo.PersonInfoRequestDto;
import com.javaee.elderlycanteen.dto.personInfo.PersonInfoResponseDto;
import com.javaee.elderlycanteen.dto.personInfo.PhoneResponseDto;
import com.javaee.elderlycanteen.dto.volServe.AccessOrderResponseDto;
import com.javaee.elderlycanteen.entity.Account;
import com.javaee.elderlycanteen.dto.login.LoginRequestDto;
import com.javaee.elderlycanteen.entity.Senior;
import com.javaee.elderlycanteen.exception.NotFoundException;
import com.javaee.elderlycanteen.utils.DateUtils;

import com.javaee.elderlycanteen.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service

public class AccountService {

    private final AccountDao accountDao;
    private final SeniorDao seniorDao;
    private final DateUtils currentDate;
    @Autowired
    public AccountService(AccountDao accountDao, SeniorDao seniorDao) {
        this.accountDao = accountDao;
        this.seniorDao = seniorDao;
        this.currentDate = new DateUtils();
    }

    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.bucketName}")
    private String bucketName;


    public Account login(LoginRequestDto loginRequestDto) {
        Account account = accountDao.login(loginRequestDto.getPhoneNum(), loginRequestDto.getPassword());
        if (account == null) {
            throw new RuntimeException("登录失败，账户或密码错误!");
        }
        return account;
    }


    public Account getAccountById(Integer accountId) {
        Account account = accountDao.getAccountById(accountId);
        if (account == null) {
            throw new RuntimeException("未找到账户信息！");
        }
        return account;
    }

    public Integer addAccount(Account account) {
        return accountDao.insertAccount(account);
    }


    public Integer deleteAccount(Integer accountId) {
        return accountDao.deleteUserById(accountId);
    }

    //查询个人信息
    public PersonInfoResponseDto getPersonInfo(Integer accountId)
    {
        Account account = accountDao.getAccountById(accountId);
        if(account == null) {
            throw new NotFoundException("account not found");
        }
        Senior senior = seniorDao.selectByAccountId(accountId);
        if(senior == null) {
            throw new NotFoundException("senior not found");
        }
//        System.out.println(account.getAccountName()+"/"+account.getIdCard()+"/"+account.getGender());
        PersonInfoResponseDto result = new PersonInfoResponseDto();

        result.setResponse(new PersonInfoResponseDto.ResponseData());
        result.getResponse().setAccountId(account.getAccountId());
        result.getResponse().setAccountName(account.getAccountName());
        result.getResponse().setBirthDate(account.getBirthDate());
        result.getResponse().setGender(account.getGender());
        result.getResponse().setName(account.getName());
        result.getResponse().setIdCard(account.getIdCard());
        result.getResponse().setPortrait(account.getPortrait());
        result.getResponse().setAddress(account.getAddress());
        result.getResponse().setMoney(account.getMoney());
        result.getResponse().setPhoneNum(account.getPhoneNum());
        result.getResponse().setIdentity(account.getIdentity());
        result.getResponse().setSubsidy(senior.getSubsidy());
        return result;
    }

    public AccountDto getAllAccount(Integer accountId){
        Account account = accountDao.getAccountById(accountId);
        AccountDto accountDto = new AccountDto();

        // 将 Account 对象的值赋给 AccountDto 对象的对应字段
        accountDto.setAccountId(account.getAccountId());
        accountDto.setAccountName(account.getAccountName());
        accountDto.setGender(account.getGender());
        accountDto.setBirthDate(account.getBirthDate());
        accountDto.setAddress(account.getAddress());
        accountDto.setPhoneNum(account.getPhoneNum());
        accountDto.setMoney(account.getMoney());
        accountDto.setPortrait(account.getPortrait());
        accountDto.setIdentity(account.getIdentity());
        accountDto.setName(account.getName());
        accountDto.setIdCard(account.getIdCard());
        return accountDto;

    }

    //修改个人信息
    public PersonInfoResponseDto alterPersonInfo (PersonInfoRequestDto personInfo, Integer accountId , MultipartFile avatar) throws ParseException {
        Account account = accountDao.getAccountById(accountId);
        PersonInfoResponseDto result = new PersonInfoResponseDto();
        if(account == null) {
            throw new NotFoundException("account not found");
        }
        // 更新账户名
        if (personInfo.getAccountName() != null && !personInfo.getAccountName().isEmpty()) {
            account.setAccountName(personInfo.getAccountName());
        }

        // 更新手机号
        if (personInfo.getPhoneNum() != null && !personInfo.getPhoneNum().isEmpty()) {
            // 检查手机号是否已存在
            Account existingAccount = accountDao.findByPhoneNum(personInfo.getPhoneNum());
            if (existingAccount != null && !existingAccount.getAccountId().equals(accountId)) {
                result.setAlterSuccess(false);
                result.setMsg("手机号已被占用");
                return result;
            }
            account.setPhoneNum(personInfo.getPhoneNum());
        }

        // 更新头像
        if (avatar != null) {
            String fileName = avatar.getOriginalFilename();
            String imageUrl = endpoint +"/"+bucketName+"/"+fileName;
            account.setPortrait(imageUrl);
        }

        //更新其他信息

        if (personInfo.getGender() != null && !personInfo.getGender().isEmpty()) {
            account.setGender(personInfo.getGender());
        }

        if (personInfo.getBirthDate() != null) {
            account.setBirthDate(personInfo.getBirthDate());
        }

        if (personInfo.getAddress() != null && !personInfo.getAddress().isEmpty()) {
            account.setAddress(personInfo.getAddress());
        }

        if (personInfo.getName() != null && !personInfo.getName().isEmpty()) {
            account.setName(personInfo.getName());
        }
        accountDao.updatePersonInfo(account.getPortrait(),account.getGender(),account.getAccountName(),account.getPhoneNum(),account.getAddress(),account.getBirthDate(),account.getName());
        result.setAlterSuccess(true);
        result.setMsg("成功修改个人信息");
        return result;
    }

    //实名认证
    public AuthenticationResponseDto nameAuthenticate(Integer accountId, AuthenticationRequestDto input){
        Account account = accountDao.getAccountById(accountId);
        AuthenticationResponseDto result = new AuthenticationResponseDto();
        if(account == null) {
            throw new NotFoundException(" 用户不存在 ");
        }
        if (account.getIdCard() != null ){
            if(account.getIdCard().length() != 18){
                result.setSuccess(false);
                result.setMessage("身份证号格式不正确");
                return result;
            }
        }
        boolean idCardExists = accountDao.existsByIdCard(input.getIdCard());
        if(idCardExists){
            result.setSuccess(false);
            result.setMessage("该身份已被注册");
        }
        // 更新账户信息
        account.setName(input.getName());
        account.setIdCard(input.getIdCard());
        Date birthDate = extractBirthDateFromIdCard(input.getIdCard());
        System.out.println(birthDate+"--"+account.getIdCard()+" --"+account.getName());
        account.setBirthDate(birthDate);
        // 判断是否为老年人
        if (calculateAge(birthDate) >= 60){
            Senior senior = new Senior();
            senior.setAccountId(account.getAccountId());
            senior.setFamilyNum(account.getPhoneNum());
            senior.setSubsidy(50.0);
            account.setIdentity("senior");
            accountDao.updatePersonIdentity(account.getIdentity(),account.getName(),account.getBirthDate(),account.getIdCard(),accountId);
            result.setSuccess(true);
            result.setMessage("老人身份认证成功");
            return result;
        }
        //普通用户认证
        result.setSuccess(true);
        result.setMessage("普通用户认证成功");
        accountDao.updatePersonIdentity("User",account.getName(),account.getBirthDate(),account.getIdCard(),accountId);
        return result;
    }

    //修改密码
    public ResponseEntity<Void> changePassword (Integer accountId , String password){
        Account account = accountDao.getAccountById(accountId);
        if (account == null ){
            throw new NotFoundException("用户不存在");
        }
        accountDao.changePassword(password, accountId);
        return ResponseEntity.ok().build();

    }
    //验证密码
    public boolean VerifyPassword (String password , Integer accountId){
        Account account = accountDao.getAccountById(accountId);
        if (account == null ){
            throw new NotFoundException("用户不存在");
        }
        return Objects.equals(password, account.getPassword());
    }

    //修改手机号
    public PhoneResponseDto changePhone (Integer accountId , String phoneNum){
        Account account = accountDao.getAccountById(accountId);
        if (account == null ){
            throw new NotFoundException("用户不存在");
        }
        PhoneResponseDto result = new PhoneResponseDto();
        // 检查数据库中是否已存在相同的手机号
        Account existingAccount = accountDao.findByPhoneNum(phoneNum);
        if (existingAccount != null && !existingAccount.getAccountId().equals(accountId)) {
            result.setSuccess(false);
            result.setMsg("手机号已被占用");
            return result;
        }
        accountDao.changePhoneNum(phoneNum,accountId);
        result.setMsg("修改手机号成功");
        result.setSuccess(true);
        return result;
    }

    public ResponseEntity<Void> deleteUser (Integer accountId ){
        Account account = accountDao.getAccountById(accountId);
        if (account == null ){
            throw new NotFoundException("用户不存在");
        }
        accountDao.deleteUserFromAccount(accountId);
        return ResponseEntity.ok().build();
    }

    //充值
    public ResponseEntity<Void> creditAccount (Integer accountId, Double money){
        Account account = accountDao.getAccountById(accountId);
        if (account == null ){
            throw new NotFoundException("用户不存在");
        }
        account.setMoney(account.getMoney()+ money) ;
        accountDao.creditAccount(account.getMoney(), accountId);
        return ResponseEntity.ok().build();
    }
    private Date extractBirthDateFromIdCard (String idCard){
        System.out.println(idCard);
        String birthDateStr = idCard.substring(6,14);
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(birthDateStr);
        }catch (ParseException e) {
            throw new IllegalArgumentException("身份证号中出生日期无效");
        }
    }

    private int calculateAge(Date birthDate) {
        // 根据出生日期计算年龄
        Calendar birthCal = Calendar.getInstance();
        birthCal.setTime(birthDate);

        Calendar nowCal = Calendar.getInstance();
        int age = nowCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);

        if (nowCal.get(Calendar.DAY_OF_YEAR) < birthCal.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        System.out.println(age);
        return age;
    }



    public Integer updateAccountMoney(Integer accountId, Double money) {
        Integer ret = accountDao.updateAccountMoney(accountId, money);
        if (ret != 1) {
            throw new ServiceException("update Account money failed!");
        }
        return ret;

    }
}