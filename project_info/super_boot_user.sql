/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50631
Source Host           : localhost:3306
Source Database       : super_boot_user

Target Server Type    : MYSQL
Target Server Version : 50631
File Encoding         : 65001

Date: 2017-09-26 11:01:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ucenter_role
-- ----------------------------
DROP TABLE IF EXISTS `ucenter_role`;
CREATE TABLE `ucenter_role` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 0为正常 1为删除 2为封存',
  `pk_role` bigint(18) NOT NULL COMMENT '角色主键',
  `role_code` varchar(20) NOT NULL COMMENT '角色编码',
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `role_info` longtext COMMENT '角色说明',
  `role_type` int(11) DEFAULT '1' COMMENT '角色类别 0为系统预置 1为用户创建',
  PRIMARY KEY (`pk_role`),
  KEY `role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- Records of ucenter_role
-- ----------------------------
INSERT INTO `ucenter_role` VALUES ('2017-09-15 19:23:40', '0', '358331074419359744', 'ROLE_ADMIN', '管理员', '系统管理用户', '0');
INSERT INTO `ucenter_role` VALUES ('2017-09-15 19:23:57', '0', '358331696208150528', 'ROLE_USER', '用户', '普通用户', '0');

-- ----------------------------
-- Table structure for ucenter_user
-- ----------------------------
DROP TABLE IF EXISTS `ucenter_user`;
CREATE TABLE `ucenter_user` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 0为正常 1为删除 2为封存',
  `pk_user` bigint(18) NOT NULL COMMENT '用户主键',
  `user_code` varchar(20) DEFAULT NULL COMMENT '用户账号',
  `user_password` char(60) DEFAULT NULL COMMENT '用户密码使用MD5双加密存储',
  `random` char(32) DEFAULT NULL COMMENT '使用32位UUID',
  `last_password_reset_date` datetime DEFAULT NULL COMMENT '最后修改密码时间',
  PRIMARY KEY (`pk_user`),
  KEY `user_code` (`user_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- Records of ucenter_user
-- ----------------------------
INSERT INTO `ucenter_user` VALUES ('2017-09-15 19:25:21', '0', '358332481058897920', '123456', '6D3816FBB1F3B47456AB7E4C843D89D2', '796994', '2017-09-15 19:25:21');
INSERT INTO `ucenter_user` VALUES ('2017-09-16 13:27:31', '0', '358604818052284416', '7040210', '8CB49AE81DCDD89FDE30F732EA6517A9', '821927', '2017-09-16 13:27:31');
INSERT INTO `ucenter_user` VALUES ('2017-09-17 23:48:12', '0', '359123404479004672', '664855665', '0C985387EDEC2D24F86BA107FF6C9B2C', '337538', '2017-09-17 23:48:12');

-- ----------------------------
-- Table structure for ucenter_user_role
-- ----------------------------
DROP TABLE IF EXISTS `ucenter_user_role`;
CREATE TABLE `ucenter_user_role` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 0为正常 1为删除 2为封存',
  `user_role_id` bigint(18) NOT NULL COMMENT '用户角色主键',
  `pk_role` bigint(18) DEFAULT NULL COMMENT '角色主键',
  `pk_user` bigint(18) DEFAULT NULL COMMENT '用户主键',
  PRIMARY KEY (`user_role_id`),
  KEY `pk_user` (`pk_role`,`pk_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色表';

-- ----------------------------
-- Records of ucenter_user_role
-- ----------------------------
INSERT INTO `ucenter_user_role` VALUES ('2017-09-15 19:25:21', '0', '358332481524465664', '358331074419359744', '358332481058897920');
INSERT INTO `ucenter_user_role` VALUES ('2017-09-16 13:27:32', '0', '358604821156069376', '358331696208150528', '358604818052284416');
INSERT INTO `ucenter_user_role` VALUES ('2017-09-17 23:48:12', '0', '359123405057818624', '358331696208150528', '359123404479004672');
