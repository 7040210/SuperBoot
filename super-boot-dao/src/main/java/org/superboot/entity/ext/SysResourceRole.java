package org.superboot.entity.ext;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="s_resource_role")
public class SysResourceRole {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column (name="id",length=10)
    private int id;

    @Column(name="roleId",length=50)
    private String roleId; //角色ID

    @Column(name="resourceId",length=50)
    private String resourceId;//资源ID

    @Column(name="updateTime")
    private Date updateTime;//更新时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


}