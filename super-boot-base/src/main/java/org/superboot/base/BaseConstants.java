package org.superboot.base;

/**
 * <b> 全局常量信息 </b>
 * <p>
 * 功能描述:全局常量及通用方法类
 * <p>
 * LINUX生成秘钥方法
 * <p>
 * <p>
 * #生成1024字节秘钥
 * openssl genrsa -out rsa_private_key.pem 1024
 * #生成公钥
 * openssl rsa -in rsa_private_key.pem -out rsa_public_key.pem -pubout
 * #如果要使用PKCS8编码执行下面语句
 * openssl pkcs8 -topk8 -in rsa_private_key.pem -out pkcs8_rsa_private_key.pem -nocrypt
 * <p>
 * #查看秘钥
 * <p>
 * cat rsa_private_key.pem
 * cat rsa_public_key.pem
 * cat pkcs8_rsa_private_key.pem
 *
 * </p>
 */
public class BaseConstants {


    /**
     * token存储字段名
     */
    public static final String TOKEN_KEY = "_SuperBootToken_";

    /**
     * 外部服务调用授权
     */
    public static final String OTHER_TOKEN_KEY = "_OtherToken_";

    /**
     * 外部调用传输消息ID
     */
    public static final String OTHER_MESSAGE_ID = "_OtherMessageId_";

    /**
     * 密钥存储字段
     */
    public static final String SECRET_KEY = "_secretKey_";

    /**
     * 基础包名
     */
    public static final String BASE_PACKAGE = "org.superboot";

    /**
     * 需要构建API对象的基本包路径
     */
    public static final String CONTROLLER_BASE_PATH = "org.superboot.controller";

    /**
     * 内部传输消息ID
     */
    public static final String GLOBAL_KEY = "_MessageId_";

    /**
     * 有管理权限的角色类型,多角色用英文逗号隔开
     */
    public static final String ADMIN_ROLE_TYPE = "SYS_ADMIN";

    /**
     * 用户TOKEN列表
     */
    public static final String USER_TOKEN = "UserToken";

    /**
     * TOKEN来源版本
     */
    public static final String TOKEN_VERSION = "TokenVersion";

    /**
     * TOKEN来源平台
     */
    public static final String TOKEN_PLATFORM = "TokenPlatform";


    /**
     * 全访问角色类型
     */
    public static final int ALL_USER_TYPE = -1;

    /**
     * 全访问用户角色名称
     */
    public static final String ALL_USER_NAME = "ALL_USER";


    /**
     * 系统管理员角色类型
     */
    public static final int SYS_ADMIN_TYPE = 0;

    /**
     * 系统管理员角色名称
     */
    public static final String SYS_ADMIN_NAME = "SYS_ADMIN";

    /**
     * 组织管理员角色类型
     */
    public static final int GROUP_ADMIN_TYPE = 1;

    /**
     * 组织管理员角色名称
     */
    public static final String GROUP_ADMIN_NAME = "GROUP_ADMIN";


    /**
     * 后台注册用户类型
     */
    public static final int GEN_USER_TYPE = 2;

    /**
     * 后台注册角色名称
     */
    public static final String GEN_USER_NAME = "GEN_USER";

    /**
     * 开放注册用户角色类型
     */
    public static final int PUB_USER_TYPE = 3;

    /**
     * 开发注册用户角色名称
     */
    public static final String PUB_USER_NAME = "PUB_USER";


    /**
     * 第三方接入角色类型
     */
    public static final int OTHER_USER_TYPE = 4;

    /**
     * 第三方接入角色名称
     */
    public static final String OTHER_USER_NAME = "OTHER_USER";


    /**
     * 开发人员角色类型
     */
    public static final int DEV_USER_TYPE = 5;

    /**
     * 开发人员角色名称
     */
    public static final String DEV_USER_NAME = "SYS_DEV";


    /**
     * 运维人员角色类型
     */
    public static final int OPER_USER_TYPE = 6;

    /**
     * 运维人员角色名称
     */
    public static final String OPER_USER_NAME = "SYS_OPER";


    /**
     * 数据状态 正常
     */
    public static final int DATA_STATUS_OK = 0;

    /**
     * 数据状态 删除
     */
    public static final int DATA_STATUS_DEL = 1;

    /**
     * 数据状态 封存
     */
    public static final int DATA_STATUS_CLOSE = 2;

    /**
     * 逻辑判断 Y
     */
    public static final String TRUE = "Y";

    /**
     * 逻辑判断 N
     */
    public static final String FALSE = "N";


    /**
     * 默认公钥
     */
    public static final String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDWxYt7nwrECUVXDhXusnIr2YCk\n" +
            "UtV1w3UBYV7D+ELuBJ+3Da6sR+Apfl1gIGL6aIa6yryo5prxlZ7PkSJJeEZs6kkt\n" +
            "f0HwvG94mNS1TJid3YYopatJug4b6awpL9LA3OS3bOoBN+/GJgCjSUDFR0+FjMDU\n" +
            "rD7HaaW7P9fVVEOnfQIDAQAB\n";

    /**
     * 默认私钥
     */
    public static final String DEFAULT_PRIVATE_KEY = "MIICXwIBAAKBgQDWxYt7nwrECUVXDhXusnIr2YCkUtV1w3UBYV7D+ELuBJ+3Da6s\n" +
            "R+Apfl1gIGL6aIa6yryo5prxlZ7PkSJJeEZs6kktf0HwvG94mNS1TJid3YYopatJ\n" +
            "ug4b6awpL9LA3OS3bOoBN+/GJgCjSUDFR0+FjMDUrD7HaaW7P9fVVEOnfQIDAQAB\n" +
            "AoGBAIf670NIATL5g7OIsY494VeuEGr6Hzi+lhhXo/6D8yxsNQtbWFqv71eWMFyq\n" +
            "ZMCS6arerfBqgXOv/asCAyltQ3iNN3x1G5Y1xP38VAcCakFAFdDzNj7NyjUoCZmA\n" +
            "FYJFazgXtYnkiqWPCDWZgO3Vo/eAmRjrWL7MYd1MooI4RPtBAkEA7DzekBKahuJ7\n" +
            "tJq9xTOQEtcHQ2Yx0b17wyfYxSuSvsr4K0Ws5h7owgnFImfai3cnLHDPJ3S7W4aD\n" +
            "bH8fTny9eQJBAOi8+D+MpkT6xUiBqU7yn3DWF0vkRKL5oPzt5ydGcGwfwl9bWIoB\n" +
            "RUaPxTp866GKFQdnyiosN2IcPw2gHMDeLSUCQQDGpgSs98fTJr3JR9nN6qb7ALAN\n" +
            "3/CCoyhgGbCxVAuovjpjAMr8Ev+8ssR/Qv9GCWL6fr3jOsiVlQ4MfPURZdlZAkEA\n" +
            "1SgVmWaA9dZTD6gpX7QY0ShTp99F+Zn1lxFwzujVmEGyLNGlkqe9QfX1Ji85Q/cK\n" +
            "cYDeiKd8he/m09zb2MzjvQJBAL+XOangdLbPLMrTd0Pkw9FVeSmVzmCKGXY0WePV\n" +
            "P6LfzLa7yizkEylDLwQkgiOUJYe4CojH65IR3BTThNKKcvs=\n";

    /**
     * 如果使用AOP拦截，则必须使用此私钥进行解密
     */
    public static final String DEFAULT_PKCS8_PRIVATE_KEY = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBANbFi3ufCsQJRVcO\n" +
            "Fe6ycivZgKRS1XXDdQFhXsP4Qu4En7cNrqxH4Cl+XWAgYvpohrrKvKjmmvGVns+R\n" +
            "Ikl4RmzqSS1/QfC8b3iY1LVMmJ3dhiilq0m6DhvprCkv0sDc5Lds6gE378YmAKNJ\n" +
            "QMVHT4WMwNSsPsdppbs/19VUQ6d9AgMBAAECgYEAh/rvQ0gBMvmDs4ixjj3hV64Q\n" +
            "avofOL6WGFej/oPzLGw1C1tYWq/vV5YwXKpkwJLpqt6t8GqBc6/9qwIDKW1DeI03\n" +
            "fHUbljXE/fxUBwJqQUAV0PM2Ps3KNSgJmYAVgkVrOBe1ieSKpY8INZmA7dWj94CZ\n" +
            "GOtYvsxh3UyigjhE+0ECQQDsPN6QEpqG4nu0mr3FM5AS1wdDZjHRvXvDJ9jFK5K+\n" +
            "yvgrRazmHujCCcUiZ9qLdycscM8ndLtbhoNsfx9OfL15AkEA6Lz4P4ymRPrFSIGp\n" +
            "TvKfcNYXS+REovmg/O3nJ0ZwbB/CX1tYigFFRo/FOnzroYoVB2fKKiw3Yhw/DaAc\n" +
            "wN4tJQJBAMamBKz3x9MmvclH2c3qpvsAsA3f8IKjKGAZsLFUC6i+OmMAyvwS/7yy\n" +
            "xH9C/0YJYvp+veM6yJWVDgx89RFl2VkCQQDVKBWZZoD11lMPqClftBjRKFOn30X5\n" +
            "mfWXEXDO6NWYQbIs0aWSp71B9fUmLzlD9wpxgN6Ip3yF7+bT3NvYzOO9AkEAv5c5\n" +
            "qeB0ts8sytN3Q+TD0VV5KZXOYIoZdjRZ49U/ot/MtrvKLOQTKUMvBCSCI5Qlh7gK\n" +
            "iMfrkhHcFNOE0opy+w==\n";


    /**
     * 默认服务调用授权KEY名称
     */
    public static final String FEIGN_KEY = "_FeignKey_";

    /**
     * 默认服务之间调用TOKEN，用于验证服务调用方是否是授权调用方
     */
    public static final String DEFAULT_FEIGN_TOKEN = "wN4tJQJBAMamBKz3x9MmvclH2c3qpvsAsA3f8IKjKGAZsLFUC6i+OmMAyvwS";


}
