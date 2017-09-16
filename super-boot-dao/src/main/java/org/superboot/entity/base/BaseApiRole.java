package org.superboot.entity.base;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * <b>  </b>
 * <p>
 * 功能描述:
 * </p>
 *
 * @author jesion
 * @date 2017/9/15
 * @time 19:12
 * @Path org.superboot.entity.base.BaseApiRole
 */
@Entity
@Table(name = "base_api_role", schema = "super_boot_base", catalog = "")
public class BaseApiRole {
    private Timestamp ts;
    private Integer dr;
    private Long pkApiRole;
    private Long pkApi;
    private Long pkRole;

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
    @Column(name = "pk_api_role", nullable = false)
    public Long getPkApiRole() {
        return pkApiRole;
    }

    public void setPkApiRole(Long pkApiRole) {
        this.pkApiRole = pkApiRole;
    }

    @Basic
    @Column(name = "pk_api", nullable = true)
    public Long getPkApi() {
        return pkApi;
    }

    public void setPkApi(Long pkApi) {
        this.pkApi = pkApi;
    }

    @Basic
    @Column(name = "pk_role", nullable = true)
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

        BaseApiRole that = (BaseApiRole) o;

        if (ts != null ? !ts.equals(that.ts) : that.ts != null) return false;
        if (dr != null ? !dr.equals(that.dr) : that.dr != null) return false;
        if (pkApiRole != null ? !pkApiRole.equals(that.pkApiRole) : that.pkApiRole != null) return false;
        if (pkApi != null ? !pkApi.equals(that.pkApi) : that.pkApi != null) return false;
        if (pkRole != null ? !pkRole.equals(that.pkRole) : that.pkRole != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ts != null ? ts.hashCode() : 0;
        result = 31 * result + (dr != null ? dr.hashCode() : 0);
        result = 31 * result + (pkApiRole != null ? pkApiRole.hashCode() : 0);
        result = 31 * result + (pkApi != null ? pkApi.hashCode() : 0);
        result = 31 * result + (pkRole != null ? pkRole.hashCode() : 0);
        return result;
    }
}
