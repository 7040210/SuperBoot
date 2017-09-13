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
 * @date 2017/9/10
 * @time 12:30
 * @Path org.superboot.entity.base.sys.SysApi
 */
@Entity
@Table(name = "sys_api", schema = "superboot", catalog = "")
public class SysApi {
    private Timestamp ts;
    private Integer dr;
    private Long pkApi;
    private String moduleName;
    private String apiName;
    private String methodName;
    private String methodPath;
    private String url;
    private String remark;
    private String modulePath;

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
    @Column(name = "pk_api", nullable = false)
    public Long getPkApi() {
        return pkApi;
    }

    public void setPkApi(Long pkApi) {
        this.pkApi = pkApi;
    }

    @Basic
    @Column(name = "module_name", nullable = true, length = 200)
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    @Basic
    @Column(name = "api_name", nullable = true, length = 500)
    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    @Basic
    @Column(name = "method_name", nullable = true, length = 500)
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Basic
    @Column(name = "method_path", nullable = true, length = 1000)
    public String getMethodPath() {
        return methodPath;
    }

    public void setMethodPath(String methodPath) {
        this.methodPath = methodPath;
    }

    @Basic
    @Column(name = "url", nullable = true, length = 1000)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "remark", nullable = true, length = 200)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SysApi sysApi = (SysApi) o;

        if (ts != null ? !ts.equals(sysApi.ts) : sysApi.ts != null) return false;
        if (dr != null ? !dr.equals(sysApi.dr) : sysApi.dr != null) return false;
        if (pkApi != null ? !pkApi.equals(sysApi.pkApi) : sysApi.pkApi != null) return false;
        if (moduleName != null ? !moduleName.equals(sysApi.moduleName) : sysApi.moduleName != null) return false;
        if (apiName != null ? !apiName.equals(sysApi.apiName) : sysApi.apiName != null) return false;
        if (methodName != null ? !methodName.equals(sysApi.methodName) : sysApi.methodName != null) return false;
        if (methodPath != null ? !methodPath.equals(sysApi.methodPath) : sysApi.methodPath != null) return false;
        if (url != null ? !url.equals(sysApi.url) : sysApi.url != null) return false;
        if (remark != null ? !remark.equals(sysApi.remark) : sysApi.remark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ts != null ? ts.hashCode() : 0;
        result = 31 * result + (dr != null ? dr.hashCode() : 0);
        result = 31 * result + (pkApi != null ? pkApi.hashCode() : 0);
        result = 31 * result + (moduleName != null ? moduleName.hashCode() : 0);
        result = 31 * result + (apiName != null ? apiName.hashCode() : 0);
        result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
        result = 31 * result + (methodPath != null ? methodPath.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "module_path", nullable = true, length = 200)
    public String getModulePath() {
        return modulePath;
    }

    public void setModulePath(String modulePath) {
        this.modulePath = modulePath;
    }
}
