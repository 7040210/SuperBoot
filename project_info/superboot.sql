/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50631
Source Host           : localhost:3306
Source Database       : superboot

Target Server Type    : MYSQL
Target Server Version : 50631
File Encoding         : 65001

Date: 2017-09-13 20:47:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_api
-- ----------------------------
DROP TABLE IF EXISTS `sys_api`;
CREATE TABLE `sys_api` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 1为删除 2为封存',
  `pk_api` bigint(18) NOT NULL COMMENT '接口主键',
  `module_name` varchar(200) DEFAULT NULL COMMENT '模块名称',
  `api_name` varchar(500) DEFAULT NULL COMMENT '接口名称',
  `method_name` varchar(500) DEFAULT NULL COMMENT '方法名称',
  `module_path` varchar(200) DEFAULT NULL COMMENT '模块路径',
  `method_path` varchar(1000) DEFAULT NULL COMMENT '请求路径',
  `url` varchar(1000) DEFAULT NULL COMMENT '地址',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`pk_api`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统接口表';

-- ----------------------------
-- Records of sys_api
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `ts` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 1为删除 2为封存',
  `pk_role` bigint(18) NOT NULL COMMENT '角色主键',
  `role_code` varchar(20) NOT NULL COMMENT '角色编码',
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `role_info` longtext COMMENT '角色说明',
  `role_type` int(11) DEFAULT '1' COMMENT '角色类别 0为系统预置 1为用户创建',
  PRIMARY KEY (`pk_role`),
  KEY `role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('2017-09-07 22:24:58', '0', '355477450278305792', 'ROLE_ADMIN', '系统管理员', null, '1');
INSERT INTO `sys_role` VALUES ('2017-09-07 22:25:15', '0', '355477450278305793', 'ROLE_USER', '普通用户', null, '1');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `ts` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 1为删除 2为封存',
  `pk_user` bigint(18) NOT NULL COMMENT '用户主键',
  `user_code` varchar(20) DEFAULT NULL COMMENT '用户账号',
  `user_password` char(60) DEFAULT NULL COMMENT '用户密码使用MD5双加密存储',
  `user_email` varchar(50) DEFAULT NULL COMMENT '用户邮箱',
  `user_phone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `user_name` varchar(20) DEFAULT NULL COMMENT '用户姓名',
  `user_idcard` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `user_auth` int(11) DEFAULT '0' COMMENT '认证状态 0为注册用户 1为认证用户',
  `last_password_reset_date` datetime DEFAULT NULL COMMENT '最后修改密码时间',
  PRIMARY KEY (`pk_user`),
  KEY `user_code` (`user_code`),
  KEY `user_idcard` (`user_idcard`),
  KEY `user_phone` (`user_phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('2017-09-09 15:00:20', '0', '356091461931892736', 'admin', '$2a$10$2Cf777KNQlkT5VarwMhayOtaRMabvUXEECNJpWwi0HhI5Rugre.sy', 'admin@superboot.org', '18601020888', null, null, '0', '2017-09-09 15:00:20');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `ts` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 1为删除 2为封存',
  `user_role_id` bigint(18) NOT NULL COMMENT '用户角色主键',
  `pk_role` bigint(18) DEFAULT NULL COMMENT '角色主键',
  `pk_user` bigint(18) DEFAULT NULL COMMENT '用户主键',
  PRIMARY KEY (`user_role_id`),
  KEY `pk_user` (`pk_role`,`pk_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('2017-09-09 15:00:20', '0', '356091462040944640', '355477450278305793', '356091461931892736');
INSERT INTO `sys_user_role` VALUES ('2017-09-10 08:48:09', '0', '356178642973229056', '355477450278305792', '356091461931892736');
