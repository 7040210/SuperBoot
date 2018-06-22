package org.superboot.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiaoleilu.hutool.util.ArrayUtil;
import com.xiaoleilu.hutool.util.StrUtil;

import java.lang.reflect.Field;
import java.util.*;

/**
 * <b> 树形节点构造器 </b>
 * <p>
 * 功能描述: 根据传入List对象动态构造树结构菜单
 * </p>
 */
public class TreeUtils {
    /**
     * 树节点 主键
     */
    private static final String TREE_ID = "ID";

    /**
     * 树节点 父节点主键
     */
    private static final String TREE_PARENTID = "parentId";

    /**
     * 树节点 节点编号
     */
    private static final String TREE_CODE = "menuCode";

    /**
     * 树节点 节点名称
     */
    private static final String TREE_NAME = "menuName";

    /**
     * 树节点 节点名称
     */
    private static final String TREE_CHILDREN = "children";


    /**
     * 生成树形节点
     * map包含3个字段 id 为节点主键 name 为节点名称 parentId 为上级节点主键
     *
     * @param dataList
     * @return
     */
    public static JSONObject genTree(List<HashMap<String, String>> dataList) {
        if (0 < dataList.size()) {
            // 节点列表（散列表，用于临时存储节点对象）
            HashMap nodeList = new HashMap();
            // 根节点
            Node root = null;
            // 根据结果集构造节点列表（存入散列表）
            for (Iterator it = dataList.iterator(); it.hasNext(); ) {
                Map dataRecord = (Map) it.next();
                Node node = new Node();
                node.id = "" + dataRecord.get("id");
                node.code = "" + dataRecord.get("code");
                node.name = "" + dataRecord.get("name");
                node.parentId = "" + dataRecord.get("parentId");
                nodeList.put(node.id, node);
            }
            // 构造无序的多叉树
            Set entrySet = nodeList.entrySet();
            for (Iterator it = entrySet.iterator(); it.hasNext(); ) {
                Node node = (Node) ((Map.Entry) it.next()).getValue();
                if (StrUtil.isBlank(node.parentId)) {
                    root = node;
                } else {
                    ((Node) nodeList.get(node.parentId)).addChild(node);
                }
            }
            root.sortChildren();
            return JSON.parseObject(root.toString());
        }

        return new JSONObject();
    }

    /**
     * 生成树形节点
     * 默认包含4个字段 id 为节点主键 menuName 为节点名称 menuCodewei parentId 为上级节点主键
     *
     * @param dataList
     * @return
     */
    public static JSONArray genTreeExtendChildren(List<TreeNode> dataList) {
        JSONArray res = new JSONArray();
        JSONObject tree = genTreeExtend(dataList);
        if (null != tree) {
            res = tree.getJSONArray(TREE_CHILDREN);
        }
        return res;
    }

    /**
     * 生成多根节点树
     *
     * @param dataList
     * @return
     */
    public static JSONArray genTreeExtendList(List<TreeNode> dataList) {
        JSONArray res = new JSONArray();
        List<TreeNode> treeNodes = new ArrayList<>();
        if (null != dataList && 0 < dataList.size()) {
            // 节点列表（散列表，用于临时存储节点对象）
            HashMap nodeList = new HashMap();
            // 根节点
            TreeNode root;
            // 根据结果集构造节点列表（存入散列表）
            for (TreeNode dataRecord : dataList) {
                if (nodeList.containsKey(dataRecord.getId())) {

                    if (nodeList.get(dataRecord.getId()) instanceof HashMap) {
                        ((HashMap) nodeList.get(dataRecord.getId())).put(dataRecord.getParentId(), dataRecord);
                    } else {
                        HashMap node = new HashMap(2);
                        node.put(dataRecord.getParentId(), dataRecord);
                        node.put(((TreeNode) nodeList.get(dataRecord.getId())).getParentId(), nodeList.get(dataRecord.getId()));
                        nodeList.put(dataRecord.getId(), node);
                    }
                } else {
                    nodeList.put(dataRecord.getId(), dataRecord);
                }
            }
            // 构造无序的多叉树
            Set entrySet = nodeList.entrySet();
            for (Iterator it = entrySet.iterator(); it.hasNext(); ) {
                Object record = ((Map.Entry) it.next()).getValue();
                //多父级子节点
                if (record instanceof HashMap) {
                    Set nodeSet = ((HashMap) record).entrySet();
                    for (Iterator nodeIt = nodeSet.iterator(); nodeIt.hasNext(); ) {
                        TreeNode reCordNode = (TreeNode) ((Map.Entry) nodeIt.next()).getValue();
                        if (StrUtil.isBlank(reCordNode.getParentId()) || null == nodeList.get(reCordNode.getParentId())) {
                            root = reCordNode;
                            treeNodes.add(root);
                        } else {
                            ((TreeNode) nodeList.get(reCordNode.getParentId())).addChild(reCordNode);
                        }
                    }
                    continue;
                }

                TreeNode node = (TreeNode) record;
                if (StrUtil.isBlank(node.getParentId())
                        || null == nodeList.get(node.getParentId())) {
                    root = node;
                    treeNodes.add(root);
                } else {
                    Object parentNode = nodeList.get(node.getParentId());
                    if (parentNode instanceof TreeNode) {
                        ((TreeNode) nodeList.get(node.getParentId())).addChild(node);
                    } else {
                        Set parentSet = ((HashMap) parentNode).entrySet();
                        for (Iterator parentIt = parentSet.iterator(); parentIt.hasNext(); ) {
                            TreeNode pNode = (TreeNode) ((Map.Entry) parentIt.next()).getValue();
                            pNode.addChild(node);
                        }
                    }
                }

            }
            for (TreeNode node : treeNodes) {
                node.sortChildren();
            }
            res = JSON.parseArray(treeNodes.toString());
        }
        return res;
    }

    /**
     * 生成树形节点
     * 默认包含4个字段 id 为节点主键 menuName 为节点名称 menuCodewei parentId 为上级节点主键
     *
     * @param dataList
     * @return
     */
    public static JSONObject genTreeExtend(List<TreeNode> dataList) {
        JSONObject res = new JSONObject();
        if (null != dataList && 0 < dataList.size()) {
            // 节点列表（散列表，用于临时存储节点对象）
            HashMap nodeList = new HashMap();
            // 根节点
            TreeNode root = null;
            // 根据结果集构造节点列表（存入散列表）
            for (TreeNode dataRecord : dataList) {
                nodeList.put(dataRecord.getId(), dataRecord);
            }
            // 构造无序的多叉树
            Set entrySet = nodeList.entrySet();
            for (Iterator it = entrySet.iterator(); it.hasNext(); ) {
                TreeNode node = (TreeNode) ((Map.Entry) it.next()).getValue();
                if (StrUtil.isBlank(node.getParentId()) || null == nodeList.get(node.getParentId())) {
                    root = node;
                } else {
                    ((TreeNode) nodeList.get(node.getParentId())).addChild(node);
                }
            }
            root.sortChildren();
            res = JSON.parseObject(root.toString());
        }
        return res;
    }

    /**
     * 生成单根节点树
     *
     * @param dataList
     * @return
     */
    public static JSONObject genSingleRootTreeByExtend(List<? extends TreeExtend> dataList) {
        return genTreeExtend(parseTreeExtend(dataList));
    }

    /**
     * 获取多根节点树形，取出唯一根节点的所有子节点作为根节点
     *
     * @param dataList
     * @return
     */
    public static JSONArray genMultiRootTreeByExtend(List<? extends TreeExtend> dataList) {
        return genTreeExtendChildren(parseTreeExtend(dataList));
    }

    /**
     * 生成多根节点树
     *
     * @param dataList
     * @return
     */
    public static JSONArray genListTreeByExtend(List<? extends TreeExtend> dataList) {
        return genTreeExtendList(parseTreeExtend(dataList));
    }

    /**
     * 解析树形扩展实体类为树节点实体类
     *
     * @param dataList 扩展信息实体类
     * @return
     */
    private static List<TreeNode> parseTreeExtend(List<? extends TreeExtend> dataList) {
        List<TreeNode> nodes = new ArrayList<>();
        for (TreeExtend treeExtend : dataList) {
            nodes.add(parseTreeExtend(treeExtend));
        }
        return nodes;
    }

    /**
     * 解析树形扩展实体类为树节点实体类
     *
     * @param dataList 扩展信息实体类
     * @return
     */
    private static TreeNode parseTreeExtend(TreeExtend dataList) {
        TreeNode node = new TreeNode();
        node.setExtend(dataList);
        //获取节点实体属性
        Field[] fields = dataList.getClass().getDeclaredFields();
        //获取节点实体父类属性
        Field[] superFields = dataList.getClass().getSuperclass().getDeclaredFields();
        fields = ArrayUtil.addAll(fields, superFields);
        for (Field f : fields) {
            f.setAccessible(true);
            //获取字段上树节点属性注解
            TreeProperty treeProp = f.getAnnotation(TreeProperty.class);
            if (null != treeProp) {
                try {
                    //根据注解设置树节点对应属性值
                    if (TreeField.ID.equals(treeProp.value())) {
                        node.setId(StringUtil.getString(f.get(dataList)));
                    } else if (TreeField.PARENTID.equals(treeProp.value())) {
                        node.setParentId(StringUtil.getString(f.get(dataList)));
                    } else if (TreeField.NAME.equals(treeProp.value())) {
                        node.setMenuName(StringUtil.getString(f.get(dataList)));
                    } else {
                        node.setMenuCode(StringUtil.getString(f.get(dataList)));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return node;
    }

    public static void main(String[] args) {
        List dataList = new ArrayList();

        HashMap dataRecord4 = new HashMap();
        dataRecord4.put("id", "113000");
        dataRecord4.put("code", "113000");
        dataRecord4.put("name", "廊坊银行开发区支行");
        dataRecord4.put("parentId", "110000");

        HashMap dataRecord5 = new HashMap();
        dataRecord5.put("id", "100000");
        dataRecord5.put("code", "100000");
        dataRecord5.put("name", "廊坊银行总行");
        dataRecord5.put("parentId", "");

        HashMap dataRecord6 = new HashMap();
        dataRecord6.put("id", "110000");
        dataRecord6.put("code", "110000");
        dataRecord6.put("name", "廊坊分行");
        dataRecord6.put("parentId", "100000");

        HashMap dataRecord7 = new HashMap();
        dataRecord7.put("id", "111000");
        dataRecord7.put("code", "111000");
        dataRecord7.put("name", "廊坊银行金光道支行");
        dataRecord7.put("parentId", "110000");


        HashMap dataRecord1 = new HashMap();
        dataRecord1.put("id", "112000");
        dataRecord1.put("code", "112000");
        dataRecord1.put("name", "廊坊银行解放道支行");
        dataRecord1.put("parentId", "110000");

        HashMap dataRecord2 = new HashMap();
        dataRecord2.put("id", "112200");
        dataRecord2.put("code", "112200");
        dataRecord2.put("name", "廊坊银行三大街支行");
        dataRecord2.put("parentId", "112000");

        HashMap dataRecord3 = new HashMap();
        dataRecord3.put("id", "112100");
        dataRecord3.put("code", "112100");
        dataRecord3.put("name", "廊坊银行广阳道支行");
        dataRecord3.put("parentId", "112000");


        HashMap dataRecord8 = new HashMap();
        dataRecord8.put("id", "112210");
        dataRecord8.put("name", "廊坊银行三大街支行");
        dataRecord8.put("parentId", "112200");

        dataList.add(dataRecord1);
        dataList.add(dataRecord2);
        dataList.add(dataRecord3);
        dataList.add(dataRecord4);
        dataList.add(dataRecord5);
        dataList.add(dataRecord6);
        dataList.add(dataRecord7);
        dataList.add(dataRecord8);


//        System.out.println(genTree(dataList));
        List<TreeNode> nodes = new ArrayList<>();
        for (Object map : dataList) {
            TreeNode t = new TreeNode();
            nodes.add(t);
            t.setId(((HashMap) map).get("id").toString());
            t.setParentId(((HashMap) map).get("parentId").toString());
            t.setMenuName(((HashMap) map).get("name").toString());
            t.setMenuCode(StringUtil.getString(((HashMap) map).get("code")));
        }
//        System.out.println(genTreeExtendList(nodes));

    }
}


