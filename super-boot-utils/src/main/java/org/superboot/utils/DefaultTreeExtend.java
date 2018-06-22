package org.superboot.utils;

import lombok.Data;

/**
 * <b> 树结构默认扩展实体 </b>
 * <p>
 * 功能描述:提供默认的树节点扩展实体，自定义的树节点扩展实体继承该实体即可实现树节点属性扩展，如果自定义实体使用@Data注解，需重写toString方法
 * </p>
 */
@Data
public class DefaultTreeExtend extends TreeExtend {

    @TreeProperty(TreeField.ID)
    protected String id;

    @TreeProperty(TreeField.PARENTID)
    protected String parentId;

    @TreeProperty(TreeField.NAME)
    protected String name;

    @TreeProperty(TreeField.CODE)
    protected String code;

    @Override
    public String toString() {
        return super.toString();
    }
}
