/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50631
Source Host           : localhost:3306
Source Database       : super_boot_base

Target Server Type    : MYSQL
Target Server Version : 50631
File Encoding         : 65001

Date: 2017-09-26 11:00:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for base_api
-- ----------------------------
DROP TABLE IF EXISTS `base_api`;
CREATE TABLE `base_api` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 0为正常 1为删除 2为封存',
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
-- Records of base_api
-- ----------------------------

-- ----------------------------
-- Table structure for base_api_role
-- ----------------------------
DROP TABLE IF EXISTS `base_api_role`;
CREATE TABLE `base_api_role` (
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间戳',
  `dr` int(11) DEFAULT '0' COMMENT '状态标识 0为正常 1为删除 2为封存',
  `pk_api_role` bigint(18) NOT NULL COMMENT '授权主键',
  `pk_api` bigint(18) DEFAULT NULL COMMENT '接口主键',
  `pk_role` bigint(18) DEFAULT NULL COMMENT '角色主键',
  PRIMARY KEY (`pk_api_role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口授权角色表';

-- ----------------------------
-- Records of base_api_role
-- ----------------------------
