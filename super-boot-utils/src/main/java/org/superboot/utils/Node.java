package org.superboot.utils;

/**
 * <b> 树节点 </b>
 * <p>
 * 功能描述:
 * </p>
 */
public class Node {

    /**
     * 节点主键
     */
    public String id;
    /**
     * 节点编码
     */
    public String code;
    /**
     * 节点内容
     */
    public String name;
    /**
     * 父节点主键
     */
    public String parentId;

    /**
     * 孩子节点列表
     */
    private Children children = new Children();


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
    public void addChild(Node node) {
        this.children.addChild(node);
    }


    /**
     * 先序遍历，拼接JSON字符串
     *
     * @return
     */
    @Override
    public String toString() {
        String result = "{"
                + "'id' : '" + id + "'"
                + ", 'code' : '" + code + "'"
                + ", 'name' : '" + name + "'"
                + ", 'parentId' : '" + parentId + "'";

        if (children != null && children.getSize() != 0) {
            result += ", 'children' : " + children.toString();
        } else {
            result += ", ";
        }

        return result + "}";
    }
}
