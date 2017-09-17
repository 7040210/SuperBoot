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
 * @Path org.superboot.entity.business.UcenterRole
 */
@Entity
@Table(name = "ucenter_role", schema = "super_boot_user", catalog = "")
public class UcenterRole {
    private Timestamp ts;
    private Integer dr;
    private Long pkRole;
    private String roleCode;
    private String roleName;
    private String roleInfo;
    private Integer roleType;

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
    @Column(name = "pk_role", nullable = false)
    public Long getPkRole() {
        return pkRole;
    }

    public void setPkRole(Long pkRole) {
        this.pkRole = pkRole;
    }

    @Basic
    @Column(name = "role_code", nullable = false, length = 20)
    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    @Basic
    @Column(name = "role_name", nullable = true, length = 100)
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Basic
    @Column(name = "role_info", nullable = true, length = -1)
    public String getRoleInfo() {
        return roleInfo;
    }

    public void setRoleInfo(String roleInfo) {
        this.roleInfo = roleInfo;
    }

    @Basic
    @Column(name = "role_type", nullable = true)
    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UcenterRole that = (UcenterRole) o;

        if (ts != null ? !ts.equals(that.ts) : that.ts != null) return false;
        if (dr != null ? !dr.equals(that.dr) : that.dr != null) return false;
        if (pkRole != null ? !pkRole.equals(that.pkRole) : that.pkRole != null) return false;
        if (roleCode != null ? !roleCode.equals(that.roleCode) : that.roleCode != null) return false;
        if (roleName != null ? !roleName.equals(that.roleName) : that.roleName != null) return false;
        if (roleInfo != null ? !roleInfo.equals(that.roleInfo) : that.roleInfo != null) return false;
        if (roleType != null ? !roleType.equals(that.roleType) : that.roleType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ts != null ? ts.hashCode() : 0;
        result = 31 * result + (dr != null ? dr.hashCode() : 0);
        result = 31 * result + (pkRole != null ? pkRole.hashCode() : 0);
        result = 31 * result + (roleCode != null ? roleCode.hashCode() : 0);
        result = 31 * result + (roleName != null ? roleName.hashCode() : 0);
        result = 31 * result + (roleInfo != null ? roleInfo.hashCode() : 0);
        result = 31 * result + (roleType != null ? roleType.hashCode() : 0);
        return result;
    }
}
