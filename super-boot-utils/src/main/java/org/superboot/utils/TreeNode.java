package org.superboot.utils;

import lombok.Data;

import java.lang.reflect.Field;

/**
 * <b> TreeNode </b>
 * <p>
 * 功能描述:树形结构 节点
 * </p>
 */
@Data
public class TreeNode {
    /**
     * 节点主键
     */
    private String id;
    /**
     * 节点编码
     */
    private String menuCode;
    /**
     * 节点内容
     */
    private String menuName;
    /**
     * 父节点主键
     */
    private String parentId;

    private TreeChildren children = new TreeChildren();
    /**
     * 扩展信息
     */
    private TreeExtend extend;

    /**
     * 兄弟节点横向排序
     */
    public void sortChildren() {
        if (children != null && children.getSize() != 0) {
            children.sortChildren();
        }
    }

    /**
     * 添加子节点
     *
     * @param node
     */
    public void addChild(TreeNode node) {
        this.children.addChild(node);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("{");
        String strSymbol = "\"";
        Field[] fs = this.getClass().getDeclaredFields();
        try {
            for (Field f : fs) {
                f.setAccessible(true);
                //扩展属性
                if ("extend".equals(f.getName())) {
                    res.append(StringUtil.getString(f.get(this)));
                    continue;
                }
                if (!"children".equals(f.getName())) {
                    res.append(strSymbol).append(f.getName()).append(strSymbol).append(":")
                            .append(strSymbol).append(StringUtil.getString(f.get(this))).append(strSymbol).append(",");
                }
            }
            res.append(strSymbol).append("children").append(strSymbol).append(":").append(children.toString()).append("}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res.toString();
    }

}
