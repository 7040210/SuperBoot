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
@Table(name = "superboot_user_employees", schema = "superboot", catalog = "")
public class SuperbootUserEmployees {
    private Timestamp ts;
    private Integer dr;
    private Timestamp createtime;
    private Long createby;
    private Timestamp lastmodifytime;
    private Long lastmodifyby;
    private Long pkUserEmployees;
    private Long pkEmployees;
    private Long pkUser;

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
    @Column(name = "pk_user_employees")
    public Long getPkUserEmployees() {
        return pkUserEmployees;
    }

    public void setPkUserEmployees(Long pkUserEmployees) {
        this.pkUserEmployees = pkUserEmployees;
    }

    @Basic
    @Column(name = "pk_employees")
    public Long getPkEmployees() {
        return pkEmployees;
    }

    public void setPkEmployees(Long pkEmployees) {
        this.pkEmployees = pkEmployees;
    }

    @Basic
    @Column(name = "pk_user")
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
        SuperbootUserEmployees that = (SuperbootUserEmployees) o;
        return Objects.equals(ts, that.ts) &&
                Objects.equals(dr, that.dr) &&
                Objects.equals(createtime, that.createtime) &&
                Objects.equals(createby, that.createby) &&
                Objects.equals(lastmodifytime, that.lastmodifytime) &&
                Objects.equals(lastmodifyby, that.lastmodifyby) &&
                Objects.equals(pkUserEmployees, that.pkUserEmployees) &&
                Objects.equals(pkEmployees, that.pkEmployees) &&
                Objects.equals(pkUser, that.pkUser);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ts, dr, createtime, createby, lastmodifytime, lastmodifyby, pkUserEmployees, pkEmployees, pkUser);
    }
}
