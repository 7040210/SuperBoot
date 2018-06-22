package org.superboot.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <b> 树节点的子节点 </b>
 * <p>
 * 功能描述:
 * </p>
 */
public class Children {

    private List list = new ArrayList();

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
    public void addChild(Node node) {
        list.add(node);
    }

    /**
     * 节点排序
     */
    public void sortChildren() {
        // 对本层节点进行排序
        // 可根据不同的排序属性，传入不同的比较器，这里传入ID比较器
        Collections.sort(list, new NodeIDComparator());
        // 对每个节点的下一层节点进行排序
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            ((Node) it.next()).sortChildren();
        }
    }

    /**
     * 拼接子节点的JSON字符串
     *
     * @return
     */
    @Override
    public String toString() {
        String result = "[";
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            result += ((Node) it.next()).toString();
            result += ",";
        }
        result = result.substring(0, result.length() - 1);
        result += "]";
        return result;
    }
}
