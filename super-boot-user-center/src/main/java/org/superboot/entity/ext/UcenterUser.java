package org.superboot.entity.ext;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * <b>  </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/15
 * @time 19:13
 * @Path org.superboot.entity.ext.UcenterUser
 */
@Entity
@Table(name = "ucenter_user", schema = "super_boot_ucenter", catalog = "")
public class UcenterUser {
    private Timestamp ts;
    private Integer dr;
    private Long pkUser;
    private String userCode;
    private String userPassword;
    private String random;
    private Timestamp lastPasswordResetDate;

    @Basic
    @Column(name = "ts", nullable = false)
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
    @Column(name = "random", nullable = true, length = 32)
    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
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

        UcenterUser that = (UcenterUser) o;

        if (ts != null ? !ts.equals(that.ts) : that.ts != null) return false;
        if (dr != null ? !dr.equals(that.dr) : that.dr != null) return false;
        if (pkUser != null ? !pkUser.equals(that.pkUser) : that.pkUser != null) return false;
        if (userCode != null ? !userCode.equals(that.userCode) : that.userCode != null) return false;
        if (userPassword != null ? !userPassword.equals(that.userPassword) : that.userPassword != null) return false;
        if (random != null ? !random.equals(that.random) : that.random != null) return false;
        if (lastPasswordResetDate != null ? !lastPasswordResetDate.equals(that.lastPasswordResetDate) : that.lastPasswordResetDate != null)
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
        result = 31 * result + (random != null ? random.hashCode() : 0);
        result = 31 * result + (lastPasswordResetDate != null ? lastPasswordResetDate.hashCode() : 0);
        return result;
    }
}
