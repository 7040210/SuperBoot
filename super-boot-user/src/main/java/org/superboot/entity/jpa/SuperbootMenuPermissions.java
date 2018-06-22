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
@Table(name = "superboot_menu_permissions", schema = "superboot", catalog = "")
public class SuperbootMenuPermissions {
    private Timestamp ts;
    private Integer dr;
    private Timestamp createtime;
    private Long createby;
    private Timestamp lastmodifytime;
    private Long lastmodifyby;
    private Long pkMenuPermissions;
    private Long pkPermissions;
    private Long pkMenu;

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
    @Column(name = "pk_menu_permissions")
    public Long getPkMenuPermissions() {
        return pkMenuPermissions;
    }

    public void setPkMenuPermissions(Long pkMenuPermissions) {
        this.pkMenuPermissions = pkMenuPermissions;
    }

    @Basic
    @Column(name = "pk_permissions")
    public Long getPkPermissions() {
        return pkPermissions;
    }

    public void setPkPermissions(Long pkPermissions) {
        this.pkPermissions = pkPermissions;
    }

    @Basic
    @Column(name = "pk_menu")
    public Long getPkMenu() {
        return pkMenu;
    }

    public void setPkMenu(Long pkMenu) {
        this.pkMenu = pkMenu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuperbootMenuPermissions that = (SuperbootMenuPermissions) o;
        return Objects.equals(ts, that.ts) &&
                Objects.equals(dr, that.dr) &&
                Objects.equals(createtime, that.createtime) &&
                Objects.equals(createby, that.createby) &&
                Objects.equals(lastmodifytime, that.lastmodifytime) &&
                Objects.equals(lastmodifyby, that.lastmodifyby) &&
                Objects.equals(pkMenuPermissions, that.pkMenuPermissions) &&
                Objects.equals(pkPermissions, that.pkPermissions) &&
                Objects.equals(pkMenu, that.pkMenu);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ts, dr, createtime, createby, lastmodifytime, lastmodifyby, pkMenuPermissions, pkPermissions, pkMenu);
    }
}
