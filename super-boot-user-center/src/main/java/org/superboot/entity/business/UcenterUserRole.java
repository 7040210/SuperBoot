package org.superboot.entity.business;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * <b>  </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/16
 * @time 19:03
 * @Path org.superboot.entity.business.UcenterUserRole
 */
@Entity
@Table(name = "ucenter_user_role", schema = "super_boot_user", catalog = "")
public class UcenterUserRole {
    private Timestamp ts;
    private Integer dr;
    private Long userRoleId;
    private Long pkRole;
    private Long pkUser;

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
    @Column(name = "user_role_id", nullable = false)
    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    @Basic
    @Column(name = "pk_role", nullable = true)
    public Long getPkRole() {
        return pkRole;
    }

    public void setPkRole(Long pkRole) {
        this.pkRole = pkRole;
    }

    @Basic
    @Column(name = "pk_user", nullable = true)
    public Long getPkUser() {
        return pkUser;
    }

    public void setPkUser(Long pkUser) {
        this.pkUser = pkUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UcenterUserRole that = (UcenterUserRole) o;

        if (ts != null ? !ts.equals(that.ts) : that.ts != null) return false;
        if (dr != null ? !dr.equals(that.dr) : that.dr != null) return false;
        if (userRoleId != null ? !userRoleId.equals(that.userRoleId) : that.userRoleId != null) return false;
        if (pkRole != null ? !pkRole.equals(that.pkRole) : that.pkRole != null) return false;
        if (pkUser != null ? !pkUser.equals(that.pkUser) : that.pkUser != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ts != null ? ts.hashCode() : 0;
        result = 31 * result + (dr != null ? dr.hashCode() : 0);
        result = 31 * result + (userRoleId != null ? userRoleId.hashCode() : 0);
        result = 31 * result + (pkRole != null ? pkRole.hashCode() : 0);
        result = 31 * result + (pkUser != null ? pkUser.hashCode() : 0);
        return result;
    }
}
