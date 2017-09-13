package org.superboot.base;

/**
 * <b> 状态码 </b>
 * <p>
 * 功能描述:定义系统的状态码 1系列为错误码 2系列为成功码 3系列为重复码 4系列为业务码 5系列为异常码
 * </p>
 *
 * @author jesion
 * @date 2017/9/9
 * @time 15:46
 * @Path org.superboot.base.SuperBootStatus
 */
public enum SuperBootCode {
    OK(200, "请求成功"),
    NO(100, "请求失败"),


    UNAUTHORIZED(401,"访问未授权"),
    NOT_FIND(404,"信息不存在"),


    PARAMS_NOT_FIND(501,"参数不能为空"),
    LANG_NOT_FIND(502,"参数【语言】不能为空"),
    VERSION_NOT_FIND(503,"参数【客户端版本】不能为空"),
    PLATFORM_NOT_FIND(504,"参数【来源平台】不能为空"),
    ACCOUNT_NOT_FIND(505,"账号不存在"),
    TOKEN_NOT_FIND(506,"Token不能为空"),
    TOKEN_HEAD_NOT_FIND(507,"TokenHead不能为空"),
    TOKEN_INVALID(508,"TOKEN无效"),

    USERNAME_NOT_FIND(601,"用户不能为空"),
    PASSWORD_NOT_FIND(602,"密码不能为空"),
    LOGIN_INVALID(603,"登陆信息无效"),
    USER_STATUS_EXCEPTION(604,"用户状态异常"),
    ACCOUNT_EXPIRED(605,"账户过期"),
    ACCOUNT_LOCKED(606,"账户锁定"),
    ACCOUNT_DISABLED(607,"账户不可用"),

    ADD_SUCCESS(201,"添加成功"),
    ADD_ERROR(101,"添加失败"),
    ADD_ERROR_EXISTS(301,"数据重复，添加失败"),

    UPDATE_SUCCESS(202,"修改成功"),
    UPDATE_ERROR(102,"修改失败"),


    DELETE_SUCCESS(203,"删除成功"),
    DELETE_ERROR(103,"删除失败");

    private final int code;
    private final String message;

    SuperBootCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
