package org.superboot.entity.base.sys;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * <b>  </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/9
 * @time 11:20
 * @Path org.superboot.entity.base.sys.SysUser
 */
@Entity
@Table(name = "sys_user", schema = "superboot", catalog = "")
public class SysUser {
    private Timestamp ts;
    private Integer dr;
    private Long pkUser;
    private String userCode;
    private String userPassword;
    private String userEmail;
    private String userPhone;
    private String userName;
    private String userIdcard;
    private Integer userAuth;
    private Timestamp lastPasswordResetDate;

    @Basic
    @Column(name = "ts", nullable = true)
    public Timestamp getTs() {
        return ts;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }

    @Basic
    @Column(name = "dr", nullable = true)
    public Integer getDr() {
        return dr;
    }

    public void setDr(Integer dr) {
        this.dr = dr;
    }

    @Id
    @Column(name = "pk_user", nullable = false)
    public Long getPkUser() {
        return pkUser;
    }

    public void setPkUser(Long pkUser) {
        this.pkUser = pkUser;
    }

    @Basic
    @Column(name = "user_code", nullable = true, length = 20)
    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    @Basic
    @Column(name = "user_password", nullable = true, length = 60)
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Basic
    @Column(name = "user_email", nullable = true, length = 50)
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Basic
    @Column(name = "user_phone", nullable = true, length = 20)
    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    @Basic
    @Column(name = "user_name", nullable = true, length = 20)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "user_idcard", nullable = true, length = 20)
    public String getUserIdcard() {
        return userIdcard;
    }

    public void setUserIdcard(String userIdcard) {
        this.userIdcard = userIdcard;
    }

    @Basic
    @Column(name = "user_auth", nullable = true)
    public Integer getUserAuth() {
        return userAuth;
    }

    public void setUserAuth(Integer userAuth) {
        this.userAuth = userAuth;
    }

    @Basic
    @Column(name = "last_password_reset_date", nullable = true)
    public Timestamp getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Timestamp lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SysUser sysUser = (SysUser) o;

        if (ts != null ? !ts.equals(sysUser.ts) : sysUser.ts != null) return false;
        if (dr != null ? !dr.equals(sysUser.dr) : sysUser.dr != null) return false;
        if (pkUser != null ? !pkUser.equals(sysUser.pkUser) : sysUser.pkUser != null) return false;
        if (userCode != null ? !userCode.equals(sysUser.userCode) : sysUser.userCode != null) return false;
        if (userPassword != null ? !userPassword.equals(sysUser.userPassword) : sysUser.userPassword != null)
            return false;
        if (userEmail != null ? !userEmail.equals(sysUser.userEmail) : sysUser.userEmail != null) return false;
        if (userPhone != null ? !userPhone.equals(sysUser.userPhone) : sysUser.userPhone != null) return false;
        if (userName != null ? !userName.equals(sysUser.userName) : sysUser.userName != null) return false;
        if (userIdcard != null ? !userIdcard.equals(sysUser.userIdcard) : sysUser.userIdcard != null) return false;
        if (userAuth != null ? !userAuth.equals(sysUser.userAuth) : sysUser.userAuth != null) return false;
        if (lastPasswordResetDate != null ? !lastPasswordResetDate.equals(sysUser.lastPasswordResetDate) : sysUser.lastPasswordResetDate != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ts != null ? ts.hashCode() : 0;
        result = 31 * result + (dr != null ? dr.hashCode() : 0);
        result = 31 * result + (pkUser != null ? pkUser.hashCode() : 0);
        result = 31 * result + (userCode != null ? userCode.hashCode() : 0);
        result = 31 * result + (userPassword != null ? userPassword.hashCode() : 0);
        result = 31 * result + (userEmail != null ? userEmail.hashCode() : 0);
        result = 31 * result + (userPhone != null ? userPhone.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (userIdcard != null ? userIdcard.hashCode() : 0);
        result = 31 * result + (userAuth != null ? userAuth.hashCode() : 0);
        result = 31 * result + (lastPasswordResetDate != null ? lastPasswordResetDate.hashCode() : 0);
        return result;
    }
}
