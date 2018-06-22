package org.superboot.utils;

/**
 * <b> TreeField </b>
 * <p>
 * 功能描述:树节点核心字段枚举类，用于生成树结构的实体字段，将被注解的字段映射成指定节点属性
 * </p>
 */
public enum TreeField {

    //节点主键
    ID {
        {
            this.value = "id";
        }
    },
    //父节点主键
    PARENTID {
        {
            this.value = "parentId";
        }
    },
    //节点名称
    NAME {
        {
            this.value = "name";
        }
    },
    //节点编号
    CODE {
        {
            this.value = "code";
        }
    };
    String value;

    public String getValue() {
        return value;
    }
}
