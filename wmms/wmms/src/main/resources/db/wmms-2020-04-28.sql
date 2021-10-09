/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.12-log : Database - wmms_db
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`wmms_db` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `wmms_db`;

/*Table structure for table `auth_permission` */

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

/*Data for the table `auth_permission` */

insert  into `auth_permission`(`id`,`permission_name`,`service_prefix`,`method`,`uri`,`valid`,`create_time`,`update_time`,`operate_id`) values (1,'查看auth服务用户权限','auth','GET','auth/getAuthUser',1,'2019-07-07','2019-07-07',NULL),(2,'查看auth服务hello权限','auth','GET','auth/hello',1,'2019-07-07','2019-07-07',NULL),(3,'查看api服务hello权限','api','GET','hello',1,'2019-07-07','2019-07-07',NULL);

/*Table structure for table `dictionary` */

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据字典';

/*Data for the table `dictionary` */

/*Table structure for table `oauth_client_details` */

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

/*Data for the table `oauth_client_details` */

insert  into `oauth_client_details`(`client_id`,`resource_ids`,`client_secret`,`scope`,`authorized_grant_types`,`web_server_redirect_uri`,`authorities`,`access_token_validity`,`refresh_token_validity`,`additional_information`,`autoapprove`) values ('client_1','wmms','$2a$10$BurTWIy5NTF9GJJH4magz.9Bd4bBurWYG8tmXxeQh1vs7r/wnCFG2','read,write,all','password,client_credentials,refresh_token',NULL,'',259200,259200,NULL,NULL),('client_2','wmms','$2a$10$BurTWIy5NTF9GJJH4magz.9Bd4bBurWYG8tmXxeQh1vs7r/wnCFG2','read,write,all','password,refresh_token,client_credentials',NULL,'',259200,259200,NULL,NULL);

/*Table structure for table `oauth_resource` */

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

/*Data for the table `oauth_resource` */

/*Table structure for table `sys_menu` */

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `id` varchar(32) NOT NULL COMMENT '编号',
  `href` varchar(200) DEFAULT NULL COMMENT '链接',
  `icon` varchar(200) DEFAULT NULL COMMENT '图标',
  `label` varchar(200) DEFAULT NULL COMMENT '标签',
  `name` varchar(200) DEFAULT NULL COMMENT '名称',
  `no` varchar(50) DEFAULT NULL COMMENT '菜单编号',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '直接上级',
  `parent_ids` varchar(500) DEFAULT NULL COMMENT '所有上级',
  `is_valid` int(1) DEFAULT NULL COMMENT '是否有效（0:否；1:是）',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` varchar(50) DEFAULT NULL COMMENT '更新时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` varchar(50) DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_p4rcp0389b4xd2kcuxfnm1ll0` (`no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_menu` */

insert  into `sys_menu`(`id`,`href`,`icon`,`label`,`name`,`no`,`parent_id`,`parent_ids`,`is_valid`,`update_user_id`,`update_time`,`create_user_id`,`create_time`,`remark`) values ('1','/','md-notifications','系统管理','系统管理','syscfg','-1','',1,NULL,NULL,NULL,NULL,'系统管理'),('2','/menu/**','md-notifications','菜单管理','菜单管理','menumgr','1','',1,NULL,NULL,NULL,NULL,'菜单管理'),('3','/diccfg/**','md-notifications','字典管理','字典管理','dicmgr','1',NULL,1,NULL,NULL,NULL,NULL,'字典管理'),('4','/','md-notifications','用户管理','用户管理','usermgr','-1','',1,NULL,NULL,NULL,NULL,'用户管理'),('5','/box/**','md-notifications','箱格管理','法官管理','judgemgr','4',NULL,1,NULL,NULL,NULL,NULL,'法官管理');

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `role_code` varchar(32) DEFAULT NULL COMMENT '角色代码',
  `role_name` varchar(200) DEFAULT NULL COMMENT '角色名称',
  `is_admin_manage` int(1) DEFAULT NULL COMMENT '是否管理房间（暂停、修改房间属性、销毁）',
  `is_admin_audio` int(1) DEFAULT NULL COMMENT '是否具有禁麦权限',
  `is_admin_video` int(1) DEFAULT NULL COMMENT '是否具有禁像权限',
  `is_admin_speak` int(1) DEFAULT NULL COMMENT '是否具有禁言权限',
  `is_admin_kick` int(1) DEFAULT NULL COMMENT '是否具有踢人权限',
  `is_admin_black` int(1) DEFAULT NULL COMMENT '是否具有拉黑权限',
  `is_admin_pause` int(1) DEFAULT NULL COMMENT '是否具有暂停权限',
  `audio_root` int(1) DEFAULT NULL COMMENT '是否有麦',
  `video_root` int(1) DEFAULT NULL COMMENT '是否有像',
  `speak_root` int(1) DEFAULT NULL COMMENT '是否有发言',
  `quality_root` int(1) DEFAULT NULL COMMENT '是否有切换画质',
  `screen_sharing_root` int(1) DEFAULT NULL COMMENT '是否共享屏幕',
  `state_root` int(1) DEFAULT NULL COMMENT '是否有推送状态',
  `pause_root` int(1) DEFAULT NULL COMMENT '是否有暂停',
  `is_valid` int(1) DEFAULT NULL COMMENT '是否有效（0:否；1:是）',
  `client_id` varchar(32) DEFAULT NULL COMMENT '客户端账号',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` varchar(50) DEFAULT NULL COMMENT '更新时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` varchar(50) DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_name` (`role_name`,`client_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1266210763752734723 DEFAULT CHARSET=utf8;

/*Data for the table `sys_role` */

insert  into `sys_role`(`id`,`role_code`,`role_name`,`is_admin_manage`,`is_admin_audio`,`is_admin_video`,`is_admin_speak`,`is_admin_kick`,`is_admin_black`,`is_admin_pause`,`audio_root`,`video_root`,`speak_root`,`quality_root`,`screen_sharing_root`,`state_root`,`pause_root`,`is_valid`,`client_id`,`update_user_id`,`update_time`,`create_user_id`,`create_time`,`remark`) values (3,'boom','boom',0,0,0,0,0,0,0,0,1,1,1,1,1,0,1,'client_1',NULL,'2020-05-19 15:08:42',NULL,'2020-05-09 10:40:53','测试新增角色'),(9,'admin','管理员',1,1,1,1,1,1,0,1,1,1,1,1,1,0,1,'client_1','1','2020-05-26 19:25:06','2','2020-05-08 11:47:52','总管理员'),(12,'10','测试角色10',0,0,0,0,0,0,0,0,1,1,1,1,1,0,1,'client_1',NULL,'2020-05-11 09:17:58','1','2020-05-11 09:17:58',''),(16,'promoter','发起人',1,1,1,1,1,1,0,1,1,1,1,1,1,0,1,'client_2','2','2020-05-26 13:21:43','2','2020-05-08 13:20:03',''),(21,'6','测试角色6',0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,'client_1',NULL,'2020-05-11 09:16:20','1','2020-05-11 09:16:20',''),(22,'ceshi1111','dasdad',0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'client_1','string','2020-05-25 14:27:48','string','2020-05-25 14:27:48','string'),(23,'admin','管理员',1,1,1,1,1,1,0,1,1,1,1,1,1,0,1,'client_2',NULL,'2020-05-26 13:21:30',NULL,'2020-05-26 13:21:30',NULL),(24,'suspects','嫌疑人',0,0,0,0,0,0,0,1,1,1,1,0,1,0,1,'client_2',NULL,'2020-05-26 13:23:55',NULL,'2020-05-26 13:23:55',NULL),(1265228100069883905,'clerk','书记员',1,1,1,1,1,1,0,1,0,1,1,1,1,0,1,'client_2',NULL,'2020-05-26 18:27:40',NULL,'2020-05-26 18:27:40',NULL),(1266210763752734722,'biubiubiu','测试',0,0,0,0,0,0,1,1,1,1,1,1,1,0,1,'client_2',NULL,'2020-05-29 13:14:59',NULL,'2020-05-29 11:32:25',NULL);

/*Table structure for table `sys_role_menus` */

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

/*Data for the table `sys_role_menus` */

insert  into `sys_role_menus`(`role_id`,`menus_id`,`update_user_id`,`update_time`,`create_user_id`,`create_time`) values ('1','1',NULL,NULL,NULL,NULL),('1','2',NULL,NULL,NULL,NULL),('1','3',NULL,NULL,NULL,NULL),('1','4',NULL,NULL,NULL,NULL),('1','5',NULL,NULL,NULL,NULL),('2','4',NULL,NULL,NULL,NULL),('2','5',NULL,NULL,NULL,NULL);

/*Table structure for table `t_sys_dict` */

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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='字典表';

/*Data for the table `t_sys_dict` */

insert  into `t_sys_dict`(`id`,`code`,`name`,`is_valid`,`serial`,`update_user_id`,`update_time`,`create_user_id`,`create_time`,`remark`) values (1,'is_valid','是否有效',1,0,NULL,NULL,NULL,NULL,'（无效、有效）'),(2,'user_type','人员类型',1,1,NULL,NULL,NULL,NULL,'（系统、用户）'),(3,'room_state','房间状态',1,2,NULL,NULL,NULL,NULL,'（已创建、可进入、正在进行、暂停、已销毁）'),(8,'token_state','令牌状态',1,3,NULL,NULL,NULL,NULL,NULL),(9,'person_type','人员角色类型',1,4,NULL,NULL,NULL,NULL,NULL),(10,'state','janus服务状态',1,5,NULL,NULL,NULL,NULL,NULL),(11,'is_not','是否',1,6,NULL,NULL,NULL,NULL,NULL),(12,'publisher_state','参与者状态',1,7,NULL,NULL,NULL,NULL,NULL),(13,'scope','权限范围',1,8,'','2020-04-22 14:33:48','','2020-04-22 14:33:48',''),(14,'authorized_grant_types','授权模式',1,9,'','2020-04-22 14:44:32','','2020-04-22 14:44:32','');

/*Table structure for table `t_sys_dict_item` */

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
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;

/*Data for the table `t_sys_dict_item` */

insert  into `t_sys_dict_item`(`id`,`dict_id`,`dict_code`,`value`,`label`,`serial`,`update_user_id`,`update_time`,`create_user_id`,`create_time`,`remark`) values (1,1,'is_valid','0','无效',1,NULL,NULL,NULL,NULL,NULL),(2,1,'is_valid','1','有效',2,NULL,NULL,NULL,NULL,NULL),(3,2,'user_type','0','系统用户',1,NULL,NULL,NULL,NULL,NULL),(4,2,'user_type','1','普通用户',2,NULL,NULL,NULL,NULL,NULL),(10,10,'room_state','created','已创建',1,'','2020-04-28 09:03:37',NULL,NULL,NULL),(11,3,'room_state','joinable','可进入',2,NULL,NULL,NULL,NULL,NULL),(12,3,'room_state','progressing','正在进行',3,NULL,NULL,NULL,NULL,NULL),(13,3,'room_state','stop','暂停',4,NULL,NULL,NULL,NULL,NULL),(14,3,'room_state','destroyed','已销毁',5,NULL,NULL,NULL,NULL,NULL),(15,8,'token_state','remove','移除',1,NULL,NULL,NULL,NULL,NULL),(16,8,'token_state','enable','可用',2,NULL,NULL,NULL,NULL,NULL),(17,8,'token_state','disable','不可用',3,NULL,NULL,NULL,NULL,NULL),(18,9,'person_type','admin','管理者',1,NULL,NULL,NULL,NULL,NULL),(19,9,'person_type','publisher','发布者',2,NULL,NULL,NULL,NULL,NULL),(20,9,'person_type','subscriber','旁观者',3,NULL,NULL,NULL,NULL,NULL),(21,9,'person_type','overseer','监督者',4,NULL,NULL,NULL,NULL,NULL),(22,10,'janus_state','0','离线',1,NULL,NULL,NULL,NULL,NULL),(23,10,'janus_state','1','在线',2,NULL,NULL,NULL,NULL,NULL),(24,11,'is_not','0','否',1,NULL,NULL,NULL,NULL,NULL),(25,11,'is_not','1','是',2,NULL,NULL,NULL,NULL,NULL),(26,12,'publisher_state','init','初始化',1,NULL,NULL,NULL,NULL,NULL),(27,12,'publisher_state','joined','已加入',2,NULL,NULL,NULL,NULL,NULL),(28,12,'publisher_state','kicked','被踢出',NULL,NULL,NULL,NULL,NULL,NULL),(29,12,'publisher_state','blacked','被拉黑',NULL,NULL,NULL,NULL,NULL,NULL),(30,13,'scope','read','只读',1,'','2020-04-22 14:43:17','','2020-04-22 14:43:17',''),(31,13,'scope','write','只写',2,'','2020-04-22 14:43:33','','2020-04-22 14:43:33',''),(32,13,'scope','all','全部',3,'','2020-04-22 14:43:49','','2020-04-22 14:43:49',''),(33,14,'authorized_grant_types','password','密码模式',1,'','2020-04-22 14:44:46','','2020-04-22 14:44:46',''),(34,14,'authorized_grant_types','client_credentials','客户端模式',2,'','2020-04-22 14:44:59','','2020-04-22 14:44:59',''),(35,14,'authorized_grant_types','refresh_token','刷新',3,'','2020-04-22 14:45:17','','2020-04-22 14:45:17',''),(36,14,'authorized_grant_types','authorization_code','授权码模式',4,'','2020-04-22 14:45:37','','2020-04-22 14:45:37',''),(37,37,'state','1','2',1,'','2020-04-27 14:44:26','','2020-04-27 14:44:26',''),(38,15,'state','1','2',1,'','2020-04-27 14:44:26','','2020-04-27 14:44:26',''),(39,15,'state','1','2',1,'','2020-04-27 14:44:26','','2020-04-27 14:44:26',''),(40,15,'state','2','3',1,'','2020-04-27 14:44:26','','2020-04-27 14:44:26',''),(41,15,'state','2','3',2,'','2020-04-27 15:02:22','','2020-04-27 15:02:22',''),(42,15,'state','1','1',1,'','2020-04-27 15:02:33','','2020-04-27 15:02:33',''),(43,43,'state','1234','2',1,'','2020-04-27 15:02:42','','2020-04-27 15:02:42','');

/*Table structure for table `t_sys_param` */

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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='系统参数';

/*Data for the table `t_sys_param` */

insert  into `t_sys_param`(`id`,`name`,`config_key`,`config_value`,`is_valid`,`serial`,`update_user_id`,`update_time`,`create_user_id`,`create_time`,`remark`) values (3,'用户默认登录密码','resetPassword','1111112',1,1,'','2020-04-22 09:51:31',NULL,NULL,'密码重置数值'),(7,'进入房间令牌长度','allowedLen','6',1,2,'1','2020-05-26 13:44:24','1',NULL,'进入房间令牌长度'),(8,'房间密码长度','roomPassWordLen','6',1,3,'','2020-04-22 09:52:37',NULL,NULL,NULL);

/*Table structure for table `t_sys_user` */

DROP TABLE IF EXISTS `t_sys_user`;

CREATE TABLE `t_sys_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(200) NOT NULL COMMENT '姓名',
  `card_id` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `birthday` varchar(8) DEFAULT NULL COMMENT '生日',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `sex` int(1) DEFAULT NULL COMMENT '性别',
  `login_account` varchar(200) NOT NULL COMMENT '登录账号',
  `login_pass` varchar(200) NOT NULL COMMENT '登录密码',
  `is_valid` int(1) NOT NULL COMMENT '是否有效（0:否；1:是）',
  `account_non_expired` int(1) NOT NULL COMMENT '过期性  1没过期0过期',
  `credentials_non_expired` int(1) NOT NULL COMMENT '有效性  1有效0失效',
  `account_non_locked` int(1) NOT NULL COMMENT '锁定性 1未锁定0锁定',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `update_user_id` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` varchar(50) DEFAULT NULL COMMENT '更新时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` varchar(50) DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='法院人员信息表';

/*Data for the table `t_sys_user` */

insert  into `t_sys_user`(`id`,`name`,`card_id`,`birthday`,`phone`,`sex`,`login_account`,`login_pass`,`is_valid`,`account_non_expired`,`credentials_non_expired`,`account_non_locked`,`email`,`update_user_id`,`update_time`,`create_user_id`,`create_time`,`remark`) values (1,'管理员','370481199106121850',NULL,'15668307338',NULL,'admin','$2a$10$AbVhZpRXHuXw.ps2Fc/ICOZzjPT5FKPn5YK87gi1a5ekbrQEC57iu',1,1,1,1,NULL,NULL,NULL,NULL,NULL,NULL),(2,'曹亚萍','142333199412121819',NULL,'18615561799',NULL,'yanwei','$2a$10$AbVhZpRXHuXw.ps2Fc/ICOZzjPT5FKPn5YK87gi1a5ekbrQEC57iu',1,1,1,1,NULL,NULL,NULL,NULL,NULL,NULL),(3,'常颖','372901198608053715',NULL,'18615561771',NULL,'user_2','123456',1,1,1,1,NULL,NULL,NULL,NULL,NULL,NULL),(4,'陈学军','370121196003187551',NULL,'18615561917',NULL,'ceshi1','S4S8S4S6SSSP458C5924',1,1,1,1,NULL,NULL,NULL,NULL,NULL,NULL),(5,'窦希梅','14010419690310226X',NULL,'18615561911',NULL,'ceshi2','S1468541424685416528',1,1,1,1,NULL,NULL,NULL,NULL,NULL,NULL),(6,'段军勇','370111196609271616',NULL,'15169130617',NULL,'ceshi3','S441428C4P41428C8752',1,1,1,1,NULL,NULL,NULL,NULL,NULL,NULL);

/* Function  structure for function  `fristPinyin` */

/*!50003 DROP FUNCTION IF EXISTS `fristPinyin` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `fristPinyin`(P_NAME VARCHAR(255)) RETURNS varchar(255) CHARSET utf8
BEGIN
    DECLARE V_RETURN VARCHAR(255);
    SET V_RETURN = ELT(INTERVAL(CONV(HEX(LEFT(CONVERT(P_NAME USING gbk),1)),16,10), 
        0xB0A1,0xB0C5,0xB2C1,0xB4EE,0xB6EA,0xB7A2,0xB8C1,0xB9FE,0xBBF7, 
        0xBFA6,0xC0AC,0xC2E8,0xC4C3,0xC5B6,0xC5BE,0xC6DA,0xC8BB,
        0xC8F6,0xCBFA,0xCDDA,0xCEF4,0xD1B9,0xD4D1),    
    'A','B','C','D','E','F','G','H','J','K','L','M','N','O','P','Q','R','S','T','W','X','Y','Z');
    RETURN V_RETURN;
END */$$
DELIMITER ;

/* Function  structure for function  `Num_char_extract` */

/*!50003 DROP FUNCTION IF EXISTS `Num_char_extract` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `Num_char_extract`(Varstring VARCHAR(100)CHARSET utf8, flag INT) RETURNS varchar(50) CHARSET utf8
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
    END */$$
DELIMITER ;

/* Function  structure for function  `pinyin` */

/*!50003 DROP FUNCTION IF EXISTS `pinyin` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `pinyin`(in_string VARCHAR(255)) RETURNS varchar(255) CHARSET utf8
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
END */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
