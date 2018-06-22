package org.superboot.service.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.xiaoleilu.hutool.util.StrUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.superboot.base.*;
import org.superboot.common.pub.Pub_AuthUtils;
import org.superboot.common.pub.Pub_Tools;
import org.superboot.dao.jpa.*;
import org.superboot.dao.mybatis.PubMapper;
import org.superboot.entity.jpa.*;
import org.superboot.entity.request.*;
import org.superboot.entity.response.ResCount;
import org.superboot.entity.response.ResUser;
import org.superboot.service.UserService;
import org.superboot.utils.DateUtils;
import org.superboot.utils.MD5Util;
import org.superboot.utils.RandomStrUtils;
import org.superboot.utils.SensitiveInfoUtils;

import java.util.List;

/**
 * <b> 提供基于用户的相关基本操作 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Service
public class UserServiceImpl extends BaseService implements UserService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private GroupDAO groupDAO;

    @Autowired
    private UserRoleDAO userRoleDAO;


    @Autowired
    private UserEmployeesDAO impUserEmployeesDAO;

    @Autowired
    private EmployeesDAO impEmployeesDAO;

    @Autowired
    private OrgDAO impOrgDAO;


    @Autowired
    private PubMapper pubMapper;


    @Autowired
    private Pub_Tools pubTools;

    @Autowired
    private Pub_AuthUtils authUtils;


    /**
     * 生成密码
     *
     * @param passWord 明文密码
     * @param random   随机码
     * @return
     */
    public static String genPassWord(String passWord, String random) {
        String pdOne = MD5Util.MD5(passWord);
        return MD5Util.MD5(pdOne + random);
    }

    @Override
    public BaseResponse registerAdmin(RegisterUser regUser) throws BaseException {
        //构造默认用户
        SuperbootUser impUser = new SuperbootUser();
        BeanUtils.copyProperties(regUser, impUser);
        impUser.setPkGroup(-1L);
        return saveUser(impUser, BaseConstants.SYS_ADMIN_TYPE, -1);
    }

    @Override
    public BaseResponse registerGroupAdmin(RegisterUser regUser, long groupId) throws BaseException {
        //校验操作的组织是否是自己的组织
        if (-1 != getPkGroup()) {
            checkGroupIsMy(groupId);
        }

        //构造默认用户
        SuperbootUser impUser = new SuperbootUser();
        BeanUtils.copyProperties(regUser, impUser);
        if (null == groupDAO.findOne(groupId)) {
            throw new BaseException(StatusCode.GROUP_NOT_FIND);
        }
        impUser.setPkGroup(groupId);
        return saveUser(impUser, BaseConstants.GROUP_ADMIN_TYPE, -1);
    }

    @Override
    public BaseResponse registerRoleUser(RegisterUser regUser, long pkRole) throws BaseException {
        //构造默认用户
        SuperbootUser impUser = new SuperbootUser();
        BeanUtils.copyProperties(regUser, impUser);
        impUser.setPkGroup(getPkGroup());
        return saveUser(impUser, BaseConstants.GEN_USER_TYPE, pkRole);
    }

    @Override
    public BaseResponse registerGroupUser(RegisterUser regUser, long groupId, long pkRole) throws BaseException {
        //校验操作的组织是否是自己的组织
        checkGroupIsMy(groupId);
        //构造默认用户
        SuperbootUser impUser = new SuperbootUser();
        BeanUtils.copyProperties(regUser, impUser);
        impUser.setPkGroup(groupId);
        if (null == groupDAO.findOne(groupId)) {
            throw new BaseException(StatusCode.GROUP_NOT_FIND);
        }
        return saveUser(impUser, BaseConstants.GEN_USER_TYPE, pkRole);
    }

    @Override
    public BaseResponse register(RegisterUser regUser) throws BaseException {
        //构造默认用户
        SuperbootUser impUser = new SuperbootUser();
        BeanUtils.copyProperties(regUser, impUser);
        return saveUser(impUser, BaseConstants.PUB_USER_TYPE, -1);
    }

    @Override
    public BaseResponse login(LoginUser loginUser) throws BaseException {
        String usercode = loginUser.getUserCode();
        if (null == usercode) {
            throw new BaseException(StatusCode.USERNAME_NOT_FIND);
        }


        String rawPassword = loginUser.getUserPassword();
        if (null == rawPassword) {
            throw new BaseException(StatusCode.PASSWORD_NOT_FIND);
        }


        //使用DSL语法
        QSuperbootUser qSuperbootUser = QSuperbootUser.superbootUser;

        SuperbootUser user = getQueryFactory().
                selectFrom(qSuperbootUser).
                //允许使用账号登陆，暂时去除手机号、邮箱登陆方式
                        where(qSuperbootUser.userCode.eq(usercode))
                .fetchOne();


        //数据不存在或者数据删除
        if (null == user || BaseConstants.DATA_STATUS_DEL == user.getDr()) {
            throw new BaseException(StatusCode.ACCOUNT_NOT_FIND);
        }

        //判断账号是否超过停用时间
        if (StrUtil.isNotBlank(user.getEndTime())) {
            long num = DateUtils.getSecondDiffer(DateUtils.getCurrentDateTime(), user.getEndTime());
            if (0 < num) {
                throw new BaseException(StatusCode.ACCOUNT_EXPIRED);
            }
        }


        //数据封存
        if (BaseConstants.DATA_STATUS_CLOSE == user.getDr()) {
            throw new BaseException(StatusCode.ACCOUNT_DISABLED);
        }

        //账户锁定
        if (1 == user.getUserStatus()) {
            throw new BaseException(StatusCode.ACCOUNT_LOCKED);
        }

        //验证密码
        if (!user.getUserPassword().equals(genPassWord(loginUser.getUserPassword(), user.getEnKey()))) {
            throw new BaseException(StatusCode.PASSWORD_ERROR);
        }


        //刷新用户的权限信息
        authUtils.reloadByPkUser(user.getPkUser());

        return new BaseResponse(setBaseTokenBySuperbootUser(user));
    }


    /**
     * 根据用户数据初始化登陆用户信息
     *
     * @param user
     * @return
     */
    private BaseToken setBaseTokenBySuperbootUser(SuperbootUser user) {
        BaseToken token = new BaseToken();
        token.setUserId(user.getPkUser());
        token.setUserName(user.getUserCode());
        token.setPkGroup(user.getPkGroup() == null ? -1 : user.getPkGroup());
        token.setUserRole(getUserRoles(user.getPkUser()));
        token.setEndTime(user.getEndTime());
        token.setFullName(user.getUserName());


        //组织用户获取当前人员及机构
        if (-1 != token.getPkGroup()) {
            SuperbootUserEmployees iue = impUserEmployeesDAO.findByPkUserAndDr(user.getPkUser(), BaseConstants.DATA_STATUS_OK);
            if (null != iue) {
                //设置当前用户关联的员工
                token.setPkEmp(iue.getPkEmployees());
                //设置员工管理的机构
                SuperbootEmployees impEmployees = impEmployeesDAO.findOne(iue.getPkEmployees());
                //设置机构主键
                token.setPkOrg(impEmployees.getPkOrganization());

                //设置员工名称
                token.setEmpName(impEmployees.getEmployeesName());

                //机构名称
                token.setOrgName(impOrgDAO.findOne(impEmployees.getPkOrganization()).getOrgName());

                //设置用户的职务信息
                QSuperbootEmployeesDuties qed = QSuperbootEmployeesDuties.superbootEmployeesDuties;
                QSuperbootDuties duties = QSuperbootDuties.superbootDuties;
                List<SuperbootDuties> duList = getQueryFactory().select(duties)
                        .from(qed, duties)
                        .where(qed.pkEmployees.eq(impEmployees.getPkEmployees())
                                .and(qed.pkDuties.eq(duties.pkDuties))
                                .and(qed.dr.eq(BaseConstants.DATA_STATUS_OK))).fetch();

                String dustName = "";
                if (null != duList) {
                    if (0 < duList.size()) {
                        for (SuperbootDuties d : duList) {
                            dustName += d.getDutiesName() + ",";
                        }
                        dustName = dustName.substring(0, dustName.length() - 1);
                    }
                }
                token.setDutiesName(dustName);

            }
        }

        return token;

    }

    @Override
    public BaseResponse sso(ReqOtherLogin otherLogin) {


        //使用DSL语法
        QSuperbootUser qSuperbootUser = QSuperbootUser.superbootUser;

        //通过雇员的ID进行查询
        QSuperbootUserEmployees qSuperbootUserEmployees = QSuperbootUserEmployees.superbootUserEmployees;
        QSuperbootEmployees qSuperbootEmployees = QSuperbootEmployees.superbootEmployees;
        SuperbootUserEmployees employees = getQueryFactory().
                select(qSuperbootUserEmployees).from(qSuperbootUserEmployees, qSuperbootEmployees)
                .where(qSuperbootEmployees.pkEmployees.eq(qSuperbootUserEmployees.pkEmployees)
                        .and(qSuperbootUserEmployees.dr.eq(BaseConstants.DATA_STATUS_OK))
                        .and(qSuperbootEmployees.dr.eq(BaseConstants.DATA_STATUS_OK))
                        .and(qSuperbootEmployees.employeesCode.eq(otherLogin.getUserKey())).and(qSuperbootEmployees.pkGroup.eq(getPkGroup())))
                .fetchOne();

        if (null == employees) {
            throw new BaseException(StatusCode.NOT_FIND);
        }


        SuperbootUser user = getQueryFactory().
                selectFrom(qSuperbootUser).
                //允许使用账号、邮箱、手机号登陆
                        where(qSuperbootUser.pkUser.eq(employees.getPkUser()))
                .fetchOne();

        //判断账号是否超过停用时间
        if (StrUtil.isNotBlank(user.getEndTime())) {
            long num = DateUtils.getSecondDiffer(DateUtils.getCurrentDateTime(), user.getEndTime());
            if (0 < num) {
                throw new BaseException(StatusCode.ACCOUNT_EXPIRED);
            }
        }


        //判断请求的TOKEN是否与数据查询到的人员属于相同公司
        if (!user.getPkGroup().equals(getPkGroup())) {
            throw new BaseException(StatusCode.UNAUTHORIZED);
        }

        //数据不存在或者数据删除
        if (null == user || BaseConstants.DATA_STATUS_DEL == user.getDr()) {
            throw new BaseException(StatusCode.ACCOUNT_NOT_FIND);
        }

        //数据封存
        if (BaseConstants.DATA_STATUS_CLOSE == user.getDr()) {
            throw new BaseException(StatusCode.ACCOUNT_DISABLED);
        }

        //账户锁定
        if (1 == user.getUserStatus()) {
            throw new BaseException(StatusCode.ACCOUNT_LOCKED);
        }

        //刷新用户的权限信息
        authUtils.reloadByPkUser(user.getPkUser());


        return new BaseResponse(setBaseTokenBySuperbootUser(user));
    }

    @Override
    public BaseResponse password(long userId, ReqPassWord passWord) throws BaseException {

        QSuperbootUser qSuperbootUser = QSuperbootUser.superbootUser;

        SuperbootUser user = userDAO.findOne(qSuperbootUser.pkUser.eq(userId).and(qSuperbootUser.dr.eq(BaseConstants.DATA_STATUS_OK)));

        if (null != user) {
            //获取加密KEY
            String random = user.getEnKey();
            String oldPass = genPassWord(passWord.getOldpassword(), random);

            if (!user.getUserPassword().equals(oldPass)) {
                throw new BaseException(StatusCode.PASSWORD_ERROR);
            }

            //设置变更后的密码
            user.setUserPassword(genPassWord(passWord.getPassword(), random));
            //设置最后修改时间
            user.setLastPasswordResetDate(DateUtils.getTimestamp());

            //执行更新
            userDAO.save(user);

            //将所有的TOKEN清理掉,所有之前登陆的用户都需要用新密码进行登陆
            getRedisUtils().cleanAllToken(userId);

            return new BaseResponse(StatusCode.UPDATE_SUCCESS);
        }


        return new BaseResponse(StatusCode.UPDATE_SUCCESS);
    }


    @Override
    public BaseResponse setUserBase(long userId, ReqUserBase userBase) throws BaseException {

        SuperbootUser impUser = userDAO.findOne(userId);
        if (null == impUser) {
            throw new BaseException(StatusCode.ACCOUNT_NOT_FIND);
        }

        //非系统管理员判断是否操作用户和登陆人是同一个用户
        if (-1L != getPkGroup()) {
            checkGroupIsMy(impUser.getPkGroup());
        }


        impUser.setUserStatus(userBase.getUserStatus());
        impUser.setUserName(userBase.getUserName());
        impUser.setEndTime(userBase.getEndTime());

        //更新
        userDAO.save(impUser);

        //将所有的TOKEN清理掉,所有之前登陆的用户都需要进行登陆
        getRedisUtils().cleanAllToken(userId);

        return new BaseResponse(StatusCode.UPDATE_SUCCESS);
    }


    @Override
    public BaseResponse getUserCount(Predicate user, Predicate role, Predicate group) throws BaseException {
        QSuperbootUser qSuperbootUser = QSuperbootUser.superbootUser;
        QSuperbootUserRole userRole = QSuperbootUserRole.superbootUserRole;
        QSuperbootRole qRole = QSuperbootRole.superbootRole;
        QSuperbootGroup qGroup = QSuperbootGroup.superbootGroup;

        if (-1L == getPkGroup()) {
            long count = getQueryFactory().select(qSuperbootUser.pkUser.count()
            ).from(qSuperbootUser, userRole, qRole, qGroup)
                    .where(qSuperbootUser.dr.in(BaseConstants.DATA_STATUS_OK)
                            .and(qSuperbootUser.pkUser.eq(userRole.pkUser))
                            .and(userRole.pkRole.eq(qRole.pkRole))
                            .and(qSuperbootUser.pkGroup.eq(qGroup.pkGroup))
                            .and(user).and(role).and(group))
                    .fetchOne();
            return new BaseResponse(new ResCount(count));
        }
        long count = getQueryFactory().select(qSuperbootUser.pkUser.count()
        ).from(qSuperbootUser, userRole, qRole, qGroup)
                .where(qSuperbootUser.dr.in(BaseConstants.DATA_STATUS_OK)
                        .and(qSuperbootUser.pkUser.eq(userRole.pkUser))
                        .and(userRole.pkRole.eq(qRole.pkRole))
                        .and(qSuperbootUser.pkGroup.eq(qGroup.pkGroup))
                        .and(user).and(role).and(group).and(qSuperbootUser.pkGroup.eq(getPkGroup())))
                .fetchOne();
        return new BaseResponse(new ResCount(count));
    }

    @Override
    public BaseResponse delUser(long userId) throws BaseException {
        QSuperbootUser qUser = QSuperbootUser.superbootUser;
        SuperbootUser user = userDAO.findOne(qUser.pkUser.eq(userId).and(qUser.dr.eq(BaseConstants.DATA_STATUS_OK)));
        if (null != user) {
            //锁定账号
            user.setUserStatus(BaseConstants.DATA_STATUS_DEL);
            userDAO.save(user);

            //将所有的TOKEN清理掉,所有之前登陆的用户都需要用新密码进行登陆
            getRedisUtils().cleanAllToken(userId);
        }

        return new BaseResponse(StatusCode.DELETE_SUCCESS);
    }


    @Override
    public BaseResponse getUserList(Pageable pageable, boolean encryption, Predicate user, Predicate role, Predicate group) throws BaseException {
        QSuperbootUser qSuperbootUser = QSuperbootUser.superbootUser;
        QSuperbootUserRole userRole = QSuperbootUserRole.superbootUserRole;
        QSuperbootRole qRole = QSuperbootRole.superbootRole;
        QSuperbootGroup qGroup = QSuperbootGroup.superbootGroup;

        if (-1L == getPkGroup()) {
            List<ResUser> resUserList = getQueryFactory().select(Projections.bean(
                    ResUser.class,
                    qSuperbootUser.pkUser,
                    qSuperbootUser.userCode,
                    qSuperbootUser.userName,
                    qSuperbootUser.userEmail,
                    qSuperbootUser.userPhone,
                    qSuperbootUser.userId,
                    qSuperbootUser.userStatus,
                    qSuperbootUser.userAuth,
                    qSuperbootUser.initUser,
                    qSuperbootUser.endTime,
                    qRole.roleCode,
                    qRole.roleName,
                    qGroup.groupCode,
                    qGroup.groupName
            )).from(qSuperbootUser, userRole, qRole, qGroup)
                    .where(qSuperbootUser.dr.in(BaseConstants.DATA_STATUS_OK)
                            .and(qSuperbootUser.pkUser.eq(userRole.pkUser))
                            .and(userRole.pkRole.eq(qRole.pkRole))
                            .and(qSuperbootUser.pkGroup.eq(qGroup.pkGroup))
                            .and(user).and(role).and(group))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            if (encryption) {
                try {
                    SensitiveInfoUtils.sensitiveObject(resUserList);
                } catch (IllegalAccessException e) {
                    throw new BaseException(StatusCode.EXCEPTION);
                }
            }

            return new BaseResponse(resUserList);
        }

        List<ResUser> resUserList = getQueryFactory().select(Projections.bean(
                ResUser.class,
                qSuperbootUser.pkUser,
                qSuperbootUser.userCode,
                qSuperbootUser.userName,
                qSuperbootUser.userEmail,
                qSuperbootUser.userPhone,
                qSuperbootUser.userId,
                qSuperbootUser.userStatus,
                qSuperbootUser.userAuth,
                qSuperbootUser.initUser,
                qSuperbootUser.endTime,
                qRole.roleCode,
                qRole.roleName,
                qGroup.groupCode,
                qGroup.groupName
        )).from(qSuperbootUser, userRole, qRole, qGroup)
                .where(qSuperbootUser.dr.in(BaseConstants.DATA_STATUS_OK)
                        .and(qSuperbootUser.pkUser.eq(userRole.pkUser))
                        .and(userRole.pkRole.eq(qRole.pkRole))
                        .and(qSuperbootUser.pkGroup.eq(qGroup.pkGroup))
                        .and(user).and(role).and(group).and(qSuperbootUser.pkGroup.eq(getPkGroup())))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (encryption) {
            try {
                SensitiveInfoUtils.sensitiveObject(resUserList);
            } catch (IllegalAccessException e) {
                throw new BaseException(StatusCode.EXCEPTION);
            }
        }

        return new BaseResponse(resUserList);
    }

    /**
     * 生成用户信息
     *
     * @param impUser  注册提交内容
     * @param userType 用户类型
     * @param pkRole   角色主键
     * @param
     * @return
     */
    public BaseResponse saveUser(SuperbootUser impUser, int userType, long pkRole) {


        if (null != userDAO.findByUserCodeAndDr(impUser.getUserCode(), BaseConstants.DATA_STATUS_OK)) {
            throw new BaseException(StatusCode.ADD_ERROR_EXISTS);
        }

        if (StringUtils.isNotBlank(impUser.getUserEmail())) {
            if (null != userDAO.findByUserEmailAndDr(impUser.getUserEmail(), BaseConstants.DATA_STATUS_OK)) {
                throw new BaseException(StatusCode.EMAIL_ERROR_EXISTS);
            }
        }

        if (StringUtils.isNotBlank(impUser.getUserPhone())) {
            if (null != userDAO.findByUserPhoneAndDr(impUser.getUserPhone(), BaseConstants.DATA_STATUS_OK)) {
                throw new BaseException(StatusCode.PHONE_ERROR_EXISTS);
            }
        }


        String random = RandomStrUtils.generateRandomString(8);


        //进行密码加密
        impUser.setUserPassword(genPassWord(impUser.getUserPassword(), random));
        impUser.setLastPasswordResetDate(DateUtils.getTimestamp());
        impUser.setEnKey(random);
        //设置用户为初始用户，此处用于一些要求用户第一次登陆后需要一些个性操作使用
        impUser.setInitUser(0);
        impUser.setPkUser(pubTools.genUUID());
        //设置用户状态 0为正常 1为锁定
        impUser.setUserStatus(0);
        //默认为认证用户，只有开放注册进来的用户才为非注册用户
        impUser.setUserAuth(1);


        //设置默认用户角色
        SuperbootUserRole userRole = new SuperbootUserRole();


        SuperbootRole role = null;

        if (BaseConstants.SYS_ADMIN_TYPE == userType) {
            role = roleDAO.findByRoleCodeAndDr(BaseConstants.SYS_ADMIN_NAME, BaseConstants.DATA_STATUS_OK);

        } else if (BaseConstants.GROUP_ADMIN_TYPE == userType) {
            role = roleDAO.findByRoleCodeAndDr(BaseConstants.GROUP_ADMIN_NAME, BaseConstants.DATA_STATUS_OK);

        } else if (BaseConstants.PUB_USER_TYPE == userType) {
            role = roleDAO.findByRoleCodeAndDr(BaseConstants.PUB_USER_NAME, BaseConstants.DATA_STATUS_OK);
            //开发注册用户 认证状态为0 如果需要认证则需要身份证等信息
            impUser.setUserAuth(0);
            //开发注册的用户默认放入开发组织中
            impUser.setPkGroup(groupDAO.findByGroupCodeAndDr("pub", BaseConstants.DATA_STATUS_OK).getPkGroup());
        } else if (BaseConstants.GEN_USER_TYPE == userType) {
            role = roleDAO.findOne(pkRole);
        }

        if (null == role) {
            throw new BaseException(StatusCode.ROLE_NOT_FIND);
        }

        impUser = userDAO.save(impUser);

        userRole.setPkUser(impUser.getPkUser());
        userRole.setPkRole(role.getPkRole());
        userRole.setUserRoleId(pubTools.genUUID());
        userRoleDAO.save(userRole);

        //返回信息

        return new BaseResponse(StatusCode.ADD_SUCCESS, setBaseTokenBySuperbootUser(impUser));
    }

    /**
     * 获取用户角色
     *
     * @param pkUser 用户主键
     * @return
     */
    private String getUserRoles(long pkUser) {
        //使用DSL语法进行数据查询
        QSuperbootRole qSuperbootRole = QSuperbootRole.superbootRole;
        QSuperbootUserRole qSuperbootUserRole = QSuperbootUserRole.superbootUserRole;
        List<SuperbootRole> roles = getQueryFactory()
                .select(qSuperbootRole)
                .from(qSuperbootRole, qSuperbootUserRole)
                .where(
                        qSuperbootUserRole.pkRole.eq(qSuperbootRole.pkRole)
                                .and(qSuperbootRole.dr.eq(0))
                                .and(qSuperbootUserRole.dr.eq(0))
                                .and(qSuperbootUserRole.pkUser.eq(pkUser)))

                .fetch();
        StringBuffer roleStr = new StringBuffer();
        if (null != roles) {
            for (SuperbootRole role : roles) {
                roleStr.append(role.getRoleCode()).append(",");
            }
            return roleStr.substring(0, roleStr.length() - 1).toString();
        }
        return roleStr.toString();
    }
}
