package com.javaee.elderlycanteen.service;

import com.javaee.elderlycanteen.dao.*;
import com.javaee.elderlycanteen.dto.admin.*;
import com.javaee.elderlycanteen.entity.*;
import com.javaee.elderlycanteen.exception.NotFoundException;
import com.javaee.elderlycanteen.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

import static com.javaee.elderlycanteen.utils.DateUtils.getCurrentDate;

@Service
public class AdministratorService {

    private final AdministratorDao administratorDao;
    private final AccountDao accountDao;
    private final FinanceDao financeDao;
    private final VolReviewDao volReviewDao;
    private final RestockDao restockDao;
    private final PayWageDao payWageDao;
    private final DateUtils currentDate;

    @Autowired
    public AdministratorService(AdministratorDao administratorDao,AccountDao accountDao,FinanceDao financeDao,
                                VolReviewDao volReviewDao, RestockDao restockDao, PayWageDao payWageDao) {
        this.accountDao= accountDao;
        this.administratorDao = administratorDao;
        this.financeDao = financeDao;
        this.volReviewDao = volReviewDao;
        this.restockDao = restockDao;
        this.payWageDao = payWageDao;
        this.currentDate = new DateUtils();
    }

    public AdminResponseDto getAdminById(Integer accountId) {
        AdminResponseDto result = new AdminResponseDto();
        Administrator admin = administratorDao.getAdminById(accountId);
        if (admin == null) {
            throw new RuntimeException("administrator not found！");
        }
        Account account = accountDao.getAccountById(accountId);
        result.setResponse(new AdminResponseDto.AdminResponseData()) ;
        result.getResponse().setName(account.getName());
        result.getResponse().setIdCard(account.getIdCard());
        result.getResponse().setBirthDate(account.getBirthDate());
        result.getResponse().setAddress(account.getAddress());
        result.getResponse().setEmail(admin.getEmail());
        result.setSuccess(true);
        result.setMessage("search successfully !");
        return result;
    }

    public AdminResponseDto updateAdmin(Integer accountId, AdminInfoChangeDto request){
        AdminResponseDto result = new AdminResponseDto();

        //查找是否存在管理员
        Administrator admin = administratorDao.getAdminById(accountId);
        if (admin == null) {throw new NotFoundException("administrator not found！");}

        //查找是否存在用户
        Account account = accountDao.getAccountById(accountId);
        if(account == null ){ throw new NotFoundException("account not found") ;}

        //查找是否存在相同手手机号
        Account existingAccount = accountDao.findByPhoneNum(request.getPhoneNum());
        if (existingAccount != null && !existingAccount.getAccountId().equals(accountId)) {
            result.setSuccess(false);
            result.setMessage("手机号已被占用");
            return result;
        }

        //修改信息
        account.setPhoneNum(request.getPhoneNum());
        account.setAccountName(request.getAccountName());
        account.setAddress(request.getAddress());
        account.setBirthDate(request.getBirthDate());
        account.setGender(request.getGender());
        admin.setEmail(request.getEmail());
        //返回修改成功信息
        result.setSuccess(true);
        result.setMessage("update successfully!");
        return result;

    }

    //添加管理员
    public AdminResponseDto addAdmin(AdminRegisterDto admin) throws ParseException {
        AdminResponseDto result = new AdminResponseDto();

        //检查是否存在相同手手机号
        Account existingAccount = accountDao.findByPhoneNum(admin.getPhoneNum());
        if (existingAccount!= null) {
            result.setSuccess(false);
            result.setMessage("用户已注册");
            return result;
        }

        //检查是否存在相同身份证号
        boolean existIdCard = accountDao.existsByIdCard(admin.getIdCard());
        if (existIdCard) {
            result.setSuccess(false);
            result.setMessage("身份证号已被占用");
            return result;
        }

        //创建新用户
        Account account = new Account();
        account.setName(admin.getName());
        account.setIdCard(admin.getIdCard());
        account.setBirthDate(admin.getBirthDate());
        account.setAddress(admin.getAddress());
        account.setPhoneNum(admin.getPhoneNum());
        account.setGender(admin.getGender());
        account.setPassword("1");
        account.setAccountName("管理员");
        account.setIdentity("admin");
        account.setMoney(100.00);
        account.setVerifyCode(111);
        account.setPortrait("http://elderly-canteen.oss-cn-hangzhou.aliyuncs.com/16800023-portrait.jpg?Expires=1756828650&OSSAccessKeyId=LTAI5tK9KaDpnWNHxJU8hD67&Signature=inADCJChdV5U39TdwJAE7%2B7b2N8%3D");
        accountDao.addAccount(account);

        //创建新管理员
        Administrator newAdmin = new Administrator();
        newAdmin.setAccountId(account.getAccountId());
        newAdmin.setEmail(admin.getEmail());
        newAdmin.setAccountId(account.getAccountId());
        newAdmin.setPosition(admin.getPosition());
        administratorDao.addAdmin(newAdmin);

        //返回添加成功信息
        result.setSuccess(true);
        result.setMessage("add successfully!");
        return result;
    }

    public ResponseEntity<Void> deleteAdmin(Integer accountId) {
        //查找用户是否存在
        Account account = accountDao.getAccountById(accountId);
        if (account == null) {
            throw new NotFoundException("account not found");
        }
        //查找管理员是否存在
        Administrator admin = administratorDao.getAdminById(accountId);
        if (admin == null) {
            throw new NotFoundException("administrator not found");
        }
        //查找该管理员是否在财务
        Finance existFinance = financeDao.getFinanceById(accountId);
        if (existFinance != null) {
            throw new RuntimeException("该管理员在财务后台，无法删除");
        }
        //查找该管理员是否在进货
        Restock existRestock = restockDao.getRestockById(accountId);
        if (existRestock != null) {
            throw new RuntimeException("该管理员已在进货后台做过操作，无法删除");
        }
        //查找该管理员是否在请假
        VolReview existVolReview = volReviewDao.getVolReviewById(accountId);
        if (existVolReview != null) {
            throw new RuntimeException("该管理员已在审核志愿者申请后台做过操作，无法删除");
        }
        //查找该管理员是否在工资
        PayWage existPayWage = payWageDao.getPayWageById(accountId);
        if (existPayWage != null) {
            throw new RuntimeException("该管理员已在发工资后台做过操作，无法删除");
        }

        //删除管理员
        administratorDao.deleteAdmin(accountId);
        //删除用户
        accountDao.deleteUserFromAccount(accountId);
        //返回删除成功信息
        return ResponseEntity.ok().build();
    }

    public AdminSearchDto searchAdmin(String name , String position){
        Administrator admin = new Administrator();
        Account existAccountName = accountDao.getAccountByName(name);
        if(existAccountName == null){
            throw new NotFoundException("account not found");
        }
        return administratorDao.findAdminsByNameAndPosition(name , position);
    }

    public AdminInfoGetDto getAdminInfo (Integer accountId){
        Account account = accountDao.getAccountById(accountId);
        if(account == null){
            throw new NotFoundException("account not found");
        }
        Administrator admin = administratorDao.getAdminById(accountId);
        if (admin == null ){
            throw new NotFoundException("User is not an administrator");
        }
        AdminInfoGetDto result = new AdminInfoGetDto();
        result.setResponse(new AdminInfoGetDto.AdminInfoData());
        result.getResponse().setName(account.getName());
        result.getResponse().setIdCard(account.getIdCard());
        result.getResponse().setBirthDate(account.getBirthDate());
        result.getResponse().setAddress(account.getAddress());
        result.getResponse().setEmail(admin.getEmail());
        result.setSuccess(true);
        result.setMessage("search successfully!");
        return result;

    }
}