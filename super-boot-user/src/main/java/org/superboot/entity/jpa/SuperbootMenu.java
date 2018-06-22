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
@Table(name = "superboot_menu", schema = "superboot", catalog = "")
public class SuperbootMenu {
    private Timestamp ts;
    private Integer dr;
    private Timestamp createtime;
    private Long createby;
    private Timestamp lastmodifytime;
    private Long lastmodifyby;
    private Long pkMenu;
    private String menuCode;
    private String menuName;
    private Integer menuType;
    private Long pkFMenu;
    private String orderCode;
    private String menuUrl;
    private String isEnd;
    private Integer menuLev;
    private String menuIco;

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
    @Column(name = "pk_menu")
    public Long getPkMenu() {
        return pkMenu;
    }

    public void setPkMenu(Long pkMenu) {
        this.pkMenu = pkMenu;
    }

    @Basic
    @Column(name = "menu_code")
    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    @Basic
    @Column(name = "menu_name")
    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    @Basic
    @Column(name = "menu_type")
    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    @Basic
    @Column(name = "pk_f_menu")
    public Long getPkFMenu() {
        return pkFMenu;
    }

    public void setPkFMenu(Long pkFMenu) {
        this.pkFMenu = pkFMenu;
    }

    @Basic
    @Column(name = "order_code")
    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    @Basic
    @Column(name = "menu_url")
    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
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
    @Column(name = "menu_lev")
    public Integer getMenuLev() {
        return menuLev;
    }

    public void setMenuLev(Integer menuLev) {
        this.menuLev = menuLev;
    }

    @Basic
    @Column(name = "menu_ico")
    public String getMenuIco() {
        return menuIco;
    }

    public void setMenuIco(String menuIco) {
        this.menuIco = menuIco;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuperbootMenu that = (SuperbootMenu) o;
        return Objects.equals(ts, that.ts) &&
                Objects.equals(dr, that.dr) &&
                Objects.equals(createtime, that.createtime) &&
                Objects.equals(createby, that.createby) &&
                Objects.equals(lastmodifytime, that.lastmodifytime) &&
                Objects.equals(lastmodifyby, that.lastmodifyby) &&
                Objects.equals(pkMenu, that.pkMenu) &&
                Objects.equals(menuCode, that.menuCode) &&
                Objects.equals(menuName, that.menuName) &&
                Objects.equals(menuType, that.menuType) &&
                Objects.equals(pkFMenu, that.pkFMenu) &&
                Objects.equals(orderCode, that.orderCode) &&
                Objects.equals(menuUrl, that.menuUrl) &&
                Objects.equals(isEnd, that.isEnd) &&
                Objects.equals(menuLev, that.menuLev) &&
                Objects.equals(menuIco, that.menuIco);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ts, dr, createtime, createby, lastmodifytime, lastmodifyby, pkMenu, menuCode, menuName, menuType, pkFMenu, orderCode, menuUrl, isEnd, menuLev, menuIco);
    }
}
