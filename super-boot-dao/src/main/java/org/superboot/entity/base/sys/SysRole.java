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
 * @Path org.superboot.entity.base.sys.SysRole
 */
@Entity
@Table(name = "sys_role", schema = "superboot", catalog = "")
public class SysRole {
    private Timestamp ts;
    private Integer dr;
    private Long pkRole;
    private String roleCode;
    private String roleName;
    private String roleInfo;
    private Integer roleType;

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

        SysRole sysRole = (SysRole) o;

        if (ts != null ? !ts.equals(sysRole.ts) : sysRole.ts != null) return false;
        if (dr != null ? !dr.equals(sysRole.dr) : sysRole.dr != null) return false;
        if (pkRole != null ? !pkRole.equals(sysRole.pkRole) : sysRole.pkRole != null) return false;
        if (roleCode != null ? !roleCode.equals(sysRole.roleCode) : sysRole.roleCode != null) return false;
        if (roleName != null ? !roleName.equals(sysRole.roleName) : sysRole.roleName != null) return false;
        if (roleInfo != null ? !roleInfo.equals(sysRole.roleInfo) : sysRole.roleInfo != null) return false;
        if (roleType != null ? !roleType.equals(sysRole.roleType) : sysRole.roleType != null) return false;

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
