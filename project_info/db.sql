/*
Navicat MySQL Data Transfer

Source Server         : 本机
Source Server Version : 50631
Source Host           : localhost:3306
Source Database       : superboot

Target Server Type    : MYSQL
Target Server Version : 50631
File Encoding         : 65001

Date: 2018-06-22 14:35:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for superboot_authorization
-- ----------------------------
DROP TABLE IF EXISTS `superboot_authorization`;
CREATE TABLE `superboot_authorization` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 0为正常 1为删除 2为封存',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `createby` bigint(18) DEFAULT NULL COMMENT '创建人',
  `lastmodifytime` datetime DEFAULT NULL COMMENT '最后修改时间',
  `lastmodifyby` bigint(18) DEFAULT NULL COMMENT '最后修改人',
  `pk_authorization` bigint(18) NOT NULL COMMENT '授权主键',
  `access_key` varchar(20) DEFAULT NULL COMMENT 'access_key',
  `secret_key` varchar(20) DEFAULT NULL COMMENT 'secret_key',
  `auth_end_date` char(10) DEFAULT NULL COMMENT '授权截止日期',
  `pk_group` bigint(18) NOT NULL COMMENT '授权组织',
  PRIMARY KEY (`pk_authorization`),
  KEY `pk_group` (`pk_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='授权管理，主要用户第三方调用系统服务使用';

-- ----------------------------
-- Records of superboot_authorization
-- ----------------------------
INSERT INTO `superboot_authorization` VALUES ('2018-03-30 17:31:00', '0', '2018-03-30 17:31:00', '377457784972640256', '2018-06-22 14:25:43', '377457784972640256', '429331724502040576', '5T83yoqbLQ6NjrgbBI0n', 'aq6BPCzBPMqwVWVyQi3I', '', '378181361514577920');

-- ----------------------------
-- Table structure for superboot_duties
-- ----------------------------
DROP TABLE IF EXISTS `superboot_duties`;
CREATE TABLE `superboot_duties` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 0为正常 1为删除 2为封存',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `createby` bigint(18) DEFAULT NULL COMMENT '创建人',
  `lastmodifytime` datetime DEFAULT NULL COMMENT '最后修改时间',
  `lastmodifyby` bigint(18) DEFAULT NULL COMMENT '最后修改人',
  `pk_duties` bigint(18) NOT NULL COMMENT '职务主键',
  `duties_code` varchar(50) DEFAULT NULL COMMENT '职务编码',
  `duties_name` varchar(100) DEFAULT NULL COMMENT '职务名称',
  `duties_type` int(11) DEFAULT NULL COMMENT '类型 0 系统自建 1 组织自建',
  `pk_group` bigint(18) NOT NULL COMMENT '组织主键',
  PRIMARY KEY (`pk_duties`),
  KEY `pk_group` (`pk_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='职务信息表';

-- ----------------------------
-- Records of superboot_duties
-- ----------------------------

-- ----------------------------
-- Table structure for superboot_employees
-- ----------------------------
DROP TABLE IF EXISTS `superboot_employees`;
CREATE TABLE `superboot_employees` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 0为正常 1为删除 2为封存',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `createby` bigint(18) DEFAULT NULL COMMENT '创建人',
  `lastmodifytime` datetime DEFAULT NULL COMMENT '最后修改时间',
  `lastmodifyby` bigint(18) DEFAULT NULL COMMENT '最后修改人',
  `pk_employees` bigint(18) NOT NULL COMMENT '人员主键',
  `pk_organization` bigint(18) DEFAULT NULL COMMENT '机构主键',
  `pk_group` bigint(18) DEFAULT NULL COMMENT '组织主键',
  `employees_code` varchar(50) DEFAULT NULL COMMENT '工号',
  `employees_name` varchar(50) DEFAULT NULL COMMENT '姓名',
  PRIMARY KEY (`pk_employees`),
  KEY `pk_group` (`pk_group`),
  KEY `pk_organization` (`pk_organization`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人员信息表';

-- ----------------------------
-- Records of superboot_employees
-- ----------------------------

-- ----------------------------
-- Table structure for superboot_employees_duties
-- ----------------------------
DROP TABLE IF EXISTS `superboot_employees_duties`;
CREATE TABLE `superboot_employees_duties` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 0为正常 1为删除 2为封存',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `createby` bigint(18) DEFAULT NULL COMMENT '创建人',
  `lastmodifytime` datetime DEFAULT NULL COMMENT '最后修改时间',
  `lastmodifyby` bigint(18) DEFAULT NULL COMMENT '最后修改人',
  `pk_employees_duties` bigint(18) NOT NULL COMMENT '主键',
  `pk_employees` bigint(18) NOT NULL COMMENT '人员主键',
  `pk_duties` bigint(18) NOT NULL COMMENT '职务主键',
  PRIMARY KEY (`pk_employees_duties`),
  KEY `pk_duties` (`pk_duties`),
  KEY `pk_employees` (`pk_employees`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人员职务信息表';

-- ----------------------------
-- Records of superboot_employees_duties
-- ----------------------------

-- ----------------------------
-- Table structure for superboot_group
-- ----------------------------
DROP TABLE IF EXISTS `superboot_group`;
CREATE TABLE `superboot_group` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 0为正常 1为删除 2为封存',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `createby` bigint(18) DEFAULT NULL COMMENT '创建人',
  `lastmodifytime` datetime DEFAULT NULL COMMENT '最后修改时间',
  `lastmodifyby` bigint(18) DEFAULT NULL COMMENT '最后修改人',
  `pk_group` bigint(18) NOT NULL COMMENT '组织主键',
  `group_code` varchar(32) DEFAULT NULL COMMENT '组织编码',
  `group_name` varchar(100) DEFAULT NULL COMMENT '组织名称',
  `group_type` int(11) DEFAULT '2' COMMENT '组织类型（0为 自建、1为授权、2为其他等 默认为2）',
  PRIMARY KEY (`pk_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织信息表';

-- ----------------------------
-- Records of superboot_group
-- ----------------------------
INSERT INTO `superboot_group` VALUES ('2018-01-04 10:46:54', '0', '2018-01-04 10:47:03', '377457784972640256', '2018-06-21 14:40:25', null, '-1', 'sys', '平台', '0');
INSERT INTO `superboot_group` VALUES ('2018-02-08 14:02:56', '0', '2017-11-09 13:57:43', '377457784972640256', '2018-02-08 14:02:56', '393717664398180352', '378181361514577920', 'demo', '演示组织', '0');
INSERT INTO `superboot_group` VALUES ('2017-11-09 14:24:29', '0', '2017-11-09 14:24:29', '377457784972640256', '2018-06-21 14:41:30', null, '378188098061729792', 'pub', '公开组织', '2');

-- ----------------------------
-- Table structure for superboot_menu
-- ----------------------------
DROP TABLE IF EXISTS `superboot_menu`;
CREATE TABLE `superboot_menu` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 0为正常 1为删除 2为封存',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `createby` bigint(18) DEFAULT NULL COMMENT '创建人',
  `lastmodifytime` datetime DEFAULT NULL COMMENT '最后修改时间',
  `lastmodifyby` bigint(18) DEFAULT NULL COMMENT '最后修改人',
  `pk_menu` bigint(18) NOT NULL COMMENT '菜单主键',
  `menu_code` varchar(50) NOT NULL COMMENT '菜单编码',
  `menu_name` varchar(100) NOT NULL COMMENT '菜单名称',
  `menu_type` int(11) NOT NULL COMMENT '菜单类型 0为后台系统菜单 1为前台业务菜单',
  `pk_f_menu` bigint(18) DEFAULT NULL COMMENT '上级菜单主键',
  `order_code` varchar(30) DEFAULT NULL COMMENT '内部序号 自动生成用于排序使用',
  `menu_url` varchar(500) DEFAULT NULL COMMENT '访问URL',
  `is_end` char(1) DEFAULT 'N' COMMENT '是否末级 Y为末级 N或者空为非末级 默认为N',
  `menu_lev` int(11) DEFAULT NULL COMMENT '菜单级次',
  `menu_ico` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  PRIMARY KEY (`pk_menu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单信息表';

-- ----------------------------
-- Records of superboot_menu
-- ----------------------------
INSERT INTO `superboot_menu` VALUES ('2018-04-03 20:40:04', '0', '2017-11-09 19:17:20', '377457784972640256', '2018-04-03 20:40:04', '402395440823140352', '378261796122787840', '010', '系统管理', '0', '0', '010', '/sys', 'N', '1', 'settings');
INSERT INTO `superboot_menu` VALUES ('2017-11-09 19:20:33', '0', '2017-11-09 19:20:33', '377457784972640256', '2017-11-09 19:21:22', '377457784972640256', '378262605225000960', '010020', '菜单管理', '0', '378261796122787840', '010020', '/sys/menu', 'N', '2', 'menu');
INSERT INTO `superboot_menu` VALUES ('2018-04-02 14:41:10', '0', '2017-11-09 19:21:22', '377457784972640256', '2018-04-02 14:41:10', '393717664398180352', '378262812222291968', '010020010', '菜单配置', '0', '378262605225000960', '010020010', '/sys/menu/conf', 'Y', '3', 'build');
INSERT INTO `superboot_menu` VALUES ('2017-12-21 20:41:39', '0', '2017-12-21 20:42:25', '377457784972640256', '2017-12-21 20:44:19', '377457784972640256', '378263121581572097', '010030', '角色管理', '0', '378261796122787840', '010030', '/sys/role', 'N', '2', 'person');
INSERT INTO `superboot_menu` VALUES ('2018-04-02 14:41:21', '0', '2017-11-09 19:22:58', '377457784972640256', '2018-04-02 14:41:21', '393717664398180352', '378263215357820928', '010040', '用户管理', '0', '378261796122787840', '010040', '/sys/user', 'Y', '2', 'group_add');
INSERT INTO `superboot_menu` VALUES ('2018-04-02 14:41:27', '0', '2017-11-09 19:23:35', '377457784972640256', '2018-04-02 14:41:27', '393717664398180352', '378263367338426368', '010050', '组织管理', '0', '378261796122787840', '010050', '/sys/group', 'Y', '2', 'group');
INSERT INTO `superboot_menu` VALUES ('2017-11-09 19:23:56', '0', '2017-11-09 19:23:56', '377457784972640256', '2017-11-09 19:24:31', '377457784972640256', '378263458849751040', '010999', '系统信息', '0', '378261796122787840', '010999', '/sys/info', 'N', '2', 'personal_video');
INSERT INTO `superboot_menu` VALUES ('2018-04-02 14:41:53', '0', '2017-11-09 19:25:07', '377457784972640256', '2018-04-02 14:41:53', '393717664398180352', '378263753197617152', '010999010', '系统日志', '0', '378263458849751040', '010999010', '/sys/info/log', 'Y', '3', 'event_available');
INSERT INTO `superboot_menu` VALUES ('2018-04-02 14:41:59', '0', '2017-11-09 19:25:27', '377457784972640256', '2018-04-02 14:41:59', '393717664398180352', '378263837368909824', '010999020', '异常日志', '0', '378263458849751040', '010999020', '/sys/info/error', 'Y', '3', 'event_busy');
INSERT INTO `superboot_menu` VALUES ('2018-04-02 14:41:15', '0', '2017-11-13 17:56:46', '377457784972640256', '2018-04-02 14:41:15', '393717664398180352', '379691072353206272', '010030010', '角色配置', '0', '378263121581572097', '010030010', '/sys/role/conf', 'Y', '3', 'person_add');

-- ----------------------------
-- Table structure for superboot_menu_permissions
-- ----------------------------
DROP TABLE IF EXISTS `superboot_menu_permissions`;
CREATE TABLE `superboot_menu_permissions` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 0为正常 1为删除 2为封存',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `createby` bigint(18) DEFAULT NULL COMMENT '创建人',
  `lastmodifytime` datetime DEFAULT NULL COMMENT '最后修改时间',
  `lastmodifyby` bigint(18) DEFAULT NULL COMMENT '最后修改人',
  `pk_menu_permissions` bigint(18) NOT NULL COMMENT '菜单权限主键',
  `pk_permissions` bigint(18) NOT NULL COMMENT '权限主键',
  `pk_menu` bigint(18) NOT NULL COMMENT '菜单主键',
  PRIMARY KEY (`pk_menu_permissions`),
  KEY `pk_menu` (`pk_menu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

-- ----------------------------
-- Records of superboot_menu_permissions
-- ----------------------------

-- ----------------------------
-- Table structure for superboot_module
-- ----------------------------
DROP TABLE IF EXISTS `superboot_module`;
CREATE TABLE `superboot_module` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 0为正常 1为删除 2为封存',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `createby` bigint(18) DEFAULT NULL COMMENT '创建人',
  `lastmodifytime` datetime DEFAULT NULL COMMENT '最后修改时间',
  `lastmodifyby` bigint(18) DEFAULT NULL COMMENT '最后修改人',
  `pk_module` bigint(18) NOT NULL COMMENT '模块主键',
  `module_id` varchar(500) DEFAULT NULL COMMENT '模块ID',
  `module_name` varchar(200) DEFAULT NULL COMMENT '模块名称',
  PRIMARY KEY (`pk_module`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模块信息';

-- ----------------------------
-- Records of superboot_module
-- ----------------------------
INSERT INTO `superboot_module` VALUES ('2018-06-22 14:31:51', '0', '2018-06-22 14:20:53', null, '2018-06-22 14:31:51', null, '459724462162444288', 'gateway', '网关中心');
INSERT INTO `superboot_module` VALUES ('2018-06-22 14:31:42', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:42', null, '459724807987003392', 'user', '用户中心');

-- ----------------------------
-- Table structure for superboot_org
-- ----------------------------
DROP TABLE IF EXISTS `superboot_org`;
CREATE TABLE `superboot_org` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 0为正常 1为删除 2为封存',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `createby` bigint(18) DEFAULT NULL COMMENT '创建人',
  `lastmodifytime` datetime DEFAULT NULL COMMENT '最后修改时间',
  `lastmodifyby` bigint(18) DEFAULT NULL COMMENT '最后修改人',
  `pk_org` bigint(18) NOT NULL COMMENT '机构主键',
  `org_code` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '编码',
  `org_name` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '名称',
  `nodetype` int(11) DEFAULT NULL COMMENT '节点类型 0:集团 1:公司 2:部门',
  `pk_f_org` bigint(18) DEFAULT NULL COMMENT '上级主键',
  `pk_group` bigint(18) NOT NULL COMMENT '组织主键',
  `org_lev` int(11) DEFAULT NULL COMMENT '机构级次',
  `is_end` char(1) DEFAULT 'N' COMMENT '是否末级 Y为末级 N或者空为非末级 默认为N',
  `order_code` varchar(30) DEFAULT NULL COMMENT '内部序号 自动生成用于排序使用',
  PRIMARY KEY (`pk_org`),
  KEY `pk_group` (`pk_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构信息表';

-- ----------------------------
-- Records of superboot_org
-- ----------------------------
INSERT INTO `superboot_org` VALUES ('2018-03-30 17:23:53', '0', '2017-11-30 17:45:52', '378182074307182592', '2018-03-30 17:23:53', '378187570242125824', '378181361514577920', 'demo', '演示组织', '0', '-1', '378181361514577920', '0', 'Y', '0');
INSERT INTO `superboot_org` VALUES ('2017-12-28 20:29:12', '0', '2018-06-21 14:46:11', '378182074307182592', '2018-06-21 14:46:18', '378187570242125824', '378188098061729792', 'pub', '公开组织', '0', '-1', '378188098061729792', '0', 'Y', '0');

-- ----------------------------
-- Table structure for superboot_permissions
-- ----------------------------
DROP TABLE IF EXISTS `superboot_permissions`;
CREATE TABLE `superboot_permissions` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 0为正常 1为删除 2为封存',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `createby` bigint(18) DEFAULT NULL COMMENT '创建人',
  `lastmodifytime` datetime DEFAULT NULL COMMENT '最后修改时间',
  `lastmodifyby` bigint(18) DEFAULT NULL COMMENT '最后修改人',
  `pk_permissions` bigint(18) NOT NULL COMMENT '权限主键',
  `permissions_code` varchar(100) DEFAULT NULL COMMENT '权限编码',
  `permissions_name` varchar(200) DEFAULT NULL COMMENT '权限名称',
  `permissions_info` varchar(150) DEFAULT NULL COMMENT '描述',
  `pk_group` bigint(18) DEFAULT NULL COMMENT '组织主键',
  PRIMARY KEY (`pk_permissions`),
  KEY `pk_group` (`pk_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限信息表';

-- ----------------------------
-- Records of superboot_permissions
-- ----------------------------
INSERT INTO `superboot_permissions` VALUES ('2018-06-22 14:31:42', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:42', null, '459724809119465472', 'user_sys_info_error', '用户中心系统异常日志管理接口', '提供对异常日志进行查询功能', '-1');
INSERT INTO `superboot_permissions` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724809580838912', 'user_sys_group', '用户中心系统组织管理接口', '提供对系统组织信息的维护管理', '-1');
INSERT INTO `superboot_permissions` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724809958326272', 'user_sys', '用户中心首页管理公共接口', '后台首页管理，提供一些图表类的接口', '-1');
INSERT INTO `superboot_permissions` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724810121904128', 'user_sys_info_log', '用户中心系统日志管理接口', '提供对业务执行日志进行查询功能', '-1');
INSERT INTO `superboot_permissions` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724810348396544', 'user_sys_menu_auth', '用户中心系统菜单授权接口', '提供对系统菜单授权的维护管理', '-1');
INSERT INTO `superboot_permissions` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724810537140224', 'user_sys_menu_conf', '用户中心系统菜单配置接口', '提供对系统菜单信息的维护管理', '-1');
INSERT INTO `superboot_permissions` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724811128537088', 'user_sys_permissions', '用户中心功能权限管理接口', '提供对功能权限信息的维护管理', '-1');
INSERT INTO `superboot_permissions` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724811388583936', 'user_sys_role_auth', '用户中心系统角色授权接口', '提供对系统角色授权的管理', '-1');
INSERT INTO `superboot_permissions` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724811497635840', 'user_sys_role_conf', '用户中心系统角色配置接口', '提供对系统角色信息的维护管理', '-1');
INSERT INTO `superboot_permissions` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724811921260544', 'user_sys_role_user', '用户中心系统角色用户授权接口', '提供对系统角色用户授权的管理', '-1');
INSERT INTO `superboot_permissions` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724811988369408', 'user_sys_user', '用户中心系统用户管理接口', '提供系统用户的管理功能', '-1');
INSERT INTO `superboot_permissions` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:17', null, '2018-06-22 14:31:43', null, '459724812311330816', 'user_userEmployees', '用户中心用户员工关系接口', '用户员工关系接口，提供信息的获取与维护', '-1');
INSERT INTO `superboot_permissions` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:17', null, '2018-06-22 14:31:43', null, '459724812617515008', 'user_users', '用户中心用户基础服务', '提供用户的相关接口管理', '-1');

-- ----------------------------
-- Table structure for superboot_permissions_resource
-- ----------------------------
DROP TABLE IF EXISTS `superboot_permissions_resource`;
CREATE TABLE `superboot_permissions_resource` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 0为正常 1为删除 2为封存',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `createby` bigint(18) DEFAULT NULL COMMENT '创建人',
  `lastmodifytime` datetime DEFAULT NULL COMMENT '最后修改时间',
  `lastmodifyby` bigint(18) DEFAULT NULL COMMENT '最后修改人',
  `pk_permissions_resource` bigint(18) NOT NULL COMMENT '权限资源主键',
  `pk_permissions` bigint(18) NOT NULL COMMENT '权限主键',
  `pk_resource` bigint(18) NOT NULL COMMENT '资源主键',
  PRIMARY KEY (`pk_permissions_resource`),
  KEY `pk_permissions` (`pk_permissions`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限资源列表';

-- ----------------------------
-- Records of superboot_permissions_resource
-- ----------------------------
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724809215934464', '459724809119465472', '459724809064939520');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724809392095232', '459724809119465472', '459724809324986368');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724809513730048', '459724809119465472', '459724809480175616');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724809597616128', '459724809580838912', '459724809555673088');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724809681502208', '459724809580838912', '459724809639559168');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724809748611072', '459724809580838912', '459724809715056640');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724809815719936', '459724809580838912', '459724809782165504');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724809895411712', '459724809580838912', '459724809857662976');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724809975103488', '459724809958326272', '459724809937354752');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724810054795264', '459724809958326272', '459724810017046528');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724810138681344', '459724810121904128', '459724810096738304');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724810209984512', '459724810121904128', '459724810184818688');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724810285481984', '459724810121904128', '459724810251927552');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724810373562368', '459724810348396544', '459724810331619328');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724810436476928', '459724810348396544', '459724810411311104');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724810491002880', '459724810348396544', '459724810465837056');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724810549723136', '459724810537140224', '459724810524557312');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724810608443392', '459724810537140224', '459724810583277568');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724810738466816', '459724810537140224', '459724810679746560');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724810809769984', '459724810537140224', '459724810780409856');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724810872684544', '459724810537140224', '459724810847518720');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724810943987712', '459724810537140224', '459724810910433280');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724811006902272', '459724810537140224', '459724810973347840');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724811069816832', '459724810537140224', '459724811044651008');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724811145314304', '459724811128537088', '459724811111759872');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724811216617472', '459724811128537088', '459724811183063040');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724811275337728', '459724811128537088', '459724811250171904');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724811346640896', '459724811128537088', '459724811317280768');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724811405361152', '459724811388583936', '459724811371806720');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724811522801664', '459724811497635840', '459724811476664320');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724811627659264', '459724811497635840', '459724811594104832');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724811694768128', '459724811497635840', '459724811661213696');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724811753488384', '459724811497635840', '459724811719933952');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724811816402944', '459724811497635840', '459724811795431424');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724811879317504', '459724811497635840', '459724811854151680');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724811942232064', '459724811921260544', '459724811912871936');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724812005146624', '459724811988369408', '459724811975786496');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724812063866880', '459724811988369408', '459724812038701056');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724812114198528', '459724811988369408', '459724812089032704');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:16', '0', '2018-06-22 14:22:16', null, null, null, '459724812160335872', '459724811988369408', '459724812143558656');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:17', '0', '2018-06-22 14:22:17', null, null, null, '459724812210667520', '459724811988369408', '459724812189696000');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:17', '0', '2018-06-22 14:22:17', null, null, null, '459724812269387776', '459724811988369408', '459724812244221952');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:17', '0', '2018-06-22 14:22:17', null, null, null, '459724812328108032', '459724812311330816', '459724812294553600');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:17', '0', '2018-06-22 14:22:17', null, null, null, '459724812378439680', '459724812311330816', '459724812353273856');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:17', '0', '2018-06-22 14:22:17', null, null, null, '459724812428771328', '459724812311330816', '459724812407799808');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:17', '0', '2018-06-22 14:22:17', null, null, null, '459724812495880192', '459724812311330816', '459724812470714368');
INSERT INTO `superboot_permissions_resource` VALUES ('2018-06-22 14:22:17', '0', '2018-06-22 14:22:17', null, null, null, '459724812634292224', '459724812617515008', '459724812596543488');

-- ----------------------------
-- Table structure for superboot_resource
-- ----------------------------
DROP TABLE IF EXISTS `superboot_resource`;
CREATE TABLE `superboot_resource` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 0为正常 1为删除 2为封存',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `createby` bigint(18) DEFAULT NULL COMMENT '创建人',
  `lastmodifytime` datetime DEFAULT NULL COMMENT '最后修改时间',
  `lastmodifyby` bigint(18) DEFAULT NULL COMMENT '最后修改人',
  `pk_resource` bigint(18) NOT NULL COMMENT '资源主键',
  `module_name` varchar(200) DEFAULT NULL COMMENT '模块名称',
  `service_name` varchar(50) DEFAULT NULL COMMENT '服务名称',
  `module_id` varchar(500) DEFAULT NULL COMMENT '模块ID',
  `api_name` varchar(500) DEFAULT NULL COMMENT '接口名称',
  `method_name` varchar(500) DEFAULT NULL COMMENT '方法名称',
  `module_path` varchar(200) DEFAULT NULL COMMENT '模块路径',
  `method_path` varchar(1000) DEFAULT NULL COMMENT '请求路径',
  `url` varchar(1000) DEFAULT NULL COMMENT '地址',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`pk_resource`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统资源表';

-- ----------------------------
-- Records of superboot_resource
-- ----------------------------
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:51', '0', '2018-06-22 14:20:53', null, '2018-06-22 14:31:51', null, '459724462560903168', '网关中心', '网关中心公共服务接口', 'gateway', '用户注册', 'register', '/PubApi', 'org.superboot.controller.BaseController', '/PubApi/register', '用户注册，返回TOKEN信息。为了数据安全，此接口不允许使用非加密方式调用');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:51', '0', '2018-06-22 14:20:53', null, '2018-06-22 14:31:51', null, '459724462820950016', '网关中心', '网关中心公共服务接口', 'gateway', '获取TOKEN的详细信息', 'tokenInfo', '/PubApi', 'org.superboot.controller.BaseController', '/PubApi/token', '第三方系统授权登陆后，页面集成用到的token关联用户信息');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:51', '0', '2018-06-22 14:20:53', null, '2018-06-22 14:31:51', null, '459724462862893056', '网关中心', '网关中心公共服务接口', 'gateway', '刷新TOKEN', 'refreshToken', '/PubApi', 'org.superboot.controller.BaseController', '/PubApi/refresh', '刷新TOKEN,一般用于TOKEN锁定或者过期的时候使用');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:51', '0', '2018-06-22 14:20:53', null, '2018-06-22 14:31:51', null, '459724462909030400', '网关中心', '网关中心公共服务接口', 'gateway', '用户登陆', 'login', '/PubApi', 'org.superboot.controller.BaseController', '/PubApi/login', '用户登陆，返回TOKEN信息。为了数据安全，此接口不允许使用非加密方式调用');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:51', '0', '2018-06-22 14:20:53', null, '2018-06-22 14:31:51', null, '459724462950973440', '网关中心', '网关中心公共服务接口', 'gateway', '授权登陆', 'sso', '/PubApi', 'org.superboot.controller.BaseController', '/PubApi/sso', '第三方系统授权登陆，返回TOKEN信息。为了数据安全，此接口不允许使用非加密方式调用');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:51', '0', '2018-06-22 14:20:53', null, '2018-06-22 14:31:51', null, '459724462984527872', '网关中心', '网关中心公共服务接口', 'gateway', '获取服务器时间信息', 'getSysDate', '/PubApi', 'org.superboot.controller.BaseController', '/PubApi/sysDate', '获取服务器的时间信息');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:51', '0', '2018-06-22 14:20:53', null, '2018-06-22 14:31:51', null, '459724463018082304', '网关中心', '网关中心公共服务接口', 'gateway', '解锁TOKEN', 'unlockedToken', '/PubApi', 'org.superboot.controller.BaseController', '/PubApi/unlocked', '解锁TOKEN,需要指定角色才有此权限');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:51', '0', '2018-06-22 14:20:53', null, '2018-06-22 14:31:51', null, '459724463080996864', '网关中心', '网关中心公共服务接口', 'gateway', '锁定TOKEN', 'lockedToken', '/PubApi', 'org.superboot.controller.BaseController', '/PubApi/locked', '锁定TOKEN,需要指定角色才有此权限');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:51', '0', '2018-06-22 14:20:53', null, '2018-06-22 14:31:51', null, '459724463118745600', '网关中心', '网关中心公共服务接口', 'gateway', '用户退出', 'logout', '/PubApi', 'org.superboot.controller.BaseController', '/PubApi/logout', '用户退出,一般用于网站登陆时候使用');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:51', '0', '2018-06-22 14:20:53', null, '2018-06-22 14:31:51', null, '459724463160688640', '网关中心', '网关中心公共服务接口', 'gateway', '获取RSA公钥', 'getPubKey', '/PubApi', 'org.superboot.controller.BaseController', '/PubApi/key', '获取RSA公钥，用于客户端加密AES密钥使用');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:51', '0', '2018-06-22 14:20:53', null, '2018-06-22 14:31:51', null, '459724463202631680', '网关中心', '网关中心公共服务接口', 'gateway', '格式化日期用于手机应用使用', 'formatShowDate', '/PubApi', 'org.superboot.controller.BaseController', '/PubApi/formatShowDate/{date}/{time}', '格式化日期用于手机应用使用，显示为几分钟前、几小时前等');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:42', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:42', null, '459724808645509120', '用户中心', '用户中心公有接口', 'user', '获取组织树', 'getGroupTree', '/base', 'org.superboot.controller.BaseController', '/base/group/tree', '获取组织树,主要用于构造菜单使用');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:42', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:42', null, '459724809027190784', '用户中心', '用户中心公有接口', 'user', '获取全部组织信息', 'getGroupAll', '/base', 'org.superboot.controller.BaseController', '/base/group/all', '获取全部组织信息,主要用于下拉使用');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:42', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:42', null, '459724809064939520', '用户中心', '用户中心系统异常日志管理接口', 'user', '获取日志记录数', 'getErrorLogCount', '/sys/info/error', 'org.superboot.controller.sys.SysErrLogController', '/sys/info/error/count', '获取日志记录数，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:42', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:42', null, '459724809324986368', '用户中心', '用户中心系统异常日志管理接口', 'user', '获取日志详细信息', 'getErrorLogItem', '/sys/info/error', 'org.superboot.controller.sys.SysErrLogController', '/sys/info/error/{id}', '获取日志详细信息，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:42', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:42', null, '459724809480175616', '用户中心', '用户中心系统异常日志管理接口', 'user', '获取日志列表信息', 'getErrorLogList', '/sys/info/error', 'org.superboot.controller.sys.SysErrLogController', '/sys/info/error/', '获取日志列表信息，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:42', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:42', null, '459724809555673088', '用户中心', '用户中心系统组织管理接口', 'user', '修改组织信息', 'setGroup', '/sys/group', 'org.superboot.controller.sys.SysGroupController', '/sys/group/{groupid}', '修改组织信息，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:42', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:42', null, '459724809639559168', '用户中心', '用户中心系统组织管理接口', 'user', '获取组织信息', 'getGroupList', '/sys/group', 'org.superboot.controller.sys.SysGroupController', '/sys/group/', '获取组织基本信息，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:42', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:42', null, '459724809715056640', '用户中心', '用户中心系统组织管理接口', 'user', '添加组织', 'addGroup', '/sys/group', 'org.superboot.controller.sys.SysGroupController', '/sys/group/', '添加组织，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:42', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:42', null, '459724809782165504', '用户中心', '用户中心系统组织管理接口', 'user', '获取组织记录总数', 'getGroupCount', '/sys/group', 'org.superboot.controller.sys.SysGroupController', '/sys/group/count', '获取组织记录总数，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724809857662976', '用户中心', '用户中心系统组织管理接口', 'user', '删除组织', 'delGroup', '/sys/group', 'org.superboot.controller.sys.SysGroupController', '/sys/group/{groupid}', '删除组织，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724809937354752', '用户中心', '用户中心首页管理公共接口', 'user', '获取今天错误日志分组信息', 'getTodayErrorCount', '/sys', 'org.superboot.controller.sys.SysIndexController', '/sys/todayErrorCount', '获取今天错误日志分组信息，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724810017046528', '用户中心', '用户中心首页管理公共接口', 'user', '获取过去7天各模块请求信息', 'getRequestCountByWeek', '/sys', 'org.superboot.controller.sys.SysIndexController', '/sys/requestCountByWeek', '获取过去7天各模块请求信息，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724810096738304', '用户中心', '用户中心系统日志管理接口', 'user', '获取日志详细信息', 'getLogItem', '/sys/info/log', 'org.superboot.controller.sys.SysLogController', '/sys/info/log/{id}', '获取日志详细信息，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724810184818688', '用户中心', '用户中心系统日志管理接口', 'user', '获取日志列表信息', 'getLogList', '/sys/info/log', 'org.superboot.controller.sys.SysLogController', '/sys/info/log/', '获取日志列表信息，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724810251927552', '用户中心', '用户中心系统日志管理接口', 'user', '获取日志记录数', 'getLogCount', '/sys/info/log', 'org.superboot.controller.sys.SysLogController', '/sys/info/log/count', '获取日志记录数，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724810331619328', '用户中心', '用户中心系统菜单授权接口', 'user', '菜单授权', 'addMenuAuth', '/sys/menu/auth', 'org.superboot.controller.sys.SysMenuAuthController', '/sys/menu/auth/', '菜单授权，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724810411311104', '用户中心', '用户中心系统菜单授权接口', 'user', '取消菜单授权', 'delMenuAuth', '/sys/menu/auth', 'org.superboot.controller.sys.SysMenuAuthController', '/sys/menu/auth/{menuPermissionsId}', '取消菜单授权，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724810465837056', '用户中心', '用户中心系统菜单授权接口', 'user', '获取菜单授权列表', 'getMenuAuthList', '/sys/menu/auth', 'org.superboot.controller.sys.SysMenuAuthController', '/sys/menu/auth/{menuId}', '获取菜单授权接口列表，主要用于取消授权使用');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724810524557312', '用户中心', '用户中心系统菜单配置接口', 'user', '获取菜单总数', 'getCount', '/sys/menu/conf', 'org.superboot.controller.sys.SysMenuController', '/sys/menu/conf/count', '获取菜单总数，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724810583277568', '用户中心', '用户中心系统菜单配置接口', 'user', '获取全部菜单', 'getItems', '/sys/menu/conf', 'org.superboot.controller.sys.SysMenuController', '/sys/menu/conf/', '获取全部菜单，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724810679746560', '用户中心', '用户中心系统菜单配置接口', 'user', '获取角色菜单', 'getItemsByRole', '/sys/menu/conf', 'org.superboot.controller.sys.SysMenuController', '/sys/menu/conf/getItemsByRole/{role_id}', '获取角色菜单，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724810780409856', '用户中心', '用户中心系统菜单配置接口', 'user', '添加菜单', 'addMenu', '/sys/menu/conf', 'org.superboot.controller.sys.SysMenuController', '/sys/menu/conf/', '添加菜单，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724810847518720', '用户中心', '用户中心系统菜单配置接口', 'user', '获取菜单信息', 'getMenu', '/sys/menu/conf', 'org.superboot.controller.sys.SysMenuController', '/sys/menu/conf/{menuId}', '获取菜单信息，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724810910433280', '用户中心', '用户中心系统菜单配置接口', 'user', '获取角色未授权菜单', 'getRoleNoMenu', '/sys/menu/conf', 'org.superboot.controller.sys.SysMenuController', '/sys/menu/conf/getRoleNoMenu/{role_id}', '获取角色未授权菜单，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724810973347840', '用户中心', '用户中心系统菜单配置接口', 'user', '删除菜单', 'delMenu', '/sys/menu/conf', 'org.superboot.controller.sys.SysMenuController', '/sys/menu/conf/{menuId}', '删除菜单，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724811044651008', '用户中心', '用户中心系统菜单配置接口', 'user', '修改菜单', 'setMenu', '/sys/menu/conf', 'org.superboot.controller.sys.SysMenuController', '/sys/menu/conf/{menuId}', '修改菜单，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724811111759872', '用户中心', '用户中心功能权限管理接口', 'user', '获取权限列表', 'getPermissions', '/sys/permissions', 'org.superboot.controller.sys.SysPermissionsController', '/sys/permissions/', '获取权限列表，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724811183063040', '用户中心', '用户中心功能权限管理接口', 'user', '获取权限信息', 'getPermission', '/sys/permissions', 'org.superboot.controller.sys.SysPermissionsController', '/sys/permissions/{permissionId}', '获取权限信息，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724811250171904', '用户中心', '用户中心功能权限管理接口', 'user', '获取全部信息', 'getAll', '/sys/permissions', 'org.superboot.controller.sys.SysPermissionsController', '/sys/permissions/all', '获取全部信息，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724811317280768', '用户中心', '用户中心功能权限管理接口', 'user', '删除权限信息', 'delPermission', '/sys/permissions', 'org.superboot.controller.sys.SysPermissionsController', '/sys/permissions/{permissionId}', '删除权限，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724811371806720', '用户中心', '用户中心系统角色授权接口', 'user', '角色菜单授权', 'addRoleMenu', '/sys/role/auth', 'org.superboot.controller.sys.SysRoleAuthController', '/sys/role/auth/', '角色菜单授权，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724811476664320', '用户中心', '用户中心系统角色配置接口', 'user', '获取记录总数', 'getCount', '/sys/role/conf', 'org.superboot.controller.sys.SysRoleController', '/sys/role/conf/count', '获取全部角色，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724811594104832', '用户中心', '用户中心系统角色配置接口', 'user', '获取全部角色', 'getAll', '/sys/role/conf', 'org.superboot.controller.sys.SysRoleController', '/sys/role/conf/all', '获取全部角色多用于下拉，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724811661213696', '用户中心', '用户中心系统角色配置接口', 'user', '获取全部角色，分页查询', 'getItems', '/sys/role/conf', 'org.superboot.controller.sys.SysRoleController', '/sys/role/conf/', '获取全部角色，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724811719933952', '用户中心', '用户中心系统角色配置接口', 'user', '添加系统角色', 'addSysRole', '/sys/role/conf', 'org.superboot.controller.sys.SysRoleController', '/sys/role/conf/', '添加系统角色，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724811795431424', '用户中心', '用户中心系统角色配置接口', 'user', '修改系统角色', 'setSysRole', '/sys/role/conf', 'org.superboot.controller.sys.SysRoleController', '/sys/role/conf/{roleId}', '修改系统角色，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724811854151680', '用户中心', '用户中心系统角色配置接口', 'user', '删除系统角色', 'delSysRole', '/sys/role/conf', 'org.superboot.controller.sys.SysRoleController', '/sys/role/conf/{roleId}', '删除角色，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724811912871936', '用户中心', '用户中心系统角色用户授权接口', 'user', '角色用户授权', 'addRoleUser', '/sys/role/user', 'org.superboot.controller.sys.SysRoleUserController', '/sys/role/user/', '角色用户授权，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724811975786496', '用户中心', '用户中心系统用户管理接口', 'user', '获取用户信息', 'getCount', '/sys/user', 'org.superboot.controller.sys.SysUserController', '/sys/user/count', '获取系统用户信息，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724812038701056', '用户中心', '用户中心系统用户管理接口', 'user', '注册管理用户', 'addAdmin', '/sys/user', 'org.superboot.controller.sys.SysUserController', '/sys/user/', '注册管理用户，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724812089032704', '用户中心', '用户中心系统用户管理接口', 'user', '注册组织管理员', 'addGroupAdmin', '/sys/user', 'org.superboot.controller.sys.SysUserController', '/sys/user/group/{group_id}', '注册组织管理员，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:16', null, '2018-06-22 14:31:43', null, '459724812143558656', '用户中心', '用户中心系统用户管理接口', 'user', '创建指定角色用户', 'addUserByRole', '/sys/user', 'org.superboot.controller.sys.SysUserController', '/sys/user/role/{role_id}', '创建指定角色用户，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:17', null, '2018-06-22 14:31:43', null, '459724812189696000', '用户中心', '用户中心系统用户管理接口', 'user', '获取用户信息', 'getUser', '/sys/user', 'org.superboot.controller.sys.SysUserController', '/sys/user/', '获取系统用户信息，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:17', null, '2018-06-22 14:31:43', null, '459724812244221952', '用户中心', '用户中心系统用户管理接口', 'user', '锁定用户', 'delUser', '/sys/user', 'org.superboot.controller.sys.SysUserController', '/sys/user/{userId}', '锁定用户，系统管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:17', null, '2018-06-22 14:31:43', null, '459724812294553600', '用户中心', '用户中心用户员工关系接口', 'user', '获取组织用户列表信息', 'getUserList', '/userEmployees', 'org.superboot.controller.sys.SysUserEmployeesRelaController', '/userEmployees/userList/{group_id}', '获取组织用户列表信息，组织管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:17', null, '2018-06-22 14:31:43', null, '459724812353273856', '用户中心', '用户中心用户员工关系接口', 'user', '用户解绑员工', 'userDebindEmp', '/userEmployees', 'org.superboot.controller.sys.SysUserEmployeesRelaController', '/userEmployees/{employees_id}', '用户解绑员工，组织管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:17', null, '2018-06-22 14:31:43', null, '459724812407799808', '用户中心', '用户中心用户员工关系接口', 'user', '用户绑定员工', 'userBindEmp', '/userEmployees', 'org.superboot.controller.sys.SysUserEmployeesRelaController', '/userEmployees/', '用户绑定员工，组织管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:17', null, '2018-06-22 14:31:43', null, '459724812470714368', '用户中心', '用户中心用户员工关系接口', 'user', '获取用户员工信息', 'getUserEmpInfo', '/userEmployees', 'org.superboot.controller.sys.SysUserEmployeesRelaController', '/userEmployees/{employees_id}', '获取用户员工信息，组织管理员默认可以访问');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:17', null, '2018-06-22 14:31:43', null, '459724812529434624', '用户中心', '用户中心用户基础服务', 'user', '修改密码', 'password', '/users', 'org.superboot.controller.UserController', '/users/password', '修改密码');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:17', null, '2018-06-22 14:31:43', null, '459724812554600448', '用户中心', '用户中心用户基础服务', 'user', '注册普通用户', 'addUser', '/users', 'org.superboot.controller.UserController', '/users/register', '注册普通用户');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:17', null, '2018-06-22 14:31:43', null, '459724812571377664', '用户中心', '用户中心用户基础服务', 'user', '授权登陆', 'sso', '/users', 'org.superboot.controller.UserController', '/users/sso', '第三方系统授权登陆，返回TOKEN信息');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:17', null, '2018-06-22 14:31:43', null, '459724812596543488', '用户中心', '用户中心用户基础服务', 'user', '变更用户基础信息', 'setUserBase', '/users', 'org.superboot.controller.UserController', '/users/{pkUser}/base', '变更用户姓名、状态、失效日期，其他信息均不提供变更');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:17', null, '2018-06-22 14:31:43', null, '459724812659458048', '用户中心', '用户中心用户基础服务', 'user', '登陆', 'login', '/users', 'org.superboot.controller.UserController', '/users/login', '用户登陆');
INSERT INTO `superboot_resource` VALUES ('2018-06-22 14:31:43', '0', '2018-06-22 14:22:17', null, '2018-06-22 14:31:43', null, '459724812684623872', '用户中心', '用户中心用户基础服务', 'user', '获取用户菜单', 'getUserMenu', '/users', 'org.superboot.controller.UserController', '/users/menu', '获取用户菜单，如果出现菜单授权变更，需要重新登陆');

-- ----------------------------
-- Table structure for superboot_role
-- ----------------------------
DROP TABLE IF EXISTS `superboot_role`;
CREATE TABLE `superboot_role` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 0为正常 1为删除 2为封存',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `createby` bigint(18) DEFAULT NULL COMMENT '创建人',
  `lastmodifytime` datetime DEFAULT NULL COMMENT '最后修改时间',
  `lastmodifyby` bigint(18) DEFAULT NULL COMMENT '最后修改人',
  `pk_role` bigint(18) NOT NULL COMMENT '角色主键',
  `role_code` varchar(20) NOT NULL COMMENT '角色编码',
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `role_info` longtext COMMENT '角色说明',
  `role_type` int(11) DEFAULT '1' COMMENT '角色类别 0为系统预置 1为用户创建',
  `pk_group` bigint(18) DEFAULT NULL COMMENT '创建组织',
  PRIMARY KEY (`pk_role`),
  KEY `role_code` (`role_code`),
  KEY `pk_group` (`pk_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- Records of superboot_role
-- ----------------------------
INSERT INTO `superboot_role` VALUES ('2018-05-31 17:15:57', '0', '2017-11-07 09:19:27', '377457784972640256', '2018-05-31 17:15:57', '377457784972640256', '377385783062953984', 'SYS_ADMIN', '系统管理员', '系统管理员', '0', '-1');
INSERT INTO `superboot_role` VALUES ('2018-02-08 11:45:00', '0', '2017-11-07 09:21:35', '377457784972640256', '2018-02-08 11:45:00', '377457784972640256', '377385783532716032', 'GROUP_ADMIN', '组织管理员', '组织管理员', '0', '-1');
INSERT INTO `superboot_role` VALUES ('2017-11-07 09:27:29', '0', '2017-11-07 09:27:03', '377457784972640256', '2017-11-09 14:17:51', '377457784972640256', '377386234021937152', 'PUB_USER', '开发注册用户', '开发注册用户', '0', '-1');
INSERT INTO `superboot_role` VALUES ('2017-11-09 14:20:09', '0', '2017-11-09 14:20:09', '377457784972640256', '2018-06-21 14:47:08', '377457784972640256', '378187007148425216', 'SYS_DEV', '开发人员', '开发人员', '0', '-1');
INSERT INTO `superboot_role` VALUES ('2017-12-13 11:27:23', '0', '2017-12-13 11:27:23', '378185327187066880', '2018-06-21 14:47:41', '377457784972640256', '390464715102355456', 'SYS_OPER', '运维人员', '系统运维人员', '0', '-1');
INSERT INTO `superboot_role` VALUES ('2018-01-15 09:35:29', '0', '2018-01-15 09:35:29', '377457784972640256', '2018-06-21 14:47:43', '377457784972640256', '402395355716517888', 'SYS_BASE', '档案管理角色', '管理档案信息', '0', '-1');
INSERT INTO `superboot_role` VALUES ('2018-01-31 15:02:58', '0', '2018-01-31 15:02:58', '377457784972640256', '2018-06-21 14:47:46', '377457784972640256', '408275975067926528', 'SYS_CONF', '系统配置管理员', '维护系统管理功能', '0', '-1');

-- ----------------------------
-- Table structure for superboot_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `superboot_role_menu`;
CREATE TABLE `superboot_role_menu` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 0为正常 1为删除 2为封存',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `createby` bigint(18) DEFAULT NULL COMMENT '创建人',
  `lastmodifytime` datetime DEFAULT NULL COMMENT '最后修改时间',
  `lastmodifyby` bigint(18) DEFAULT NULL COMMENT '最后修改人',
  `pk_role_menu` bigint(18) NOT NULL COMMENT '角色菜单主键',
  `pk_menu` bigint(18) NOT NULL COMMENT '菜单主键',
  `pk_role` bigint(18) NOT NULL COMMENT '角色主键',
  PRIMARY KEY (`pk_role_menu`),
  KEY `pk_role` (`pk_role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单信息表';

-- ----------------------------
-- Records of superboot_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for superboot_user
-- ----------------------------
DROP TABLE IF EXISTS `superboot_user`;
CREATE TABLE `superboot_user` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 0为正常 1为删除 2为封存',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `createby` bigint(18) DEFAULT NULL COMMENT '创建人',
  `lastmodifytime` datetime DEFAULT NULL COMMENT '最后修改时间',
  `lastmodifyby` bigint(18) DEFAULT NULL COMMENT '最后修改人',
  `pk_user` bigint(18) NOT NULL COMMENT '用户主键',
  `user_code` varchar(20) DEFAULT NULL COMMENT '用户账号',
  `user_password` char(60) DEFAULT NULL COMMENT '用户密码使用MD5双加密存储',
  `user_email` varchar(150) DEFAULT NULL COMMENT '邮箱',
  `user_phone` varchar(20) DEFAULT NULL COMMENT '手机',
  `user_status` int(11) DEFAULT '0' COMMENT '用户状态 默认0 正常态 1锁定',
  `user_name` varchar(20) DEFAULT NULL COMMENT '用户姓名',
  `user_id` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `user_auth` int(11) DEFAULT '0' COMMENT '认证状态 0为注册用户 1为认证用户',
  `en_key` varchar(50) DEFAULT NULL COMMENT '密码加密密钥',
  `init_user` int(11) DEFAULT '0' COMMENT '是否为初始用户，默认0为初始用户 1为非初始用户，初始用户需要初始化信息',
  `last_password_reset_date` datetime DEFAULT NULL COMMENT '最后修改密码时间',
  `pk_group` bigint(18) DEFAULT NULL COMMENT '组织主键',
  `end_time` varchar(19) DEFAULT NULL COMMENT '账号停用时间',
  PRIMARY KEY (`pk_user`),
  KEY `user_code` (`user_code`),
  KEY `user_email` (`user_email`),
  KEY `user_phone` (`user_phone`),
  KEY `pk_group` (`pk_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- ----------------------------
-- Records of superboot_user
-- ----------------------------
INSERT INTO `superboot_user` VALUES ('2018-05-15 15:51:52', '0', '2017-11-08 18:56:50', '377457784972640256', '2018-05-15 15:51:52', '377457784972640256', '377457784972640256', 'admin', '23CC827BA04603A796AD0844915C1F33', 'admin@superboot.org', '', '0', '系统管理员', '', '1', 'o3Z8yPil', '0', '2018-05-15 15:51:52', '-1', '');
INSERT INTO `superboot_user` VALUES ('2018-05-14 19:46:26', '0', '2017-11-09 14:00:33', '377457784972640256', '2018-05-14 19:46:26', '377457784972640256', '378182074307182592', 'group', '228E60403ADA3534D0700FAFC777D9F8', 'group@superboot.org', '', '0', '公共管理员', '', '1', 'KUcxNxxD', '0', '2017-11-09 14:00:33', '378188098061729792', '');
INSERT INTO `superboot_user` VALUES ('2018-02-06 11:03:24', '0', '2017-11-09 14:22:23', '378182074307182592', '2018-02-06 11:03:24', '378182074307182592', '378187570242125824', 'demo', 'A8E3B7C8443C9F3F94183DD62FE79167', 'demo@superboot.org', '', '0', '演示管理员', '', '1', 'DHKnC1PJ', '0', '2017-11-09 14:22:23', '378181361514577920', '');

-- ----------------------------
-- Table structure for superboot_user_employees
-- ----------------------------
DROP TABLE IF EXISTS `superboot_user_employees`;
CREATE TABLE `superboot_user_employees` (
  `ts` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 0为正常 1为删除 2为封存',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `createby` bigint(18) DEFAULT NULL COMMENT '创建人',
  `lastmodifytime` datetime DEFAULT NULL COMMENT '最后修改时间',
  `lastmodifyby` bigint(18) DEFAULT NULL COMMENT '最后修改人',
  `pk_user_employees` bigint(18) NOT NULL COMMENT '用户人员主键',
  `pk_employees` bigint(18) NOT NULL COMMENT '人员主键',
  `pk_user` bigint(18) NOT NULL COMMENT '用户主键',
  PRIMARY KEY (`pk_user_employees`),
  KEY `pk_employees` (`pk_employees`),
  KEY `pk_user` (`pk_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人员用户表';

-- ----------------------------
-- Records of superboot_user_employees
-- ----------------------------

-- ----------------------------
-- Table structure for superboot_user_role
-- ----------------------------
DROP TABLE IF EXISTS `superboot_user_role`;
CREATE TABLE `superboot_user_role` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 0为正常 1为删除 2为封存',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `createby` bigint(18) DEFAULT NULL COMMENT '创建人',
  `lastmodifytime` datetime DEFAULT NULL COMMENT '最后修改时间',
  `lastmodifyby` bigint(18) DEFAULT NULL COMMENT '最后修改人',
  `user_role_id` bigint(18) NOT NULL COMMENT '用户角色主键',
  `pk_role` bigint(18) DEFAULT NULL COMMENT '角色主键',
  `pk_user` bigint(18) DEFAULT NULL COMMENT '用户主键',
  PRIMARY KEY (`user_role_id`),
  KEY `pk_user` (`pk_role`,`pk_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色表';

-- ----------------------------
-- Records of superboot_user_role
-- ----------------------------
INSERT INTO `superboot_user_role` VALUES ('2017-11-07 14:02:29', '0', '2017-11-09 14:15:58', '377457784972640256', '2017-11-07 14:02:29', '377457784972640256', '377457785538871296', '377385783062953984', '377457784972640256');
INSERT INTO `superboot_user_role` VALUES ('2017-11-09 14:00:33', '0', '2017-11-09 14:00:33', '377457784972640256', '2017-11-09 14:00:33', '377457784972640256', '378182074638532608', '377385783532716032', '378182074307182592');
INSERT INTO `superboot_user_role` VALUES ('2017-11-09 14:22:23', '0', '2017-11-09 14:22:23', '378182074307182592', '2017-11-09 14:22:23', '378182074307182592', '378187570523144192', '377385783532716032', '378187570242125824');
