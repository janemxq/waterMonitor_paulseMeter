/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50721
Source Host           : 127.0.0.1:3306
Source Database       : wmms_db

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2020-08-23 18:46:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for auth_permission
-- ----------------------------
DROP TABLE IF EXISTS `auth_permission`;
CREATE TABLE `auth_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(50) DEFAULT NULL COMMENT '权限名称',
  `service_prefix` varchar(25) DEFAULT NULL COMMENT '前缀',
  `method` varchar(10) DEFAULT NULL COMMENT '请求方式 restful 模式 GET查看 POST 新增 PUT更新 DELETE 删除',
  `uri` varchar(50) DEFAULT NULL COMMENT '请求地址  资源',
  `valid` tinyint(4) DEFAULT NULL COMMENT '是否可用1是0否',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `update_time` date DEFAULT NULL COMMENT '更新时间',
  `operate_id` int(11) DEFAULT NULL COMMENT '操作用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='权限表';

-- ----------------------------
-- Records of auth_permission
-- ----------------------------
INSERT INTO `auth_permission` VALUES ('1', '查看auth服务用户权限', 'auth', 'GET', 'auth/getAuthUser', '1', '2019-07-07', '2019-07-07', null);
INSERT INTO `auth_permission` VALUES ('2', '查看auth服务hello权限', 'auth', 'GET', 'auth/hello', '1', '2019-07-07', '2019-07-07', null);
INSERT INTO `auth_permission` VALUES ('3', '查看api服务hello权限', 'api', 'GET', 'hello', '1', '2019-07-07', '2019-07-07', null);

-- ----------------------------
-- Table structure for dictionary
-- ----------------------------
DROP TABLE IF EXISTS `dictionary`;
CREATE TABLE `dictionary` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `parent_id` int(10) unsigned NOT NULL COMMENT '父ID',
  `type` varchar(50) NOT NULL COMMENT '字典类型',
  `item_name` varchar(100) NOT NULL COMMENT '显示名',
  `item_value` varchar(100) DEFAULT NULL COMMENT '存储值',
  `description` varchar(100) DEFAULT NULL COMMENT '描述说明',
  `extdata` varchar(200) DEFAULT NULL COMMENT '扩展JSON',
  `sort_id` smallint(6) NOT NULL DEFAULT '99' COMMENT '排序号',
  `is_editable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可改',
  `is_deletable` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否可删',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_directory` (`type`,`item_value`)
) ENGINE=InnoDB AUTO_INCREMENT=10069 DEFAULT CHARSET=utf8 COMMENT='数据字典';

-- ----------------------------
-- Records of dictionary
-- ----------------------------
INSERT INTO `dictionary` VALUES ('10014', '0', 'system', '是否有效', 'is_valid', '', null, '99', '0', '0', '0', '2020-06-08 09:58:04');
INSERT INTO `dictionary` VALUES ('10031', '10014', 'system', '12', '122', '1', null, '99', '0', '1', '1', '2020-06-09 02:48:38');
INSERT INTO `dictionary` VALUES ('10032', '10014', 'system', '无效', '0', null, null, '99', '0', '1', '0', '2020-06-09 02:50:12');
INSERT INTO `dictionary` VALUES ('10033', '10014', 'system', '有效', '1', null, null, '99', '0', '1', '0', '2020-06-09 02:50:23');
INSERT INTO `dictionary` VALUES ('10034', '0', 'system', '过期性', 'account_non_expired', '', null, '99', '0', '1', '0', '2020-06-09 12:53:03');
INSERT INTO `dictionary` VALUES ('10035', '10034', 'system', '过期', '0', null, null, '99', '0', '1', '0', '2020-06-09 12:53:34');
INSERT INTO `dictionary` VALUES ('10036', '10034', 'system', '没过去', '1', null, null, '99', '0', '1', '0', '2020-06-09 12:53:50');
INSERT INTO `dictionary` VALUES ('10037', '0', 'system', '有效性', 'credentials_non_expired', '', null, '99', '0', '1', '0', '2020-06-09 12:54:32');
INSERT INTO `dictionary` VALUES ('10038', '10037', 'system', '失效', '0', null, null, '99', '0', '1', '0', '2020-06-09 12:55:03');
INSERT INTO `dictionary` VALUES ('10039', '10037', 'system', '有效', '1', null, null, '99', '0', '1', '0', '2020-06-09 12:55:11');
INSERT INTO `dictionary` VALUES ('10040', '0', 'system', '锁定性', 'account_non_locked', '', null, '99', '0', '1', '0', '2020-06-09 12:58:12');
INSERT INTO `dictionary` VALUES ('10041', '10040', 'system', '锁定', '0', null, null, '99', '0', '1', '0', '2020-06-09 12:58:26');
INSERT INTO `dictionary` VALUES ('10042', '10040', 'system', '未锁定', '1', null, null, '99', '0', '1', '0', '2020-06-09 12:58:36');
INSERT INTO `dictionary` VALUES ('10043', '0', 'system', '用户类型', 'user_type', '', null, '99', '0', '1', '0', '2020-06-09 12:59:09');
INSERT INTO `dictionary` VALUES ('10044', '10043', 'system', '普通用户', '0', null, null, '99', '0', '1', '0', '2020-06-09 12:59:31');
INSERT INTO `dictionary` VALUES ('10045', '10043', 'system', '村级管理员', '1', null, null, '99', '0', '1', '0', '2020-06-09 12:59:49');
INSERT INTO `dictionary` VALUES ('10046', '10043', 'system', '集管管理员', '2', null, null, '99', '0', '1', '0', '2020-06-09 13:00:00');
INSERT INTO `dictionary` VALUES ('10047', '10043', 'system', '超级管理员', '3', null, null, '99', '0', '1', '0', '2020-06-09 13:03:25');
INSERT INTO `dictionary` VALUES ('10048', '0', 'system', '测试1', 'c', '', null, '99', '0', '1', '1', '2020-06-17 14:36:25');
INSERT INTO `dictionary` VALUES ('10049', '0', 'system', '11111', 'cc', '', null, '99', '0', '1', '1', '2020-06-17 14:49:38');
INSERT INTO `dictionary` VALUES ('10050', '10049', 'system', '11', '22', '44', null, '99', '0', '1', '1', '2020-06-17 14:49:46');
INSERT INTO `dictionary` VALUES ('10051', '0', 'system', 'aaa', 'a', '222', null, '99', '0', '1', '1', '2020-06-17 14:50:06');
INSERT INTO `dictionary` VALUES ('10052', '0', 'system', 'wzr', '123', '无', null, '99', '0', '1', '1', '2020-06-18 08:00:08');
INSERT INTO `dictionary` VALUES ('10053', '0', 'system', '121', '123', '1', null, '99', '0', '1', '1', '2020-06-18 08:05:17');
INSERT INTO `dictionary` VALUES ('10054', '10053', 'system', 'you', '1', '1', null, '99', '0', '1', '1', '2020-06-18 08:05:33');
INSERT INTO `dictionary` VALUES ('10055', '0', 'system', '支付方式', 'payment', '支付方式', null, '99', '0', '1', '0', '2020-06-22 06:56:10');
INSERT INTO `dictionary` VALUES ('10056', '10055', 'system', 'cash', '现金', null, null, '99', '0', '1', '0', '2020-06-22 06:56:55');
INSERT INTO `dictionary` VALUES ('10057', '10055', 'system', 'alipay', '支付宝', null, null, '99', '0', '1', '0', '2020-06-22 06:57:13');
INSERT INTO `dictionary` VALUES ('10058', '10055', 'system', '微信支付', 'wechatpay', null, null, '99', '0', '1', '0', '2020-06-22 06:57:37');
INSERT INTO `dictionary` VALUES ('10059', '0', 'system', '用水量异常', 'water_exception', '用水量异常的阈值', null, '99', '0', '1', '0', '2020-06-26 06:38:32');
INSERT INTO `dictionary` VALUES ('10060', '10059', 'system', 'water_exception_min', '2', '用水量异常阈值的上限。低于这个值，代表异常', null, '99', '0', '1', '0', '2020-06-26 06:39:37');
INSERT INTO `dictionary` VALUES ('10061', '10059', 'system', 'water_exception_max', '200', '用水量异常阈值的下限。大于这个值，代表异常', null, '99', '0', '1', '0', '2020-06-26 06:39:59');
INSERT INTO `dictionary` VALUES ('10062', '0', 'system', '缴费异常', 'payment_exception', '缴费异常的阈值', null, '99', '0', '1', '0', '2020-06-26 06:41:16');
INSERT INTO `dictionary` VALUES ('10063', '10062', 'system', 'pay_exception_min', '10', '缴费异常阈值的上限，小于这个阈值，代表异常', null, '99', '0', '1', '0', '2020-06-26 06:42:17');
INSERT INTO `dictionary` VALUES ('10064', '10062', 'system', 'pay_exception_max', '600', '缴费异常阈值的下限。大于这个值，代表异常', null, '99', '0', '1', '0', '2020-06-26 06:42:54');
INSERT INTO `dictionary` VALUES ('10065', '0', 'system', '未缴费异常', 'no_pay_exception', '未缴费异常阈值，方便统计久未缴费的用户', null, '99', '0', '1', '0', '2020-06-26 06:44:27');
INSERT INTO `dictionary` VALUES ('10066', '10065', 'system', 'no_pay_volume', '10', '用水量查过该值，并且没有缴费的，统计出来', null, '99', '0', '1', '0', '2020-06-26 06:46:01');
INSERT INTO `dictionary` VALUES ('10067', '10065', 'system', 'no_pay_long', '31', '超过设置的这个天数没有缴费的，统计出来', null, '99', '0', '1', '0', '2020-06-26 06:46:56');
INSERT INTO `dictionary` VALUES ('10068', '10055', 'system', 'balance', '余额缴费', '1', null, '99', '0', '1', '0', '2020-07-03 12:20:01');

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(250) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES ('client_1', 'wmms', '$2a$10$BurTWIy5NTF9GJJH4magz.9Bd4bBurWYG8tmXxeQh1vs7r/wnCFG2', 'read,write,all', 'password,client_credentials,refresh_token', null, '', '259200', '259200', null, null);
INSERT INTO `oauth_client_details` VALUES ('client_2', 'wmms', '$2a$10$BurTWIy5NTF9GJJH4magz.9Bd4bBurWYG8tmXxeQh1vs7r/wnCFG2', 'read,write,all', 'password,refresh_token,client_credentials', null, '', '259200', '259200', null, null);

-- ----------------------------
-- Table structure for oauth_resource
-- ----------------------------
DROP TABLE IF EXISTS `oauth_resource`;
CREATE TABLE `oauth_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) DEFAULT NULL COMMENT 'serviceName|Method|URI  资源名称|请求方式|URI (资源地址)',
  `need_permission` varchar(255) DEFAULT NULL COMMENT '资源需要权限 多个逗号隔开，且用#隔开 如 ROLE_ADMIN，资源名称|URI|Method  (restFul标识)',
  `create_time` date DEFAULT NULL,
  `update_time` date DEFAULT NULL,
  `operate_id` int(11) DEFAULT NULL COMMENT '操作用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限配置表';

-- ----------------------------
-- Records of oauth_resource
-- ----------------------------

-- ----------------------------
-- Table structure for pad_meter_info
-- ----------------------------
DROP TABLE IF EXISTS `pad_meter_info`;
CREATE TABLE `pad_meter_info` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `meter_yard_sn` varchar(255) NOT NULL DEFAULT '' COMMENT '表井标识（pad_meter_yard表sn）',
  `meter_yard_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '表井id（pad_meter_yard表id）',
  `yard_address` varchar(20) NOT NULL DEFAULT '' COMMENT '表井地址',
  `meter_mac` varchar(20) NOT NULL DEFAULT '' COMMENT '水表通道号（十六进制字符）',
  `remark` varchar(500) NOT NULL DEFAULT '' COMMENT '备注',
  `sn` varchar(80) NOT NULL DEFAULT '' COMMENT '水表的w唯一标识',
  `pulse_sum` int(80) unsigned NOT NULL DEFAULT '0' COMMENT '总脉冲',
  `water_sum` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '总的用水量',
  `meter_num` int(11) NOT NULL DEFAULT '0' COMMENT '码盘值',
  `get_data_time` varchar(50) NOT NULL DEFAULT '' COMMENT '最后获取数据的时间',
  `create_time` varchar(50) NOT NULL DEFAULT '',
  `update_time` varchar(50) NOT NULL DEFAULT '',
  `create_user_id` varchar(32) NOT NULL,
  `update_user_id` varchar(32) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sn` (`meter_yard_sn`),
  KEY `yard_id` (`meter_yard_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='pad端水表信息';

-- ----------------------------
-- Records of pad_meter_info
-- ----------------------------

-- ----------------------------
-- Table structure for pad_meter_yard
-- ----------------------------
DROP TABLE IF EXISTS `pad_meter_yard`;
CREATE TABLE `pad_meter_yard` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL DEFAULT '' COMMENT 'pad端表井名称',
  `sn` varchar(80) NOT NULL DEFAULT '' COMMENT '唯一字符串标识',
  `remark` varchar(500) NOT NULL DEFAULT '' COMMENT 'pad端表井备注',
  `address` varchar(32) NOT NULL DEFAULT '' COMMENT '表井地址',
  `create_time` varchar(50) NOT NULL DEFAULT '' COMMENT '创建时间',
  `update_time` varchar(50) NOT NULL DEFAULT '' COMMENT '更新时间',
  `create_user_id` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_user_id` varchar(32) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `sn` (`sn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='pad端表井';

-- ----------------------------
-- Records of pad_meter_yard
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `href` varchar(200) NOT NULL DEFAULT '' COMMENT '链接',
  `icon` varchar(200) NOT NULL DEFAULT '' COMMENT '图标',
  `label` varchar(200) NOT NULL DEFAULT '' COMMENT '标签',
  `name` varchar(200) NOT NULL DEFAULT '' COMMENT '名称',
  `no` varchar(50) NOT NULL DEFAULT '' COMMENT '菜单编号',
  `serial` int(11) NOT NULL DEFAULT '99' COMMENT '用于排序，越小越靠前',
  `parent_id` varchar(32) NOT NULL DEFAULT '' COMMENT '直接上级',
  `file_path` varchar(500) NOT NULL DEFAULT '' COMMENT '页面文件路径',
  `parent_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '所有上级',
  `is_valid` int(1) NOT NULL DEFAULT '1' COMMENT '是否有效（0:否；1:是）',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` varchar(50) DEFAULT NULL COMMENT '更新时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` varchar(50) DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_p4rcp0389b4xd2kcuxfnm1ll0` (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '/system', 'md-build', 'main', '系统管理', 'system', '1', '-1', 'main', '', '1', '', '2020-06-27 23:28:11', null, null, '系统管理');
INSERT INTO `sys_menu` VALUES ('2', '/org', 'ios-people', 'main', '组织架构管理', 'org', '2', '-1', 'main', '', '1', '', '2020-06-27 23:32:52', null, null, '组织架构管理');
INSERT INTO `sys_menu` VALUES ('3', 'Village', 'md-pricetags', 'Village', '村庄管理', 'Village', '201', '2', 'Village', '2', '1', '', '2020-06-27 23:33:41', null, null, '村庄管理');
INSERT INTO `sys_menu` VALUES ('4', 'TSysUser', 'ios-person', 'TSysUser', '用户管理', 'TSysUser', '202', '2', 'TSysUser', '2', '1', '', '2020-06-27 23:34:12', null, null, '用户管理');
INSERT INTO `sys_menu` VALUES ('5', '/WaterManage', 'md-phone-landscape', 'main', '设备管理', 'WaterManage', '3', '-1', 'main', '', '1', '', '2020-06-27 23:34:53', null, null, '设备管理');
INSERT INTO `sys_menu` VALUES ('6', 'WsGroup', 'md-notifications', 'WsGroup', '表井管理', 'WsGroup', '301', '5', 'WsGroup', '5', '1', '', '2020-06-27 23:35:17', null, null, '表井管理');
INSERT INTO `sys_menu` VALUES ('7', 'WsMeter', 'md-pie', 'WsMeter', '水表设备管理', 'WsMeter', '302', '5', 'WsMeter', '5', '1', '', '2020-06-27 23:36:46', null, null, '水表设备管理');
INSERT INTO `sys_menu` VALUES ('8', '/Pays', 'md-water', 'main', '用水缴费', 'Pays', '4', '-1', 'main', '', '1', '', '2020-06-27 23:37:13', null, null, '用水缴费');
INSERT INTO `sys_menu` VALUES ('9', '/Logs', 'ios-unlock', 'main', '日志管理', 'Logs', '5', '-1', 'main', '', '1', '', '2020-06-27 23:38:32', null, null, '日志管理');
INSERT INTO `sys_menu` VALUES ('10', 'Log', 'ios-book', 'Log', '读表记录', 'Log', '501', '9', 'Log', '9', '1', '', '2020-06-27 23:39:53', null, null, '读表记录');
INSERT INTO `sys_menu` VALUES ('14', '/demo1', 'demo1', 'demo1', 'demo1111', 'demo1', '11', '13', '', '13', '1', '', '2020-06-27 14:15:43', '', '2020-06-27 00:12:06', '22');
INSERT INTO `sys_menu` VALUES ('16', 'systemParam', 'md-code', 'systemParam', '参数管理', 'systemParam', '101', '1', 'systemParam', '1', '1', '', '2020-06-27 23:29:39', '', '2020-06-27 23:29:39', null);
INSERT INTO `sys_menu` VALUES ('17', 'DictPage', 'md-text', 'DictPage', '字典管理', 'DictPage', '102', '1', 'DictPage', '1', '1', '', '2020-06-27 23:30:15', '', '2020-06-27 23:30:15', null);
INSERT INTO `sys_menu` VALUES ('18', 'Role', 'ios-happy', 'Role', '角色管理', 'Role', '103', '1', 'Role', '1', '1', '', '2020-06-27 23:30:44', '', '2020-06-27 23:30:44', null);
INSERT INTO `sys_menu` VALUES ('19', 'rates', 'ios-water', 'rates', '收费标准', 'rates', '104', '1', 'rates', '1', '1', '', '2020-06-27 23:31:46', '', '2020-06-27 23:31:11', null);
INSERT INTO `sys_menu` VALUES ('20', 'menu', 'ios-menu', 'menu', '菜单管理', 'menu', '105', '1', 'menu', '1', '1', '', '2020-06-27 23:31:36', '', '2020-06-27 23:31:36', null);
INSERT INTO `sys_menu` VALUES ('21', 'Pay', 'md-planet', 'Pay', '用水缴费', 'Pay', '401', '8', 'Pay', '8', '1', '', '2020-06-27 23:37:58', '', '2020-06-27 23:37:58', '用水缴费');
INSERT INTO `sys_menu` VALUES ('22', 'PayLog', 'md-bookmarks', 'PayLog', '缴费记录', 'PayLog', '502', '9', 'PayLog', '9', '1', '', '2020-06-27 23:40:39', '', '2020-06-27 23:40:39', '缴费记录');
INSERT INTO `sys_menu` VALUES ('23', '/test', 'md-build', 'main', '测试页', 'test', '6', '-1', 'main', '', '1', '', '2020-06-27 23:41:16', '', '2020-06-27 23:41:16', '测试页');
INSERT INTO `sys_menu` VALUES ('24', 'testPage', 'md-home', 'testPage', '测试页', 'testPage', '601', '23', 'testPage', '23', '1', '', '2020-06-27 23:41:56', '', '2020-06-27 23:41:56', '测试页');
INSERT INTO `sys_menu` VALUES ('27', 'hht', 'ios-phone-portrait', 'hht', '手持终端', 'hht', '123', '5', 'hht', '5', '1', '', '2020-08-23 16:29:54', '', '2020-08-23 16:29:54', null);

-- ----------------------------
-- Table structure for sys_role_menus
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menus`;
CREATE TABLE `sys_role_menus` (
  `role_id` varchar(32) NOT NULL COMMENT '角色编号',
  `menus_id` varchar(32) NOT NULL COMMENT '菜单编号',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` varchar(50) DEFAULT NULL COMMENT '更新时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` varchar(50) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`,`menus_id`),
  KEY `FK3sq5xkthr6icwcyohtdoje586` (`menus_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_menus
-- ----------------------------
INSERT INTO `sys_role_menus` VALUES ('1', '1', '', '2020-06-28 11:13:33', '', '2020-06-28 11:13:33');
INSERT INTO `sys_role_menus` VALUES ('1', '16', '', '2020-06-28 11:13:33', '', '2020-06-28 11:13:33');
INSERT INTO `sys_role_menus` VALUES ('1', '17', '', '2020-06-28 11:13:33', '', '2020-06-28 11:13:33');
INSERT INTO `sys_role_menus` VALUES ('1', '18', '', '2020-06-28 11:13:33', '', '2020-06-28 11:13:33');
INSERT INTO `sys_role_menus` VALUES ('1', '19', '', '2020-06-28 11:13:33', '', '2020-06-28 11:13:33');
INSERT INTO `sys_role_menus` VALUES ('1', '2', '', '2020-06-28 11:13:33', '', '2020-06-28 11:13:33');
INSERT INTO `sys_role_menus` VALUES ('1', '20', '', '2020-06-28 11:13:33', '', '2020-06-28 11:13:33');
INSERT INTO `sys_role_menus` VALUES ('1', '21', '', '2020-06-28 11:13:33', '', '2020-06-28 11:13:33');
INSERT INTO `sys_role_menus` VALUES ('1', '3', '', '2020-06-28 11:13:33', '', '2020-06-28 11:13:33');
INSERT INTO `sys_role_menus` VALUES ('1', '4', '', '2020-06-28 11:13:33', '', '2020-06-28 11:13:33');
INSERT INTO `sys_role_menus` VALUES ('1', '5', '', '2020-06-28 11:13:33', '', '2020-06-28 11:13:33');
INSERT INTO `sys_role_menus` VALUES ('1', '6', '', '2020-06-28 11:13:33', '', '2020-06-28 11:13:33');
INSERT INTO `sys_role_menus` VALUES ('1', '7', '', '2020-06-28 11:13:33', '', '2020-06-28 11:13:33');
INSERT INTO `sys_role_menus` VALUES ('1', '8', '', '2020-06-28 11:13:33', '', '2020-06-28 11:13:33');
INSERT INTO `sys_role_menus` VALUES ('2', '4', null, null, null, null);
INSERT INTO `sys_role_menus` VALUES ('2', '5', null, null, null, null);
INSERT INTO `sys_role_menus` VALUES ('3', '1', '', '2020-08-22 12:46:45', '', '2020-08-22 12:46:45');
INSERT INTO `sys_role_menus` VALUES ('3', '10', '', '2020-08-22 12:46:45', '', '2020-08-22 12:46:45');
INSERT INTO `sys_role_menus` VALUES ('3', '16', '', '2020-08-22 12:46:45', '', '2020-08-22 12:46:45');
INSERT INTO `sys_role_menus` VALUES ('3', '17', '', '2020-08-22 12:46:45', '', '2020-08-22 12:46:45');
INSERT INTO `sys_role_menus` VALUES ('3', '18', '', '2020-08-22 12:46:45', '', '2020-08-22 12:46:45');
INSERT INTO `sys_role_menus` VALUES ('3', '19', '', '2020-08-22 12:46:45', '', '2020-08-22 12:46:45');
INSERT INTO `sys_role_menus` VALUES ('3', '2', '', '2020-08-22 12:46:45', '', '2020-08-22 12:46:45');
INSERT INTO `sys_role_menus` VALUES ('3', '20', '', '2020-08-22 12:46:45', '', '2020-08-22 12:46:45');
INSERT INTO `sys_role_menus` VALUES ('3', '21', '', '2020-08-22 12:46:45', '', '2020-08-22 12:46:45');
INSERT INTO `sys_role_menus` VALUES ('3', '22', '', '2020-08-22 12:46:45', '', '2020-08-22 12:46:45');
INSERT INTO `sys_role_menus` VALUES ('3', '23', '', '2020-08-22 12:46:45', '', '2020-08-22 12:46:45');
INSERT INTO `sys_role_menus` VALUES ('3', '24', '', '2020-08-22 12:46:45', '', '2020-08-22 12:46:45');
INSERT INTO `sys_role_menus` VALUES ('3', '25', '', '2020-08-22 12:46:45', '', '2020-08-22 12:46:45');
INSERT INTO `sys_role_menus` VALUES ('3', '26', '', '2020-08-22 12:46:45', '', '2020-08-22 12:46:45');
INSERT INTO `sys_role_menus` VALUES ('3', '3', '', '2020-08-22 12:46:45', '', '2020-08-22 12:46:45');
INSERT INTO `sys_role_menus` VALUES ('3', '4', '', '2020-08-22 12:46:45', '', '2020-08-22 12:46:45');
INSERT INTO `sys_role_menus` VALUES ('3', '5', '', '2020-08-22 12:46:45', '', '2020-08-22 12:46:45');
INSERT INTO `sys_role_menus` VALUES ('3', '6', '', '2020-08-22 12:46:45', '', '2020-08-22 12:46:45');
INSERT INTO `sys_role_menus` VALUES ('3', '7', '', '2020-08-22 12:46:45', '', '2020-08-22 12:46:45');
INSERT INTO `sys_role_menus` VALUES ('3', '8', '', '2020-08-22 12:46:45', '', '2020-08-22 12:46:45');
INSERT INTO `sys_role_menus` VALUES ('3', '9', '', '2020-08-22 12:46:45', '', '2020-08-22 12:46:45');

-- ----------------------------
-- Table structure for t_sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_dict`;
CREATE TABLE `t_sys_dict` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `code` varchar(200) NOT NULL COMMENT '字典代码',
  `name` varchar(200) DEFAULT NULL COMMENT '字典名称',
  `is_valid` int(1) DEFAULT NULL COMMENT '是否有效（0:否；1:是）',
  `serial` int(10) DEFAULT NULL COMMENT '顺序',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` varchar(50) DEFAULT NULL COMMENT '更新时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` varchar(50) DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COMMENT='字典表';

-- ----------------------------
-- Records of t_sys_dict
-- ----------------------------
INSERT INTO `t_sys_dict` VALUES ('28', 'is_valid', '是否有效', '1', '1', '', '2020-06-08 19:58:25', '', '2020-06-08 17:58:04', '');
INSERT INTO `t_sys_dict` VALUES ('32', 'account_non_expired', '过期性', '1', '2', '', '2020-06-09 20:53:03', '', '2020-06-09 20:53:03', '');
INSERT INTO `t_sys_dict` VALUES ('33', 'credentials_non_expired', '有效性', '1', '3', '', '2020-06-09 20:54:32', '', '2020-06-09 20:54:32', '');
INSERT INTO `t_sys_dict` VALUES ('34', 'account_non_locked', '锁定性', '1', '4', '', '2020-06-09 20:58:12', '', '2020-06-09 20:58:12', '');
INSERT INTO `t_sys_dict` VALUES ('35', 'user_type', '用户类型', '1', '5', '', '2020-06-09 20:59:08', '', '2020-06-09 20:59:08', '');
INSERT INTO `t_sys_dict` VALUES ('36', 'payment', '支付方式', '1', '1', '', '2020-06-22 14:56:09', '', '2020-06-22 14:56:09', '支付方式');
INSERT INTO `t_sys_dict` VALUES ('37', 'water_exception', '用水量异常', '1', '1', '', '2020-06-26 14:38:30', '', '2020-06-26 14:38:30', '用水量异常的阈值');
INSERT INTO `t_sys_dict` VALUES ('38', 'payment_exception', '缴费异常', '1', '1', '', '2020-06-26 14:41:14', '', '2020-06-26 14:41:14', '缴费异常的阈值');
INSERT INTO `t_sys_dict` VALUES ('39', 'no_pay_exception', '未缴费异常', '1', '1', '', '2020-06-26 14:44:25', '', '2020-06-26 14:44:25', '未缴费异常阈值，方便统计久未缴费的用户');

-- ----------------------------
-- Table structure for t_sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_dict_item`;
CREATE TABLE `t_sys_dict_item` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `dict_id` bigint(20) unsigned DEFAULT NULL COMMENT '字典编号',
  `dict_code` varchar(200) DEFAULT NULL COMMENT '字典代码',
  `value` varchar(200) DEFAULT NULL COMMENT '值',
  `label` varchar(200) DEFAULT NULL COMMENT '标签项',
  `serial` int(11) DEFAULT NULL COMMENT '顺序',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` varchar(50) DEFAULT NULL COMMENT '更新时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` varchar(50) DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_sys_dict_item
-- ----------------------------
INSERT INTO `t_sys_dict_item` VALUES ('65', '28', 'is_valid', '0', '无效', '1', '', '2020-06-09 10:50:12', '', '2020-06-09 10:50:12', null);
INSERT INTO `t_sys_dict_item` VALUES ('66', '28', 'is_valid', '1', '有效', '2', '', '2020-06-09 10:50:23', '', '2020-06-09 10:50:23', null);
INSERT INTO `t_sys_dict_item` VALUES ('67', '32', 'account_non_expired', '0', '过期', '1', '', '2020-06-09 20:53:33', '', '2020-06-09 20:53:33', null);
INSERT INTO `t_sys_dict_item` VALUES ('68', '32', 'account_non_expired', '1', '没过去', '2', '', '2020-06-09 20:53:49', '', '2020-06-09 20:53:49', null);
INSERT INTO `t_sys_dict_item` VALUES ('69', '33', 'credentials_non_expired', '0', '失效', '1', '', '2020-06-09 20:55:02', '', '2020-06-09 20:55:02', null);
INSERT INTO `t_sys_dict_item` VALUES ('70', '33', 'credentials_non_expired', '1', '有效', '2', '', '2020-06-09 20:55:11', '', '2020-06-09 20:55:11', null);
INSERT INTO `t_sys_dict_item` VALUES ('71', '34', 'account_non_locked', '0', '锁定', '1', '', '2020-06-09 20:58:25', '', '2020-06-09 20:58:25', null);
INSERT INTO `t_sys_dict_item` VALUES ('72', '34', 'account_non_locked', '1', '未锁定', '2', '', '2020-06-09 20:58:36', '', '2020-06-09 20:58:36', null);
INSERT INTO `t_sys_dict_item` VALUES ('73', '35', 'user_type', '0', '普通用户', '1', '', '2020-06-09 20:59:31', '', '2020-06-09 20:59:31', null);
INSERT INTO `t_sys_dict_item` VALUES ('74', '35', 'user_type', '1', '村级管理员', '2', '', '2020-06-09 20:59:49', '', '2020-06-09 20:59:49', null);
INSERT INTO `t_sys_dict_item` VALUES ('75', '35', 'user_type', '2', '集管管理员', '3', '', '2020-06-09 21:00:00', '', '2020-06-09 21:00:00', null);
INSERT INTO `t_sys_dict_item` VALUES ('76', '35', 'user_type', '3', '超级管理员', '4', '', '2020-06-09 21:03:25', '', '2020-06-09 21:03:25', null);
INSERT INTO `t_sys_dict_item` VALUES ('77', '36', 'payment', '现金', 'cash', '1', '', '2020-06-22 14:56:55', '', '2020-06-22 14:56:55', null);
INSERT INTO `t_sys_dict_item` VALUES ('78', '36', 'payment', '支付宝', 'alipay', '1', '', '2020-06-22 14:57:13', '', '2020-06-22 14:57:13', null);
INSERT INTO `t_sys_dict_item` VALUES ('79', '36', 'payment', '微信支付', 'wechatpay', '1', '', '2020-06-22 14:58:03', '', '2020-06-22 14:57:36', '支付方式');
INSERT INTO `t_sys_dict_item` VALUES ('80', '37', 'water_exception', '2', 'water_exception_min', '1', '', '2020-06-26 14:39:35', '', '2020-06-26 14:39:35', '用水量异常阈值的上限。低于这个值，代表异常');
INSERT INTO `t_sys_dict_item` VALUES ('81', '37', 'water_exception', '200', 'water_exception_max', '1', '', '2020-06-26 14:39:58', '', '2020-06-26 14:39:58', '用水量异常阈值的下限。大于这个值，代表异常');
INSERT INTO `t_sys_dict_item` VALUES ('82', '38', 'payment_exception', '10', 'pay_exception_min', '1', '', '2020-06-26 14:42:15', '', '2020-06-26 14:42:15', '缴费异常阈值的上限，小于这个阈值，代表异常');
INSERT INTO `t_sys_dict_item` VALUES ('83', '38', 'payment_exception', '600', 'pay_exception_max', '1', '', '2020-06-26 14:42:52', '', '2020-06-26 14:42:52', '缴费异常阈值的下限。大于这个值，代表异常');
INSERT INTO `t_sys_dict_item` VALUES ('84', '39', 'no_pay_exception', '10', 'no_pay_volume', '1', '', '2020-06-26 14:48:06', '', '2020-06-26 14:45:59', '用水量查过该值，并且没有缴费的，统计出来。只有no_pay_long条件也满足时才统计');
INSERT INTO `t_sys_dict_item` VALUES ('85', '39', 'no_pay_exception', '31', 'no_pay_long', '1', '', '2020-06-26 14:47:52', '', '2020-06-26 14:46:54', '超过设置的这个天数没有缴费的，统计出来。只有no_pay_volume条件也满足时才统计');
INSERT INTO `t_sys_dict_item` VALUES ('86', '36', 'payment', '余额缴费', 'balance', '1', '', '2020-07-03 20:20:00', '', '2020-07-03 20:20:00', '1');

-- ----------------------------
-- Table structure for t_sys_param
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_param`;
CREATE TABLE `t_sys_param` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(200) NOT NULL COMMENT '名称',
  `config_key` varchar(200) NOT NULL COMMENT '配置键',
  `config_value` varchar(200) DEFAULT NULL COMMENT '配置值',
  `is_valid` int(1) NOT NULL COMMENT '是否有效（0:否；1:是）',
  `serial` int(11) DEFAULT NULL COMMENT '顺序',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` varchar(50) DEFAULT NULL COMMENT '更新时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` varchar(50) DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(2000) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `config_key_index` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='系统参数';

-- ----------------------------
-- Records of t_sys_param
-- ----------------------------
INSERT INTO `t_sys_param` VALUES ('3', '用户默认登录密码', 'resetPassword', '111111', '1', '1', '', '2020-06-08 11:54:52', null, null, '密码重置数值');
INSERT INTO `t_sys_param` VALUES ('14', '帧头', 'header', 'FDDF', '1', '2', '', '2020-06-12 00:31:14', '', '2020-06-12 00:31:14', '串口通信的帧头');
INSERT INTO `t_sys_param` VALUES ('15', '源地址', 'fromAddress', '0000', '1', '3', null, '2020-06-12 00:31:14', null, '2020-06-12 00:31:14', 'PC机loRa模块地址');
INSERT INTO `t_sys_param` VALUES ('16', 'lora环境是否测试', 'isTest', '1', '1', '5', null, null, null, null, '是否lora测试模式1：是；0：否');
INSERT INTO `t_sys_param` VALUES ('17', '串口名', 'commName', 'COM5', '1', '4', null, null, null, null, null);
INSERT INTO `t_sys_param` VALUES ('18', '串口通信频率', 'baudrate', '9600', '1', '6', null, null, null, null, null);
INSERT INTO `t_sys_param` VALUES ('19', '集管端的通讯地址', 'CENTER_SERVER_URL', 'http://127.0.0.1:7772/wmms/tools/save', '1', '1', '', '2020-07-27 12:36:35', '', '2020-07-27 12:36:35', '1');
INSERT INTO `t_sys_param` VALUES ('20', '同步数据默认签名密钥', 'API_KEY', 'WBLiNG99Success', '1', '1', '', '2020-07-27 12:37:11', '', '2020-07-27 12:37:11', '1');
INSERT INTO `t_sys_param` VALUES ('21', '软件的运行模式', 'app_schema', 'schema_single', '1', '1', '', '2020-07-27 16:13:25', '', '2020-07-27 16:12:37', '值必须是schema_single（村委端）或者schema_center（集管端）');

-- ----------------------------
-- Table structure for t_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role`;
CREATE TABLE `t_sys_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `role_code` varchar(32) DEFAULT NULL COMMENT '角色代码',
  `role_name` varchar(200) DEFAULT NULL COMMENT '角色名称',
  `is_valid` int(1) DEFAULT NULL COMMENT '是否有效（0:否；1:是）',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` varchar(50) DEFAULT NULL COMMENT '更新时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` varchar(50) DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_name` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='角色';

-- ----------------------------
-- Records of t_sys_role
-- ----------------------------
INSERT INTO `t_sys_role` VALUES ('1', 'admin', '管理员', '1', '1', '2020-05-26 19:25:06', '2', '2020-05-08 11:47:52', '');
INSERT INTO `t_sys_role` VALUES ('2', 'centerAdmin', '集管管理员', '1', null, '2020-05-11 09:17:58', '1', '2020-05-11 09:17:58', '');
INSERT INTO `t_sys_role` VALUES ('3', 'superAdmin', '超级管理员', '1', '2', '2020-05-26 13:21:43', '2', '2020-05-08 13:20:03', '');
INSERT INTO `t_sys_role` VALUES ('4', 'user', '普通用户', '1', null, '2020-05-19 15:08:42', null, '2020-05-09 10:40:53', '');

-- ----------------------------
-- Table structure for t_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user`;
CREATE TABLE `t_sys_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(200) NOT NULL COMMENT '姓名',
  `id_card` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `login_account` varchar(200) NOT NULL COMMENT '登录账号',
  `login_pass` varchar(200) NOT NULL COMMENT '登录密码',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0普通用户1村级管理员2集管管理员',
  `is_valid` int(1) NOT NULL DEFAULT '1' COMMENT '是否有效（0:否；1:是）',
  `account_non_expired` int(1) DEFAULT '1' COMMENT '过期性  1没过期0过期',
  `credentials_non_expired` int(1) DEFAULT '1' COMMENT '有效性  1有效0失效',
  `account_non_locked` int(1) DEFAULT '1' COMMENT '锁定性 1未锁定0锁定',
  `branch_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '集管端用于记录各村用户的id',
  `village_id` int(11) NOT NULL DEFAULT '0' COMMENT '村id（管理员为0）',
  `village_sn` varchar(80) NOT NULL DEFAULT '' COMMENT '村标识',
  `village_name` varchar(200) NOT NULL DEFAULT '' COMMENT '村名',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` varchar(50) DEFAULT NULL COMMENT '更新时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` varchar(50) DEFAULT NULL COMMENT '创建时间',
  `sn` char(32) NOT NULL DEFAULT '' COMMENT '唯一标识',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sn` (`sn`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用水监测管理平台用户信息表';

-- ----------------------------
-- Records of t_sys_user
-- ----------------------------
INSERT INTO `t_sys_user` VALUES ('1', '超级管理员', null, '15668307338', 'superAdmin', '$2a$10$AbVhZpRXHuXw.ps2Fc/ICOZzjPT5FKPn5YK87gi1a5ekbrQEC57iu', '3', '1', '1', '1', '1', '0', '0', '', '', null, null, null, null, 'dc4d83d08917400fab66dade0896ff85', null);
INSERT INTO `t_sys_user` VALUES ('2', '管理员', '371581198911164759', '18615561793', 'admin', '$2a$10$llpgBva7YCxOv5H0LTEASesqUW9O8TyShPzZ7OlDSEfLBBJtz/l4S', '1', '1', '1', '1', '1', '0', '12', '222222', '东赵', '', '2020-07-06 22:08:31', null, null, '83334bcacbe249e89f124c4289caedc0', '今天是个好日子1');

-- ----------------------------
-- Table structure for ws_charge
-- ----------------------------
DROP TABLE IF EXISTS `ws_charge`;
CREATE TABLE `ws_charge` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_sn` varchar(32) NOT NULL DEFAULT '' COMMENT '用户标识',
  `meter_sn` varchar(80) NOT NULL DEFAULT '' COMMENT '水表标识',
  `village_sn` varchar(80) NOT NULL DEFAULT '' COMMENT '村标识',
  `user_name` varchar(80) NOT NULL DEFAULT '' COMMENT '用户名称',
  `phone` varchar(20) NOT NULL DEFAULT '' COMMENT '手机号',
  `account_num` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '收款金额',
  `cost` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '水费',
  `balance` decimal(14,2) NOT NULL DEFAULT '0.00' COMMENT '余额',
  `payment` varchar(20) NOT NULL DEFAULT '' COMMENT '支付方式',
  `create_time` varchar(50) NOT NULL DEFAULT '',
  `update_time` varchar(50) NOT NULL DEFAULT '',
  `create_user_id` varchar(32) NOT NULL DEFAULT '',
  `update_user_id` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户充值记录';

-- ----------------------------
-- Records of ws_charge
-- ----------------------------

-- ----------------------------
-- Table structure for ws_fee_log
-- ----------------------------
DROP TABLE IF EXISTS `ws_fee_log`;
CREATE TABLE `ws_fee_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户id',
  `user_sn` char(32) NOT NULL DEFAULT '' COMMENT '用户标识',
  `village_sn` varchar(80) NOT NULL DEFAULT '' COMMENT '村标识',
  `user_name` varchar(80) NOT NULL DEFAULT '' COMMENT '用户名',
  `phone` varchar(20) NOT NULL DEFAULT '' COMMENT '手机号',
  `meter_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '设备id',
  `meter_sn` varchar(80) NOT NULL DEFAULT '' COMMENT '设备编号',
  `water_sum` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '累计用水量',
  `pay_status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否缴费：0未缴费1已缴费-1已取消',
  `payment` varchar(20) NOT NULL DEFAULT '' COMMENT '缴费方式：现金，支付宝，微信',
  `pay_time` varchar(50) NOT NULL DEFAULT '' COMMENT '缴费时间',
  `start_num` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '上期水表示数',
  `end_num` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '本期水表示数',
  `fee_start_time` varchar(50) NOT NULL DEFAULT '' COMMENT '缴费开始时间',
  `fee_end_time` varchar(50) NOT NULL DEFAULT '' COMMENT '缴费结束时间',
  `fee_standard` varchar(50) NOT NULL DEFAULT '' COMMENT '缴费基准',
  `volume` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '用水量',
  `account` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '水费金额',
  `real_pay` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '收款金额',
  `balance` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '使用的余额数量',
  `create_time` varchar(50) NOT NULL DEFAULT '',
  `update_time` varchar(50) NOT NULL DEFAULT '',
  `create_user_id` varchar(32) NOT NULL DEFAULT '',
  `update_user_id` varchar(32) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `create_time` (`create_time`),
  KEY `meter_id` (`meter_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='缴费记录';

-- ----------------------------
-- Records of ws_fee_log
-- ----------------------------

-- ----------------------------
-- Table structure for ws_fee_standard
-- ----------------------------
DROP TABLE IF EXISTS `ws_fee_standard`;
CREATE TABLE `ws_fee_standard` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `display_name` varchar(200) NOT NULL DEFAULT '' COMMENT '收费名称',
  `sort_id` int(11) NOT NULL DEFAULT '99' COMMENT '用于排序的字段',
  `fee_standard` varchar(500) NOT NULL DEFAULT '' COMMENT '收费标准',
  `is_step` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否按照阶梯计价：1是，0不是',
  `sn` char(32) NOT NULL DEFAULT '' COMMENT '唯一标识',
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  `update_time` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COMMENT='水表的收费标准';

-- ----------------------------
-- Records of ws_fee_standard
-- ----------------------------
INSERT INTO `ws_fee_standard` VALUES ('15', '居民生活用水', '1', '1-144,4.2&145-288,5.6&289-100000,9.8', '1', 'rtrts', '', '', '2020-06-17 22:49:40', '2020-06-17 22:49:40');
INSERT INTO `ws_fee_standard` VALUES ('16', '执行居民用水价格非居民用水', '2', '1-100000,4.35', '1', '2', '', '', '2020-06-17 22:51:09', '2020-06-17 22:51:09');
INSERT INTO `ws_fee_standard` VALUES ('17', '非居民用水', '3', '1-100000,6.05', '1', '3', '', '', '2020-06-17 22:51:50', '2020-06-17 22:51:50');
INSERT INTO `ws_fee_standard` VALUES ('18', '特种用水', '4', '1-100000,16.3', '1', '4', '', '', '2020-06-17 22:52:22', '2020-06-17 22:52:22');
INSERT INTO `ws_fee_standard` VALUES ('20', '居民非阶梯收费', '5', '1-999999999,4.2', '0', '9470320a874f405cb9c24350a1f641d3', '', '', '2020-07-11 18:00:56', '2020-07-11 18:01:09');

-- ----------------------------
-- Table structure for ws_fee_standard_item
-- ----------------------------
DROP TABLE IF EXISTS `ws_fee_standard_item`;
CREATE TABLE `ws_fee_standard_item` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fee_standard_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '表ws_fee_standard的id',
  `start_num` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '计费区间开始项',
  `end_num` decimal(35,2) NOT NULL DEFAULT '0.00' COMMENT '收费标准',
  `is_step` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否按照阶梯计价：1是，0不是',
  `price` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '价格',
  `create_user_id` varchar(32) DEFAULT '',
  `update_user_id` varchar(32) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  `update_time` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COMMENT='水表的收费标准子项';

-- ----------------------------
-- Records of ws_fee_standard_item
-- ----------------------------
INSERT INTO `ws_fee_standard_item` VALUES ('23', '17', '0.00', '9223372036854775807.00', '1', '6.05', '', '', '2020-06-17 22:51:50', '2020-06-17 22:51:50');
INSERT INTO `ws_fee_standard_item` VALUES ('24', '18', '0.00', '9223372036854775807.00', '1', '16.30', '', '', '2020-06-17 22:52:23', '2020-06-17 22:52:23');
INSERT INTO `ws_fee_standard_item` VALUES ('25', '15', '1.00', '144.00', '1', '4.20', '', '', '2020-08-23 14:00:30', '2020-08-23 14:00:30');
INSERT INTO `ws_fee_standard_item` VALUES ('26', '15', '145.00', '288.00', '1', '5.60', '', '', '2020-08-23 14:00:30', '2020-08-23 14:00:30');
INSERT INTO `ws_fee_standard_item` VALUES ('27', '15', '289.00', '9223372036854775807.00', '1', '9.80', '', '', '2020-08-23 14:00:30', '2020-08-23 14:00:30');
INSERT INTO `ws_fee_standard_item` VALUES ('36', '16', '1.00', '9223372036854775807.00', '0', '222.00', '', '', '2020-08-23 15:50:34', '2020-08-23 15:50:34');

-- ----------------------------
-- Table structure for ws_group
-- ----------------------------
DROP TABLE IF EXISTS `ws_group`;
CREATE TABLE `ws_group` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `sn` varchar(80) NOT NULL DEFAULT '' COMMENT '表井标号',
  `address` varchar(80) NOT NULL DEFAULT '' COMMENT '表井终端设备地址16进制',
  `group_name` varchar(200) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '组名',
  `village_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '村id',
  `village_name` varchar(200) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '村名',
  `village_sn` varchar(80) NOT NULL DEFAULT '' COMMENT '村庄编码',
  `create_time` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
  `update_time` varchar(50) CHARACTER SET utf8mb4 DEFAULT '',
  `create_user_id` varchar(30) CHARACTER SET utf8mb4 DEFAULT '',
  `update_user_id` varchar(30) CHARACTER SET utf8mb4 DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `village_id` (`village_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='表井';

-- ----------------------------
-- Records of ws_group
-- ----------------------------

-- ----------------------------
-- Table structure for ws_meter
-- ----------------------------
DROP TABLE IF EXISTS `ws_meter`;
CREATE TABLE `ws_meter` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `sn` varchar(80) NOT NULL DEFAULT '' COMMENT '水表编号',
  `meter_init` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '水表初始值（用水量）',
  `meter_num` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '累计用水量',
  `water_sum` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '码盘值',
  `user_name` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `village_name` varchar(200) NOT NULL DEFAULT '' COMMENT '村名',
  `village_id` int(11) NOT NULL DEFAULT '0' COMMENT '村id',
  `blance` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '余额',
  `branch_id` bigint(20) DEFAULT '0' COMMENT '集管端用于记录各村记录id',
  `group_id` int(11) NOT NULL DEFAULT '0' COMMENT '表井id',
  `meter_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '水表用途的类型，管理ws_fee_standard表的id',
  `activation_time` varchar(10) NOT NULL DEFAULT '' COMMENT '设备启用时间，形式必须为2020-01-09',
  `group_name` varchar(200) NOT NULL DEFAULT '' COMMENT '表井名称',
  `group_sn` varchar(80) NOT NULL DEFAULT '' COMMENT '表井标识',
  `village_sn` varchar(80) NOT NULL DEFAULT '' COMMENT '村的唯一标识',
  `user_sn` char(32) NOT NULL DEFAULT '' COMMENT '用户编号',
  `remark` varchar(200) NOT NULL DEFAULT '' COMMENT '备注',
  `meter_type_sn` char(32) NOT NULL DEFAULT '' COMMENT '收费标准编号',
  `online` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否在线1在线0 不在线',
  `pulse` varchar(50) NOT NULL DEFAULT '' COMMENT '脉冲（多少脉冲对应 1 方水）',
  `pulse_sum` int(11) NOT NULL DEFAULT '0' COMMENT '脉冲总值',
  `mac_sn` varchar(80) NOT NULL DEFAULT '' COMMENT '表井通道号',
  `read_time` varchar(50) NOT NULL DEFAULT '' COMMENT '水表最新抄表时间',
  `balance_version` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '用于余额的乐观锁',
  `balance` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '余额',
  `create_time` varchar(50) NOT NULL DEFAULT '',
  `update_time` varchar(50) NOT NULL DEFAULT '',
  `create_user_id` varchar(32) NOT NULL DEFAULT '',
  `update_user_id` varchar(32) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `sn` (`sn`),
  KEY `village_sn` (`village_sn`),
  KEY `group_sn` (`group_sn`),
  KEY `user_sn` (`user_sn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='水表设备';

-- ----------------------------
-- Records of ws_meter
-- ----------------------------

-- ----------------------------
-- Table structure for ws_meter_reader
-- ----------------------------
DROP TABLE IF EXISTS `ws_meter_reader`;
CREATE TABLE `ws_meter_reader` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户id',
  `user_name` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  `meter_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '设备id',
  `meter_sn` varchar(80) NOT NULL DEFAULT '' COMMENT '设备标识',
  `village_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '村id',
  `village_name` varchar(50) NOT NULL DEFAULT '' COMMENT '村名',
  `group_id` bigint(11) NOT NULL DEFAULT '0' COMMENT '组（表井）id',
  `group_name` varchar(500) NOT NULL DEFAULT '' COMMENT '表名名称',
  `num` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '水表读数',
  `volume` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '用水量',
  `group_sn` varchar(80) NOT NULL DEFAULT '' COMMENT '表井标识',
  `user_sn` char(32) NOT NULL DEFAULT '' COMMENT '用户标识',
  `create_time` varchar(50) NOT NULL DEFAULT '' COMMENT '抄表时间',
  `village_sn` varchar(80) NOT NULL DEFAULT '' COMMENT '村标识',
  `update_time` varchar(50) NOT NULL DEFAULT '',
  `create_user_id` varchar(32) DEFAULT NULL,
  `update_user_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `meter_sn` (`meter_sn`),
  KEY `user_sn` (`user_sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='水表读数（用水记录）';

-- ----------------------------
-- Records of ws_meter_reader
-- ----------------------------

-- ----------------------------
-- Table structure for ws_syn
-- ----------------------------
DROP TABLE IF EXISTS `ws_syn`;
CREATE TABLE `ws_syn` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `data_type` varchar(200) NOT NULL DEFAULT '' COMMENT '数据类型（class name）',
  `branch_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '对应的数据记录的id',
  `create_time` varchar(50) NOT NULL DEFAULT '' COMMENT '创建时间（同步时间）',
  `update_time` varchar(50) NOT NULL DEFAULT '' COMMENT '更新时间',
  `create_user_id` varchar(50) NOT NULL DEFAULT '',
  `update_user_id` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `data_type` (`data_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='同步记录';

-- ----------------------------
-- Records of ws_syn
-- ----------------------------

-- ----------------------------
-- Table structure for ws_village
-- ----------------------------
DROP TABLE IF EXISTS `ws_village`;
CREATE TABLE `ws_village` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `village_name` varchar(200) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '名称',
  `sn` varchar(80) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '村落编号',
  `address` varchar(200) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '地址',
  `sort_id` int(11) NOT NULL DEFAULT '99' COMMENT '排序字段',
  `online` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否在线',
  `create_user_id` varchar(30) CHARACTER SET utf8mb4 DEFAULT '',
  `update_user_id` varchar(30) CHARACTER SET utf8mb4 DEFAULT '',
  `create_time` varchar(50) CHARACTER SET utf8mb4 DEFAULT '',
  `update_time` varchar(50) CHARACTER SET utf8mb4 DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='村庄';

-- ----------------------------
-- Records of ws_village
-- ----------------------------

-- ----------------------------
-- Function structure for fristPinyin
-- ----------------------------
DROP FUNCTION IF EXISTS `fristPinyin`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `fristPinyin`(P_NAME VARCHAR(255)) RETURNS varchar(255) CHARSET utf8
BEGIN
    DECLARE V_RETURN VARCHAR(255);
    SET V_RETURN = ELT(INTERVAL(CONV(HEX(LEFT(CONVERT(P_NAME USING gbk),1)),16,10), 
        0xB0A1,0xB0C5,0xB2C1,0xB4EE,0xB6EA,0xB7A2,0xB8C1,0xB9FE,0xBBF7, 
        0xBFA6,0xC0AC,0xC2E8,0xC4C3,0xC5B6,0xC5BE,0xC6DA,0xC8BB,
        0xC8F6,0xCBFA,0xCDDA,0xCEF4,0xD1B9,0xD4D1),    
    'A','B','C','D','E','F','G','H','J','K','L','M','N','O','P','Q','R','S','T','W','X','Y','Z');
    RETURN V_RETURN;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for Num_char_extract
-- ----------------------------
DROP FUNCTION IF EXISTS `Num_char_extract`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `Num_char_extract`(Varstring VARCHAR(100)CHARSET utf8, flag INT) RETURNS varchar(50) CHARSET utf8
BEGIN
	DECLARE len INT DEFAULT 0;
	DECLARE Tmp VARCHAR(100) DEFAULT '';
	SET len=CHAR_LENGTH(Varstring);
	IF flag = 0 
	THEN
		WHILE len > 0 DO
		IF MID(Varstring,len,1)REGEXP'[0-9]' THEN
		SET Tmp=CONCAT(Tmp,MID(Varstring,len,1));
		END IF;
		SET len = len - 1;
		END WHILE;
	ELSEIF flag=1
	THEN
		WHILE len > 0 DO
		IF (MID(Varstring,len,1)REGEXP '[a-zA-Z]') 
		THEN
		SET Tmp=CONCAT(Tmp,MID(Varstring,len,1));
		END IF;
		SET len = len - 1;
		END WHILE;
	ELSEIF flag=2
	THEN
		WHILE len > 0 DO
		IF ( (MID(Varstring,len,1)REGEXP'[0-9]')
		OR (MID(Varstring,len,1)REGEXP '[a-zA-Z]') ) 
		THEN
		SET Tmp=CONCAT(Tmp,MID(Varstring,len,1));
		END IF;
		SET len = len - 1;
		END WHILE;
	ELSEIF flag=3
	THEN
		WHILE len > 0 DO
		IF NOT (MID(Varstring,len,1)REGEXP '^[u0391-uFFE5]')
		THEN
		SET Tmp=CONCAT(Tmp,MID(Varstring,len,1));
		END IF;
		SET len = len - 1;
		END WHILE;
	ELSE 
		SET Tmp = 'Error: The second paramter should be in (0,1,2,3)';
		RETURN Tmp;
	END IF;
	RETURN REVERSE(Tmp);
    END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for pinyin
-- ----------------------------
DROP FUNCTION IF EXISTS `pinyin`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `pinyin`(in_string VARCHAR(255)) RETURNS varchar(255) CHARSET utf8
BEGIN  
DECLARE tmp_str VARCHAR(21845) CHARSET gbk DEFAULT '' ;   
DECLARE tmp_len SMALLINT DEFAULT 0;  
DECLARE tmp_loc SMALLINT DEFAULT 0;  
DECLARE tmp_char VARCHAR(2) CHARSET gbk DEFAULT '';  
DECLARE tmp_rs VARCHAR(21845)CHARSET gbk DEFAULT '';  
DECLARE tmp_cc VARCHAR(2) CHARSET gbk DEFAULT '';  
SET tmp_str = in_string;  
SET tmp_len = LENGTH(tmp_str);  
WHILE tmp_len > 0 DO   
SET tmp_char = LEFT(tmp_str,1);  
SET tmp_cc = tmp_char;  
SET tmp_loc=INTERVAL(CONV(HEX(tmp_char),16,10),0xB0A1,0xB0C5,0xB2C1,0xB4EE,0xB6EA,0xB7A2,0xB8C1,0xB9FE,0xBBF7,0xBFA6,0xC0AC  
,0xC2E8,0xC4C3,0xC5B6,0xC5BE,0xC6DA,0xC8BB,0xC8F6,0xCBFA,0xCDDA ,0xCEF4,0xD1B9,0xD4D1);  
IF (LENGTH(tmp_char)>1 AND tmp_loc>0 AND tmp_loc<24) THEN  
SELECT ELT(tmp_loc,'A','B','C','D','E','F','G','H','J','K','L','M','N','O','P','Q','R','S','T','W','X','Y','Z') INTO tmp_cc;   
END IF;  
SET tmp_rs = CONCAT(tmp_rs,tmp_cc);  
SET tmp_str = SUBSTRING(tmp_str,2);  
SET tmp_len = LENGTH(tmp_str);  
END WHILE;  
RETURN tmp_rs;  
END
;;
DELIMITER ;
