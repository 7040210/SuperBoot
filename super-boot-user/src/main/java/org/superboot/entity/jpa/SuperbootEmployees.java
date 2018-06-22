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
@Table(name = "superboot_employees", schema = "superboot", catalog = "")
public class SuperbootEmployees {
    private Timestamp ts;
    private Integer dr;
    private Timestamp createtime;
    private Long createby;
    private Timestamp lastmodifytime;
    private Long lastmodifyby;
    private Long pkEmployees;
    private Long pkOrganization;
    private Long pkGroup;
    private String employeesCode;
    private String employeesName;

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
    @Column(name = "pk_employees")
    public Long getPkEmployees() {
        return pkEmployees;
    }

    public void setPkEmployees(Long pkEmployees) {
        this.pkEmployees = pkEmployees;
    }

    @Basic
    @Column(name = "pk_organization")
    public Long getPkOrganization() {
        return pkOrganization;
    }

    public void setPkOrganization(Long pkOrganization) {
        this.pkOrganization = pkOrganization;
    }

    @Basic
    @Column(name = "pk_group")
    public Long getPkGroup() {
        return pkGroup;
    }

    public void setPkGroup(Long pkGroup) {
        this.pkGroup = pkGroup;
    }

    @Basic
    @Column(name = "employees_code")
    public String getEmployeesCode() {
        return employeesCode;
    }

    public void setEmployeesCode(String employeesCode) {
        this.employeesCode = employeesCode;
    }

    @Basic
    @Column(name = "employees_name")
    public String getEmployeesName() {
        return employeesName;
    }

    public void setEmployeesName(String employeesName) {
        this.employeesName = employeesName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuperbootEmployees that = (SuperbootEmployees) o;
        return Objects.equals(ts, that.ts) &&
                Objects.equals(dr, that.dr) &&
                Objects.equals(createtime, that.createtime) &&
                Objects.equals(createby, that.createby) &&
                Objects.equals(lastmodifytime, that.lastmodifytime) &&
                Objects.equals(lastmodifyby, that.lastmodifyby) &&
                Objects.equals(pkEmployees, that.pkEmployees) &&
                Objects.equals(pkOrganization, that.pkOrganization) &&
                Objects.equals(pkGroup, that.pkGroup) &&
                Objects.equals(employeesCode, that.employeesCode) &&
                Objects.equals(employeesName, that.employeesName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ts, dr, createtime, createby, lastmodifytime, lastmodifyby, pkEmployees, pkOrganization, pkGroup, employeesCode, employeesName);
    }
}
