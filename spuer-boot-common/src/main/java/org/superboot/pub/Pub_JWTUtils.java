package org.superboot.pub;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.superboot.base.BaseConstants;
import org.superboot.base.BaseToken;
import org.superboot.base.SuperBootCode;
import org.superboot.base.SuperBootException;
import org.superboot.utils.AESUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <b> JWT工具类 </b>
 * <p>
 * 功能描述:提供JWT生成及验证功能
 * </p>
 *
 * @author jesion
 * @date 2017/9/15
 * @time 12:40
 * @Path org.superboot.pub.utils.Pub_JWTUtils
 */
@Component
public class Pub_JWTUtils {


    private static final long serialVersionUID = -3301605591108950415L;


    /**
     * JWT加密秘钥
     */
    private final String SECRET = "DSDXt7zooL3cYsDjt794p";

    /**
     * Token头信息
     */
    private final String TOKEN_HEADER = "Bearer";
    /**
     * 指定TOKEN过期时间，默认1天
     */
    private final Long expiration = 604800L;


    private static final String CLAIM_KEY_USERNAME = "username";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_KEY_USERID = "userid";


    /**
     * 根据用户信息生成令牌信息
     *
     * @param token 用户信息
     * @return
     */
    public String generateToken(BaseToken token) {

        Map<String, Object> claims = new HashMap<String, Object>();

        claims.put(CLAIM_KEY_CREATED, new Date());
        String userName = token.getUsername();
        if (StringUtils.isBlank(userName)) {
            throw new SuperBootException(SuperBootCode.ACCOUNT_NOT_FIND);
        }
        claims.put(CLAIM_KEY_USERNAME, userName);
        Long userid = token.getUserid();
        if (0 == userid) {
            throw new SuperBootException(SuperBootCode.ACCOUNT_NOT_FIND);
        }
        claims.put(CLAIM_KEY_USERID, userid);


        return TOKEN_HEADER + " " + AESUtil.AESEncode(Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, SECRET)//指定算法方式，使用RSA算法需要注意特殊字符造成的异常
                .compact());
    }


    /**
     * 生成令牌信息
     *
     * @param claims 令牌信息
     * @return
     */
    private String generateToken(Claims claims) {

        return TOKEN_HEADER + " " + AESUtil.AESEncode(Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, SECRET)//指定算法方式，使用RSA算法需要注意特殊字符造成的异常
                .compact());
    }

    /**
     * 刷新TOKEN
     *
     * @param token TOKEN信息
     * @return
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            throw new SuperBootException(SuperBootCode.TOKEN_INVALID);
        }
        return refreshedToken;
    }

    /**
     * 解析令牌信息
     *
     * @param token TOKEN信息
     * @return
     */
    public Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            if (token.startsWith(TOKEN_HEADER)) {
                token = token.substring(TOKEN_HEADER.length());
            }
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(AESUtil.AESDecode(token))//增加AES解密
                    .getBody();
        } catch (Exception e) {
            throw new SuperBootException(SuperBootCode.TOKEN_INVALID);
        }
        return claims;
    }


    /**
     * 生成过期时间信息
     *
     * @return
     */
    public Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 获取用户名
     *
     * @param token TOKEN信息
     * @return
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = (String) claims.get(CLAIM_KEY_USERNAME);
        } catch (Exception e) {
            throw new SuperBootException(SuperBootCode.TOKEN_INVALID);
        }
        return username;
    }


    /**
     * 获取用户ID
     *
     * @param token TOKEN信息
     * @return
     */
    public Long getUserIdFromToken(String token) {
        Long userid;
        try {
            Claims claims = getClaimsFromToken(token);
            userid = Long.valueOf("" + claims.get(CLAIM_KEY_USERID));
        } catch (Exception e) {
            throw new SuperBootException(SuperBootCode.TOKEN_INVALID);
        }
        return userid;
    }


    /**
     * 获取创建时间
     *
     * @param token TOKEN信息
     * @return
     */
    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            throw new SuperBootException(SuperBootCode.TOKEN_INVALID);
        }
        return created;
    }


    /**
     * 获取TOKEN失效时间
     *
     * @param token TOKEN信息
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            throw new SuperBootException(SuperBootCode.TOKEN_INVALID);
        }
        return expiration;
    }

    /**
     * 校验TOKEN是否过期
     *
     * @param token TOKEN信息
     * @return
     */
    public Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    /**
     * TOKEN创建时间是否在密码最后修改时间之前
     *
     * @param created           创建时间
     * @param lastPasswordReset 密码最后修改时间
     * @return
     */
    public Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (null != lastPasswordReset && created.before(lastPasswordReset));
    }


    /**
     * 检验是否需要刷新TOKEN信息
     *
     * @param token             TOKEN信息
     * @param lastPasswordReset 密码修改时间
     * @return
     */
    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && !isTokenExpired(token);
    }


    /**
     * 验证TOKEN是否过期
     *
     * @param token TOKEN信息
     * @return
     */
    public Boolean validateToken(String token) {
        String username = getUsernameFromToken(token);
        Date created = getCreatedDateFromToken(token);
        return (!isTokenExpired(token));
    }


    /**
     * 根据请求信息获取用户TOKEN信息
     *
     * @param request 请求头信息
     * @return
     */
    public BaseToken getTokenInfo(HttpServletRequest request) {
        BaseToken userInfo = null;
        //判断请求头是否包含授权TOKEN
        String token = request.getHeader(BaseConstants.TOKEN_KEY);
        if (null == token) {
            //判断COOKIE是否包含授权TOKEN
            Cookie[] cookies = request.getCookies();
            if(null != cookies){
                for (Cookie cookie : cookies) {
                    if (BaseConstants.TOKEN_KEY.toLowerCase().equals(cookie.getName().toLowerCase())) {
                        token = cookie.getValue();
                    }
                }
            }

        }

        if (null != token) {
            try {
                Claims claims = getClaimsFromToken(token);
                userInfo = new BaseToken();
                //属性复制
                BeanUtils.copyProperties(claims, userInfo);

            } catch (Exception e) {
                throw new SuperBootException(SuperBootCode.TOKEN_INVALID);
            }
        }

        return userInfo;

    }

}
