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
@Table(name = "superboot_duties", schema = "superboot", catalog = "")
public class SuperbootDuties {
    private Timestamp ts;
    private Integer dr;
    private Timestamp createtime;
    private Long createby;
    private Timestamp lastmodifytime;
    private Long lastmodifyby;
    private Long pkDuties;
    private String dutiesCode;
    private String dutiesName;
    private Integer dutiesType;
    private Long pkGroup;

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
    @Column(name = "pk_duties")
    public Long getPkDuties() {
        return pkDuties;
    }

    public void setPkDuties(Long pkDuties) {
        this.pkDuties = pkDuties;
    }

    @Basic
    @Column(name = "duties_code")
    public String getDutiesCode() {
        return dutiesCode;
    }

    public void setDutiesCode(String dutiesCode) {
        this.dutiesCode = dutiesCode;
    }

    @Basic
    @Column(name = "duties_name")
    public String getDutiesName() {
        return dutiesName;
    }

    public void setDutiesName(String dutiesName) {
        this.dutiesName = dutiesName;
    }

    @Basic
    @Column(name = "duties_type")
    public Integer getDutiesType() {
        return dutiesType;
    }

    public void setDutiesType(Integer dutiesType) {
        this.dutiesType = dutiesType;
    }

    @Basic
    @Column(name = "pk_group")
    public Long getPkGroup() {
        return pkGroup;
    }

    public void setPkGroup(Long pkGroup) {
        this.pkGroup = pkGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuperbootDuties that = (SuperbootDuties) o;
        return Objects.equals(ts, that.ts) &&
                Objects.equals(dr, that.dr) &&
                Objects.equals(createtime, that.createtime) &&
                Objects.equals(createby, that.createby) &&
                Objects.equals(lastmodifytime, that.lastmodifytime) &&
                Objects.equals(lastmodifyby, that.lastmodifyby) &&
                Objects.equals(pkDuties, that.pkDuties) &&
                Objects.equals(dutiesCode, that.dutiesCode) &&
                Objects.equals(dutiesName, that.dutiesName) &&
                Objects.equals(dutiesType, that.dutiesType) &&
                Objects.equals(pkGroup, that.pkGroup);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ts, dr, createtime, createby, lastmodifytime, lastmodifyby, pkDuties, dutiesCode, dutiesName, dutiesType, pkGroup);
    }
}
