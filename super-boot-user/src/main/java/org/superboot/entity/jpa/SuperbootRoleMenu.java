package org.superboot.entity.jpa;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <b>  </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Entity
@Table(name = "superboot_role_menu", schema = "superboot", catalog = "")
public class SuperbootRoleMenu {
    private Timestamp ts;
    private Integer dr;
    private Timestamp createtime;
    private Long createby;
    private Timestamp lastmodifytime;
    private Long lastmodifyby;
    private Long pkRoleMenu;
    private Long pkMenu;
    private Long pkRole;

    @Basic
    @Column(name = "ts")
    public Timestamp getTs() {
        return ts;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }

    @Basic
    @Column(name = "dr")
    public Integer getDr() {
        return dr;
    }

    public void setDr(Integer dr) {
        this.dr = dr;
    }

    @Basic
    @Column(name = "createtime")
    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    @Basic
    @Column(name = "createby")
    public Long getCreateby() {
        return createby;
    }

    public void setCreateby(Long createby) {
        this.createby = createby;
    }

    @Basic
    @Column(name = "lastmodifytime")
    public Timestamp getLastmodifytime() {
        return lastmodifytime;
    }

    public void setLastmodifytime(Timestamp lastmodifytime) {
        this.lastmodifytime = lastmodifytime;
    }

    @Basic
    @Column(name = "lastmodifyby")
    public Long getLastmodifyby() {
        return lastmodifyby;
    }

    public void setLastmodifyby(Long lastmodifyby) {
        this.lastmodifyby = lastmodifyby;
    }

    @Id
    @Column(name = "pk_role_menu")
    public Long getPkRoleMenu() {
        return pkRoleMenu;
    }

    public void setPkRoleMenu(Long pkRoleMenu) {
        this.pkRoleMenu = pkRoleMenu;
    }

    @Basic
    @Column(name = "pk_menu")
    public Long getPkMenu() {
        return pkMenu;
    }

    public void setPkMenu(Long pkMenu) {
        this.pkMenu = pkMenu;
    }

    @Basic
    @Column(name = "pk_role")
    public Long getPkRole() {
        return pkRole;
    }

    public void setPkRole(Long pkRole) {
        this.pkRole = pkRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuperbootRoleMenu that = (SuperbootRoleMenu) o;
        return Objects.equals(ts, that.ts) &&
                Objects.equals(dr, that.dr) &&
                Objects.equals(createtime, that.createtime) &&
                Objects.equals(createby, that.createby) &&
                Objects.equals(lastmodifytime, that.lastmodifytime) &&
                Objects.equals(lastmodifyby, that.lastmodifyby) &&
                Objects.equals(pkRoleMenu, that.pkRoleMenu) &&
                Objects.equals(pkMenu, that.pkMenu) &&
                Objects.equals(pkRole, that.pkRole);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ts, dr, createtime, createby, lastmodifytime, lastmodifyby, pkRoleMenu, pkMenu, pkRole);
    }
}
