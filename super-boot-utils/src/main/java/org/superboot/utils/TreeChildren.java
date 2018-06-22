package org.superboot.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.*;

/**
 * <b> TreeChildren </b>
 * <p>
 * 功能描述:树形结构 子节点
 * </p>
 */
@Data
public class TreeChildren implements Comparator, Serializable {
    private List<TreeNode> list = new ArrayList();

    /**
     * 获取节点数量
     *
     * @return
     */
    public int getSize() {
        return list.size();
    }

    /**
     * 添加子节点
     *
     * @param node
     */
    public void addChild(TreeNode node) {
        list.add(node);
    }

    /**
     * 节点排序
     */
    public void sortChildren() {
        // 对本层节点进行排序
        // 可根据不同的排序属性，传入不同的比较器，这里传入ID比较器
        Collections.sort(list, this);
        // 对每个节点的下一层节点进行排序
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            ((TreeNode) it.next()).sortChildren();
        }
    }

    /**
     * 按照节点编号比较
     *
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(Object o1, Object o2) {
        String n1 = PinYinUtils.getFullPinYin(((TreeNode) o1).getMenuName());
        String n2 = PinYinUtils.getFullPinYin(((TreeNode) o2).getMenuName());
        n1.compareTo(n2);
        return n1.compareTo(n2);
    }

    /**
     * 拼接子节点的JSON字符串
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            result.append(((TreeNode) it.next()).toString()).append(",");
        }
        if (result.lastIndexOf(",") == result.length() - 1) {
            result.setLength(result.length() - 1);
        }
        result.append("]");
        return result.toString();
    }
}
