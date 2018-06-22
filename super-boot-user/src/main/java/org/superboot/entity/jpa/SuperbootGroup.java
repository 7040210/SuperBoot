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
@Table(name = "superboot_group", schema = "superboot", catalog = "")
public class SuperbootGroup {
    private Timestamp ts;
    private Integer dr;
    private Timestamp createtime;
    private Long createby;
    private Timestamp lastmodifytime;
    private Long lastmodifyby;
    private Long pkGroup;
    private String groupCode;
    private String groupName;
    private Integer groupType;

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
    @Column(name = "pk_group")
    public Long getPkGroup() {
        return pkGroup;
    }

    public void setPkGroup(Long pkGroup) {
        this.pkGroup = pkGroup;
    }

    @Basic
    @Column(name = "group_code")
    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    @Basic
    @Column(name = "group_name")
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Basic
    @Column(name = "group_type")
    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuperbootGroup that = (SuperbootGroup) o;
        return Objects.equals(ts, that.ts) &&
                Objects.equals(dr, that.dr) &&
                Objects.equals(createtime, that.createtime) &&
                Objects.equals(createby, that.createby) &&
                Objects.equals(lastmodifytime, that.lastmodifytime) &&
                Objects.equals(lastmodifyby, that.lastmodifyby) &&
                Objects.equals(pkGroup, that.pkGroup) &&
                Objects.equals(groupCode, that.groupCode) &&
                Objects.equals(groupName, that.groupName) &&
                Objects.equals(groupType, that.groupType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ts, dr, createtime, createby, lastmodifytime, lastmodifyby, pkGroup, groupCode, groupName, groupType);
    }
}
