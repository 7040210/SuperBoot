package org.superboot.utils;

import java.util.Comparator;

/**
 * <b> 排序工具类 </b>
 * <p>
 * 功能描述:
 * </p>
 */
public class NodeIDComparator implements Comparator {
    /**
     * 按照节点编号比较
     *
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(Object o1, Object o2) {
        long j1 = Long.parseLong(((Node) o1).id);
        long j2 = Long.parseLong(((Node) o2).id);
        return (j1 < j2 ? -1 : (j1 == j2 ? 0 : 1));
    }
}
