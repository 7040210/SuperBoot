package org.superboot.base;

/**
 * <b> 状态码 </b>
 * <p>
 * 功能描述:定义系统的状态码 1系列为错误码 2系列为成功码 3系列为重复码 4系列为系统码 5系列为异常码 6系列为业务码
 * <b></>添加前尽量避免与HttpStatus状态码重复，如果重复可重写状态码返回消息内容，复用HttpStatus状态码</b>
 * </p>
 */
public enum StatusCode {
    OK(200, "请求成功"),
    NO(100, "请求失败"),
    EXCEPTION(500, "程序出现异常"),


    UNAUTHORIZED(401, "访问未授权"),
    NOT_FIND(404, "信息不存在"),

    METHOD_NOT_ALLOWED(405, "不支持当前请求方法"),


    DECODE_FAIL(409, "解密失败"),
    AUTH_SUCCESS(410, "授权成功"),
    UNAUTH_SUCCESS(411, "取消授权成功"),
    OPERATION_SUCCESS(412, "操作成功"),
    OPERATION_FAIL(413, "操作失败"),
    UNSUPPORTED_MEDIA_TYPE(415, "不支持当前媒体类型"),
    RATELIMIT_ERR(429, "请求超过最大数，请稍后再访问"),

    PARAMS_NOT_VALIDATE(501, "参数校验失败"),
    LANG_NOT_FIND(502, "参数【语言】不能为空"),
    VERSION_NOT_FIND(503, "参数【客户端版本】不能为空"),
    PLATFORM_NOT_FIND(504, "参数【来源平台】不能为空"),
    ACCOUNT_NOT_FIND(505, "账号不存在"),
    TOKEN_NOT_FIND(506, "Token不能为空"),
    TOKEN_HEAD_NOT_FIND(507, "TokenHead不能为空"),
    TOKEN_INVALID(508, "TOKEN无效"),
    TOKEN_SUCCESS(509, "TOKEN验证通过"),
    TOKEN_LOCKED(510, "授权过期"),
    BAD_REQUEST(511, "参数解析失败"),
    SERVICE_NOT_OK(514, "程序跑丢了,稍后再重试"),
    LOCKED_SUCCESS(515, "锁定成功"),
    UNLOCKED_SUCCESS(516, "解锁成功"),
    UNAUTHORIZED_OPERATION(517, "操作未授权"),
    AUTHORIZATION_OPERATION(518, "操作已授权"),
    JOB_EXISTS(519, "任务正在运行中，请稍后重试"),
    JOB_EXEC_SUCCESS(520, "任务调度成功，请稍后查看执行结果"),
    JOB_EXEC_FAIL(521, "任务调度失败，请到调度平台查看错误原因"),
    JOB_NOT_FIND(522, "任务不存在，请检查"),
    FILE_UPLOAD_SUCCESS(523, "文件上传成功"),
    FILE_UPLOAD_FAIL(524, "文件上传失败"),
    FILE_CHECK_FAIL(525, "文件已存在"),
    FILE_CHECK_SUCCESS(526, "文件不存在"),

    USERNAME_NOT_FIND(601, "用户不能为空"),
    PASSWORD_NOT_FIND(602, "密码不能为空"),
    LOGIN_INVALID(603, "登陆信息无效"),
    USER_STATUS_EXCEPTION(604, "用户状态异常"),
    ACCOUNT_EXPIRED(605, "账户过期"),
    ACCOUNT_LOCKED(606, "账户锁定"),
    ACCOUNT_DISABLED(607, "账户不可用"),
    ROLE_NOT_FIND(608, "角色不存在"),
    PASSWORD_ERROR(609, "密码错误"),
    GROUP_NOT_FIND(610, "组织不存在"),
    EMAIL_ERROR_EXISTS(611, "邮箱已存在"),
    PHONE_ERROR_EXISTS(612, "手机号已存在"),
    MENU_F_NOT_FIND(613, "上级菜单不存在"),
    MENU_OEDER_CODE_LENGTH_ERROR(614, "菜单内部编码错误，菜单内部编码应为三位一级"),
    DATA_QUOTE(615, "数据被引用，无法执行操作"),
    DATA_NOT_FNID(616, "数据不存在"),

    ADD_SUCCESS(201, "添加成功"),
    ADD_ERROR(101, "添加失败"),
    ADD_ERROR_EXISTS(301, "数据重复，添加失败"),

    UPDATE_SUCCESS(202, "修改成功"),
    UPDATE_ERROR(102, "修改失败"),


    DELETE_SUCCESS(203, "删除成功"),
    DELETE_ERROR(103, "删除失败"),

    DATABASE_COLLECT_SUCCESS(204, "数据库连接成功"),
    DATABASE_COLLECT_ERROR(104, "数据库连接失败"),

    SQL_VALIDATE_SUCCESS(205, "sql验证成功"),
    SQL_VALIDATE_ERROR(105, "sql验证失败"),

    DATA_RULE_ERROR(106, "数据规则验证失败"),

    EXPRESSION_NOT_VALIDATE(107, "复杂公式校验失败,复杂运算中不允许使用简单运算符");

    private final int code;
    private final String message;

    StatusCode(int code, String message) {
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
