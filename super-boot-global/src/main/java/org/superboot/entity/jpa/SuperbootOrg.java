package org.superboot.entity.jpa;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <b>  </b>
 * <p>
 * 功能描述:
 * </p>
 *
 */
@Entity
@Table(name = "superboot_org", schema = "superboot", catalog = "")
public class SuperbootOrg {
    private Timestamp ts;
    private Integer dr;
    private Timestamp createtime;
    private Long createby;
    private Timestamp lastmodifytime;
    private Long lastmodifyby;
    private Long pkOrg;
    private String orgCode;
    private String orgName;
    private Integer nodetype;
    private Long pkFOrg;
    private Long pkGroup;
    private Integer orgLev;
    private String isEnd;
    private String orderCode;

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
    @Column(name = "pk_org")
    public Long getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(Long pkOrg) {
        this.pkOrg = pkOrg;
    }

    @Basic
    @Column(name = "org_code")
    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    @Basic
    @Column(name = "org_name")
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Basic
    @Column(name = "nodetype")
    public Integer getNodetype() {
        return nodetype;
    }

    public void setNodetype(Integer nodetype) {
        this.nodetype = nodetype;
    }

    @Basic
    @Column(name = "pk_f_org")
    public Long getPkFOrg() {
        return pkFOrg;
    }

    public void setPkFOrg(Long pkFOrg) {
        this.pkFOrg = pkFOrg;
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
    @Column(name = "org_lev")
    public Integer getOrgLev() {
        return orgLev;
    }

    public void setOrgLev(Integer orgLev) {
        this.orgLev = orgLev;
    }

    @Basic
    @Column(name = "is_end")
    public String getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(String isEnd) {
        this.isEnd = isEnd;
    }

    @Basic
    @Column(name = "order_code")
    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuperbootOrg that = (SuperbootOrg) o;
        return Objects.equals(ts, that.ts) &&
                Objects.equals(dr, that.dr) &&
                Objects.equals(createtime, that.createtime) &&
                Objects.equals(createby, that.createby) &&
                Objects.equals(lastmodifytime, that.lastmodifytime) &&
                Objects.equals(lastmodifyby, that.lastmodifyby) &&
                Objects.equals(pkOrg, that.pkOrg) &&
                Objects.equals(orgCode, that.orgCode) &&
                Objects.equals(orgName, that.orgName) &&
                Objects.equals(nodetype, that.nodetype) &&
                Objects.equals(pkFOrg, that.pkFOrg) &&
                Objects.equals(pkGroup, that.pkGroup) &&
                Objects.equals(orgLev, that.orgLev) &&
                Objects.equals(isEnd, that.isEnd) &&
                Objects.equals(orderCode, that.orderCode);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ts, dr, createtime, createby, lastmodifytime, lastmodifyby, pkOrg, orgCode, orgName, nodetype, pkFOrg, pkGroup, orgLev, isEnd, orderCode);
    }
}
