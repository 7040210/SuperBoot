package org.superboot.utils;

import java.lang.reflect.Field;

/**
 * <b> 树结构扩展信息 </b>
 * <p>
 * 功能描述:树形结构 扩展信息
 * </p>
 */
public class TreeExtend {
    /**
     * 格式化扩展属性
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        Field[] fs = this.getClass().getDeclaredFields();
        String strSymbol = "\"";
        try {
            for (Field f : fs) {
                //如果扩展属性与树节点默认属性重名，则自动跳过，由树节点产生该属性信息
                TreeProperty tf = f.getAnnotation(TreeProperty.class);
                if (null != tf && tf.value().getValue().equals(f.getName())) {
                    continue;
                }
                f.setAccessible(true);
                //特殊字符处理
                res.append(strSymbol).append(f.getName()).append(strSymbol).append(":")
                        .append(strSymbol).append(StringUtil.getString(f.get(this)).replace("\"", "“")).append(strSymbol).append(",");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res.toString();
    }
}
