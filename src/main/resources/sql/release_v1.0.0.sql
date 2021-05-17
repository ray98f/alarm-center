-- Host: frp    Database: alarm_center
-- ------------------------------------------------------
-- Server version	8.0.22
DROP DATABASE IF EXISTS `alarm_center`;
CREATE DATABASE `alarm_center` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;

use alarm_center;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `alarm_code`
--

DROP TABLE IF EXISTS `alarm_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alarm_code` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `exe_id` bigint DEFAULT NULL COMMENT '第三方内告警码id',
  `system_id` bigint NOT NULL COMMENT '系统id',
  `position_id` bigint NOT NULL COMMENT '位置id',
  `code` int unsigned NOT NULL COMMENT '告警码',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '告警名称',
  `reason` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '告警原因',
  `level_id` bigint NOT NULL COMMENT '告警级别',
  `handling_opinions` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '处理意见',
  `is_locked` tinyint(1) unsigned zerofill NOT NULL DEFAULT '0' COMMENT '是否锁定',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='告警码';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alarm_code`
--

LOCK TABLES `alarm_code` WRITE;
/*!40000 ALTER TABLE `alarm_code` DISABLE KEYS */;
/*!40000 ALTER TABLE `alarm_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alarm_error`
--

DROP TABLE IF EXISTS `alarm_error`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alarm_error` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `alarm_time` timestamp NULL DEFAULT NULL COMMENT '告警时间',
  `system_code` bigint unsigned DEFAULT NULL COMMENT '系统编号',
  `line_code` bigint DEFAULT NULL COMMENT '线路编号',
  `position_code` bigint DEFAULT NULL COMMENT '车站编号',
  `device_code` bigint unsigned DEFAULT NULL COMMENT '设备编号',
  `slot_code` bigint unsigned DEFAULT NULL COMMENT '槽位编号',
  `alarm_code` int unsigned DEFAULT NULL COMMENT '告警码',
  `ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'ip',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  `error_content` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '出错原因',
  `is_deleted` int unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `alarm_error_alarm_time_IDX` (`alarm_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='告警异常';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alarm_error`
--

LOCK TABLES `alarm_error` WRITE;
/*!40000 ALTER TABLE `alarm_error` DISABLE KEYS */;
/*!40000 ALTER TABLE `alarm_error` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alarm_history`
--

DROP TABLE IF EXISTS `alarm_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alarm_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `subsystem_id` bigint NOT NULL COMMENT '子系统id',
  `subsystem_code` bigint DEFAULT NULL COMMENT '子系统id',
  `line_id` bigint NOT NULL COMMENT '线路id',
  `line_code` bigint DEFAULT NULL COMMENT '线路id',
  `site_id` bigint NOT NULL COMMENT '站点',
  `site_code` bigint DEFAULT NULL COMMENT '站点',
  `device_id` bigint NOT NULL COMMENT '设备',
  `device_code` bigint DEFAULT NULL COMMENT '设备',
  `slot_id` bigint NOT NULL COMMENT '槽位',
  `slot_code` bigint DEFAULT NULL COMMENT '槽位',
  `alarm_code` bigint NOT NULL COMMENT '告警码',
  `alarm_code_id` bigint DEFAULT NULL COMMENT '告警码',
  `alarm_level` tinyint NOT NULL COMMENT '告警等级',
  `alarm_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '告警名称',
  `alarm_reason` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '告警原因',
  `first_time` timestamp NULL DEFAULT NULL COMMENT '第一次告警时间',
  `final_time` timestamp NULL DEFAULT NULL COMMENT '最后一次告警时间',
  `frequency` int NOT NULL DEFAULT '0' COMMENT '告警次数',
  `alarm_volume` tinyint(1) NOT NULL DEFAULT '1' COMMENT '告警音量',
  `is_upgrade` tinyint(1) NOT NULL DEFAULT '1' COMMENT '告警升级',
  `is_mute` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否静音',
  `is_ring` tinyint(1) NOT NULL DEFAULT '1' COMMENT '播放声音 1播放声音 0 不播放',
  `alarm_state` tinyint NOT NULL DEFAULT '0' COMMENT '告警状态(0待处理;1延迟告警;2手动确认;3自动确认;4手动过滤;5自动过滤;6手动清除;7自动清除)',
  `recovery_time` timestamp NULL DEFAULT NULL COMMENT '告警恢复时间',
  `delay_time` timestamp NULL DEFAULT NULL COMMENT '告警延迟时间',
  `alarm_frequency` int DEFAULT NULL COMMENT '告警升级频次',
  `frequency_time` bigint DEFAULT NULL COMMENT '告警升级频次单位时间',
  `experience_time` bigint DEFAULT NULL COMMENT '告警升级经历时间',
  `alarm_remark` varchar(300) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '告警备注',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='告警历史表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alarm_history`
--

LOCK TABLES `alarm_history` WRITE;
/*!40000 ALTER TABLE `alarm_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `alarm_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alarm_level`
--

DROP TABLE IF EXISTS `alarm_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alarm_level` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` tinyint NOT NULL COMMENT '1 紧急告警 2 重要告警 3 一般告警',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '告警级别名称',
  `color` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '告警级别色号',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='告警级别表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alarm_level`
--

LOCK TABLES `alarm_level` WRITE;
/*!40000 ALTER TABLE `alarm_level` DISABLE KEYS */;
INSERT INTO `alarm_level` VALUES (1,1,'紧急告警','#FF0000','2021-03-11 05:55:56','2021-04-02 07:43:38','1','admin',0),(2,2,'重要告警','#FF8A00','2021-03-11 05:56:15','2021-04-06 09:42:47','1','admin',0),(3,3,'一般告警','#F9F504','2021-03-11 05:56:28','2021-04-22 06:34:28','1','admin',0);
/*!40000 ALTER TABLE `alarm_level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alarm_message`
--

DROP TABLE IF EXISTS `alarm_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alarm_message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `alarm_id` bigint NOT NULL COMMENT '告警记录id',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '标题',
  `content` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '内容',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='告警记录附加信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alarm_message`
--

LOCK TABLES `alarm_message` WRITE;
/*!40000 ALTER TABLE `alarm_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `alarm_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alarm_rule`
--

DROP TABLE IF EXISTS `alarm_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alarm_rule` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` int NOT NULL COMMENT '告警类型（1-告警延迟规则;2-告警入库过滤规则;3-告警过滤规则;4-告警确认规则;5-告警清除规则;6-告警升级规则;7-告警前转规则）',
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '告警规则名称',
  `is_filter_system` tinyint unsigned DEFAULT NULL COMMENT '0 不过滤系统 1 过滤系统',
  `is_filter_position` tinyint unsigned DEFAULT NULL COMMENT '0 不过滤位置 1 过滤位置',
  `is_filter_device` tinyint unsigned DEFAULT NULL COMMENT '0 不过滤设备 1 过滤设备',
  `is_filter_alarm` tinyint unsigned DEFAULT NULL COMMENT '0 不过滤告警码 1 过滤告警码',
  `delay_time` bigint DEFAULT NULL COMMENT '告警延迟时间间隔',
  `frequency` int DEFAULT NULL COMMENT '单位时间告警次数限制',
  `frequency_time` bigint DEFAULT NULL COMMENT '告警次数限制单位时间',
  `experience_time` bigint DEFAULT NULL COMMENT '历时时间',
  `msg_config_id` bigint DEFAULT NULL COMMENT '前转规则消息推送配置id',
  `is_enable` tinyint(1) NOT NULL COMMENT '启用状态',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='告警规则';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alarm_rule`
--

LOCK TABLES `alarm_rule` WRITE;
/*!40000 ALTER TABLE `alarm_rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `alarm_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alarm_sound`
--

DROP TABLE IF EXISTS `alarm_sound`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alarm_sound` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `system_id` bigint NOT NULL COMMENT '系统id',
  `level_id` bigint NOT NULL COMMENT '告警级别id',
  `sound_file` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '语音文件',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='告警声音';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alarm_sound`
--

LOCK TABLES `alarm_sound` WRITE;
/*!40000 ALTER TABLE `alarm_sound` DISABLE KEYS */;
/*!40000 ALTER TABLE `alarm_sound` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `change_shifts`
--

DROP TABLE IF EXISTS `change_shifts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `change_shifts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `by_user_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `to_user_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='交接班管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `change_shifts`
--

LOCK TABLES `change_shifts` WRITE;
/*!40000 ALTER TABLE `change_shifts` DISABLE KEYS */;
/*!40000 ALTER TABLE `change_shifts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `device`
--

DROP TABLE IF EXISTS `device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '设备名称',
  `exe_id` bigint DEFAULT NULL COMMENT '该设备信息在客户端数据库中的id',
  `system_id` bigint NOT NULL COMMENT '所属系统id，subsystem表主键',
  `position_id` bigint NOT NULL COMMENT '设备位置id，position表主键',
  `device_code` int NOT NULL COMMENT '设备编号',
  `brand` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '品牌型号',
  `serial_num` varchar(125) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '设备串号',
  `description` varchar(125) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '设备描述',
  `manufacturer` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '设备厂商',
  `is_normal` tinyint(1) DEFAULT '0' COMMENT '是否正常',
  `is_locked` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='设备管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device`
--

LOCK TABLES `device` WRITE;
/*!40000 ALTER TABLE `device` DISABLE KEYS */;
/*!40000 ALTER TABLE `device` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `device_slot`
--

DROP TABLE IF EXISTS `device_slot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_slot` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `exe_id` bigint DEFAULT NULL COMMENT '该设备槽位信息在客户端数据库中的id',
  `system_id` bigint DEFAULT NULL COMMENT '所属系统id，subsystem表主键',
  `position_id` bigint DEFAULT NULL COMMENT '设备位置id，position表主键',
  `device_id` bigint NOT NULL COMMENT '设备id，device表主键',
  `slot_code` int NOT NULL COMMENT '槽位编号',
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '槽位名称',
  `is_locked` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否锁定',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='设备槽位表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device_slot`
--

LOCK TABLES `device_slot` WRITE;
/*!40000 ALTER TABLE `device_slot` DISABLE KEYS */;
/*!40000 ALTER TABLE `device_slot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dic`
--

DROP TABLE IF EXISTS `dic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dic` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(20) NOT NULL COMMENT '类型',
  `name` varchar(30) NOT NULL COMMENT '名称',
  `is_enable` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态：0-停用，1-启用',
  `description` varchar(255) DEFAULT NULL COMMENT '备注',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_by` varchar(32) NOT NULL COMMENT '创建人',
  `updated_by` varchar(32) NOT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dic`
--

LOCK TABLES `dic` WRITE;
/*!40000 ALTER TABLE `dic` DISABLE KEYS */;
INSERT INTO `dic` VALUES (1,'系统参数','告警状态',1,NULL,'2021-04-01 03:22:40','2021-04-01 03:22:40','admin','admin',0),(2,'系统参数','告警等级',1,NULL,'2021-04-01 03:22:58','2021-04-01 03:22:58','admin','admin',0);
/*!40000 ALTER TABLE `dic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dic_data`
--

DROP TABLE IF EXISTS `dic_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dic_data` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `key` smallint NOT NULL COMMENT '字典键值',
  `value` varchar(30) NOT NULL COMMENT '字典标签',
  `type` varchar(20) NOT NULL COMMENT '类型',
  `order` tinyint DEFAULT NULL COMMENT '字典排序',
  `description` varchar(40) DEFAULT NULL COMMENT '描述',
  `is_enable` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态：0-停用，1-启用',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_by` varchar(32) NOT NULL COMMENT '创建人',
  `updated_by` varchar(32) NOT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dic_data`
--

LOCK TABLES `dic_data` WRITE;
/*!40000 ALTER TABLE `dic_data` DISABLE KEYS */;
INSERT INTO `dic_data` VALUES (1,0,'待处理','告警状态',1,NULL,1,'2021-04-01 03:30:30','2021-04-01 03:30:30','admin','admin',0),(2,1,'延迟告警','告警状态',2,NULL,1,'2021-04-01 03:30:30','2021-04-01 03:30:30','admin','admin',0),(3,2,'手动确认','告警状态',3,NULL,1,'2021-04-01 03:30:30','2021-04-01 03:30:30','admin','admin',0),(4,3,'自动确认','告警状态',4,NULL,1,'2021-04-01 03:30:30','2021-04-01 03:30:30','admin','admin',0),(5,4,'手动过滤','告警状态',5,NULL,1,'2021-04-01 03:30:30','2021-04-01 03:30:30','admin','admin',0),(6,5,'自动过滤','告警状态',6,NULL,1,'2021-04-01 03:30:30','2021-04-01 03:30:30','admin','admin',0),(7,6,'手动清除','告警状态',7,NULL,1,'2021-04-01 03:30:30','2021-04-01 03:30:30','admin','admin',0),(8,7,'自动清除','告警状态',8,NULL,1,'2021-04-01 03:30:30','2021-04-01 03:30:30','admin','admin',0);
/*!40000 ALTER TABLE `dic_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `filter_alarm`
--

DROP TABLE IF EXISTS `filter_alarm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `filter_alarm` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `rule_id` bigint NOT NULL COMMENT '规则id',
  `alarm_code_id` bigint NOT NULL COMMENT '告警码id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='过滤告警';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `filter_alarm`
--

LOCK TABLES `filter_alarm` WRITE;
/*!40000 ALTER TABLE `filter_alarm` DISABLE KEYS */;
/*!40000 ALTER TABLE `filter_alarm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `filter_device`
--

DROP TABLE IF EXISTS `filter_device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `filter_device` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `rule_id` bigint NOT NULL COMMENT '规则id',
  `device_id` bigint NOT NULL COMMENT '设备id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='设备过滤';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `filter_device`
--

LOCK TABLES `filter_device` WRITE;
/*!40000 ALTER TABLE `filter_device` DISABLE KEYS */;
/*!40000 ALTER TABLE `filter_device` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `filter_position`
--

DROP TABLE IF EXISTS `filter_position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `filter_position` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `rule_id` bigint NOT NULL COMMENT '规则id',
  `position_id` bigint NOT NULL COMMENT '位置id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='位置过滤表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `filter_position`
--

LOCK TABLES `filter_position` WRITE;
/*!40000 ALTER TABLE `filter_position` DISABLE KEYS */;
/*!40000 ALTER TABLE `filter_position` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `filter_system`
--

DROP TABLE IF EXISTS `filter_system`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `filter_system` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `rule_id` bigint NOT NULL COMMENT '规则主键',
  `system_id` bigint NOT NULL COMMENT '系统主键',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统过滤表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `filter_system`
--

LOCK TABLES `filter_system` WRITE;
/*!40000 ALTER TABLE `filter_system` DISABLE KEYS */;
/*!40000 ALTER TABLE `filter_system` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单管理id',
  `is_catalog` tinyint NOT NULL DEFAULT '1' COMMENT '是否为目录',
  `catalog_id` bigint DEFAULT NULL COMMENT '目录id',
  `is_menu` tinyint NOT NULL DEFAULT '1' COMMENT '是否为菜单',
  `menu_id` bigint DEFAULT NULL COMMENT '菜单id',
  `menu_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '名称',
  `menu_type` tinyint DEFAULT NULL COMMENT '类型',
  `menu_sort` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '排序',
  `menu_value` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '路由地址',
  `role_identify` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '权限标识',
  `is_show` tinyint NOT NULL DEFAULT '0' COMMENT '是否显示',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='菜单管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,0,NULL,1,NULL,'告警信息',1,'1',NULL,NULL,0,'2021-03-03 07:38:14','2021-03-03 07:38:14','admin','admin',0),(3,1,1,0,NULL,'历史告警',2,'2','historyWarning',NULL,0,'2021-03-03 07:38:14','2021-03-03 07:38:14','admin','admin',0),(4,1,1,0,NULL,'异常告警',2,'3','abnormalWarning',NULL,0,'2021-03-03 07:38:14','2021-03-03 07:38:14','admin','admin',0),(5,0,NULL,1,NULL,'告警统计',1,'2',NULL,NULL,0,'2021-03-03 07:38:14','2021-03-03 07:38:14','admin','admin',0),(6,1,5,0,NULL,'告警数据总计',2,'1','totalStatistics',NULL,0,'2021-03-03 07:38:14','2021-03-03 07:38:14','admin','admin',0),(7,1,5,0,NULL,'按线路统计',2,'2','lineStatistics',NULL,0,'2021-03-03 07:38:14','2021-03-03 07:38:14','admin','admin',0),(8,1,5,0,NULL,'按系统统计',2,'3','sysStatistics',NULL,0,'2021-03-03 07:38:14','2021-03-03 07:38:14','admin','admin',0),(9,1,5,0,NULL,'按告警级别统计',2,'4','levelStatistics',NULL,0,'2021-03-03 07:38:14','2021-03-03 07:38:14','admin','admin',0),(10,1,5,0,NULL,'线路告警趋势',2,'5','lineTrend',NULL,0,'2021-03-03 07:38:14','2021-03-03 07:38:14','admin','admin',0),(11,1,5,0,NULL,'系统告警趋势',2,'6','sysTrend',NULL,0,'2021-03-03 07:38:14','2021-03-03 07:38:14','admin','admin',0),(12,1,5,0,NULL,'级别告警趋势',2,'7','levelTrend',NULL,0,'2021-03-03 07:38:14','2021-03-03 07:38:14','admin','admin',0),(13,1,5,0,NULL,'告警解决效率',2,'8','solveEfficiency',NULL,0,'2021-03-03 07:38:14','2021-03-03 07:38:14','admin','admin',0),(14,0,NULL,1,NULL,'告警管理',1,'3',NULL,NULL,0,'2021-03-03 07:38:54','2021-03-03 07:38:54','admin','admin',0),(15,1,14,0,NULL,'设备槽位配置',2,'1','slotSet',NULL,0,'2021-03-03 07:38:54','2021-03-03 07:38:54','admin','admin',0),(16,1,14,0,NULL,'告警规则配置',2,'2','warningRole',NULL,0,'2021-03-03 07:38:14','2021-04-20 09:45:34','admin','admin',0),(17,1,14,0,NULL,'告警级别配置',2,'3','waringLevel',NULL,0,'2021-03-03 07:38:14','2021-04-20 09:45:34','admin','admin',0),(18,1,14,0,NULL,'告警码配置',2,'4','warningCode',NULL,0,'2021-03-03 07:38:14','2021-04-20 09:45:34','admin','admin',0),(19,1,14,0,NULL,'SNMP槽位配置',2,'5','snmp',NULL,0,'2021-03-03 07:38:14','2021-04-19 06:07:36','admin','admin',0),(20,0,NULL,1,NULL,'设备管理',1,'4',NULL,NULL,0,'2021-03-03 07:38:54','2021-03-03 07:38:54','admin','admin',0),(21,1,20,0,NULL,'设备信息管理',2,'1','equipManage',NULL,0,'2021-03-03 07:38:14','2021-03-03 07:38:14','admin','admin',0),(22,0,NULL,1,NULL,'系统管理',1,'6',NULL,NULL,0,'2021-03-03 07:38:54','2021-03-31 03:27:03','admin','admin',0),(23,1,29,0,NULL,'子系统管理',2,'1','subSystem',NULL,0,'2021-03-03 07:38:14','2021-03-31 02:56:10','admin','admin',0),(24,1,29,0,NULL,'位置管理',2,'2','positionManage',NULL,0,'2021-03-03 07:38:14','2021-03-31 02:56:10','admin','admin',0),(25,1,22,0,NULL,'人员管理',2,'1','menberManage',NULL,0,'2021-03-03 07:38:14','2021-03-31 02:56:10','admin','admin',0),(26,1,22,0,NULL,'角色管理',2,'2','roleManage',NULL,0,'2021-03-03 07:38:14','2021-03-31 02:56:10','admin','admin',0),(27,1,22,0,NULL,'操作日志',2,'3','operateLog',NULL,0,'2021-03-03 07:38:14','2021-03-31 02:56:10','admin','admin',0),(28,1,22,0,NULL,'系统参数',2,'4','sysParams',NULL,0,'2021-03-03 07:38:14','2021-03-31 02:56:10','admin','admin',0),(29,0,NULL,1,NULL,'拓扑管理',1,'5',NULL,NULL,0,'2021-03-31 02:55:11','2021-03-31 03:27:03','admin','admin',0),(30,1,14,0,NULL,'SNMP告警码配置',2,'6','snmpCode',NULL,0,'2021-03-03 07:38:14','2021-03-03 07:38:14','admin','admin',0),(31,1,14,0,NULL,'前转配置',2,'7','msgConfig',NULL,0,'2021-03-03 07:38:14','2021-03-03 07:38:14','admin','admin',0);
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `msg_config`
--

DROP TABLE IF EXISTS `msg_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `msg_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
  `type` tinyint DEFAULT NULL COMMENT '类型（1-短信，2-邮件，3-告警箱）',
  `phone` char(11) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '邮箱',
  `box` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '告警箱',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态(0-启用;1-禁用)',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='消息推送配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `msg_config`
--

LOCK TABLES `msg_config` WRITE;
/*!40000 ALTER TABLE `msg_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `msg_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `msg_push_history`
--

DROP TABLE IF EXISTS `msg_push_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `msg_push_history` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `type` tinyint DEFAULT NULL COMMENT '类型（1-短信，2-邮件，3-告警箱）',
  `phone` char(11) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '邮箱',
  `box` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '告警箱',
  `content` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '消息推送内容',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='消息前传推送历史';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `msg_push_history`
--

LOCK TABLES `msg_push_history` WRITE;
/*!40000 ALTER TABLE `msg_push_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `msg_push_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operation_log`
--

DROP TABLE IF EXISTS `operation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `operation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '操作员名称',
  `operation_time` timestamp NULL DEFAULT NULL COMMENT '操作时间',
  `operation_type` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '操作类型',
  `use_time` int DEFAULT NULL COMMENT '用时',
  `params` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '参数',
  `host_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '主机IP',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operation_log`
--

LOCK TABLES `operation_log` WRITE;
/*!40000 ALTER TABLE `operation_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `operation_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `position`
--

DROP TABLE IF EXISTS `position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `position` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `p_id` bigint NOT NULL DEFAULT '0' COMMENT '父节点id',
  `position_code` int DEFAULT NULL COMMENT '位置编码',
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '节点名称',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '位置图标',
  `topographic` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '地形图',
  `type` tinyint(1) NOT NULL COMMENT '节点类型，1-地图，2-线路，3-车站',
  `coordinate` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '坐标',
  `sort` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=339 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='位置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `position`
--

LOCK TABLES `position` WRITE;
/*!40000 ALTER TABLE `position` DISABLE KEYS */;
INSERT INTO `position` VALUES (1,300,1,'S1',NULL,NULL,2,'1，1',NULL,'2021-03-25 07:34:32','2021-04-07 07:48:43',NULL,'1',1),(2,300,2,'S2','group1/M00/00/00/wKgeZ2BIaFOAJkBCAABOsJFfMHM422.png','group1/M00/00/00/wKgeZ2BIaFaAc6FcABwsPhhIAlg552.png',2,'2，3',NULL,'2021-03-10 06:34:00','2021-03-31 01:19:04','1','1',0),(3,1,1,'桐岭',NULL,NULL,3,'122，367',NULL,'2021-03-25 07:37:36','2021-04-07 07:48:43',NULL,'1',1),(4,1,2,'潘桥',NULL,NULL,3,'122，339',NULL,'2021-03-25 07:37:36','2021-04-07 07:48:43',NULL,'1',1),(5,2,1,'下塘',NULL,NULL,3,'712，43',1,'2021-03-25 07:37:36','2021-04-13 05:50:19',NULL,'zzz',0),(6,2,2,'乐成',NULL,NULL,3,'712，71',2,'2021-03-25 07:37:36','2021-04-27 07:01:48',NULL,'admin',0),(100,1,3,'动车南','1','1',3,'122，311',NULL,'2021-03-10 06:39:07','2021-04-07 07:48:43','1','1',1),(300,0,1,'温州','','group1/M00/00/00/wKgeZ2B3rZ-AGzVSAAF8fCMVtyg488.png',1,'1，1',1,'2021-03-31 08:01:46','2021-04-15 03:06:15',NULL,'admin',0),(301,1,3,'秀屿','','',3,'161，285',NULL,'2021-03-31 01:21:06','2021-04-07 07:48:43','1',NULL,1),(302,1,5,'新桥','','',3,'199，285',NULL,'2021-03-31 01:21:38','2021-04-07 07:48:43','1',NULL,1),(303,1,6,'德政','','',3,'234，285',NULL,'2021-03-31 01:22:08','2021-04-07 07:48:43','1',NULL,1),(304,1,7,'龙霞路','','',3,'271，285',NULL,'2021-03-31 01:23:09','2021-04-07 07:48:43','1',NULL,1),(305,1,8,'温州火车站','','',3,'309，285',NULL,'2021-03-31 01:23:36','2021-04-07 07:48:43','1',NULL,1),(306,1,9,'惠民路','','',3,'344，285',NULL,'2021-03-31 01:25:00','2021-04-07 07:48:43','1',NULL,1),(307,1,10,'三洋湿地','','',3,'381，285',NULL,'2021-03-31 01:25:25','2021-04-07 07:48:43','1',NULL,1),(308,1,11,'文昌路','','',3,'417，285',NULL,'2021-03-31 01:29:21','2021-04-07 07:48:43','1',NULL,1),(309,1,12,'龙腾路','','',3,'455，285',NULL,'2021-03-31 01:31:52','2021-04-07 07:48:43','1',NULL,1),(310,1,13,'科技城','','',3,'493，285',NULL,'2021-03-31 01:32:25','2021-04-07 07:48:43','1',NULL,1),(311,1,14,'瑶溪','','',3,'524，330',NULL,'2021-03-31 01:32:49','2021-04-07 07:48:43','1',NULL,1),(312,1,15,'奥体中心','','',3,'548，373',NULL,'2021-03-31 01:35:51','2021-04-07 07:48:43','1',NULL,1),(313,1,16,'永中','','',3,'591，373',NULL,'2021-03-31 01:36:38','2021-04-07 07:48:43','1',NULL,1),(314,1,17,'机场','','',3,'648，343',NULL,'2021-03-31 01:37:03','2021-04-07 07:48:43','1',NULL,1),(315,1,18,'灵昆','','',3,'688，302',NULL,'2021-03-31 01:37:35','2021-04-07 07:48:43','1',NULL,1),(316,1,19,'瓯江口','','',3,'736，315',NULL,'2021-03-31 01:38:03','2021-04-07 07:48:43','1',NULL,1),(317,1,20,'瓯华','','',3,'761，342',NULL,'2021-03-31 01:38:27','2021-04-07 07:48:43','1',NULL,1),(318,1,21,'双瓯大道','','',3,'787，367',NULL,'2021-03-31 01:38:48','2021-04-07 07:48:43','1',NULL,1),(319,2,3,'万岱','','',3,'712，102',3,'2021-03-31 01:40:56','2021-03-31 06:28:06','1',NULL,0),(320,2,4,'盐盆','','',3,'712，130',4,'2021-03-31 01:43:34','2021-03-31 06:28:06','1',NULL,0),(321,2,5,'翁垟北','','',3,'712，160',5,'2021-03-31 01:44:03','2021-03-31 06:28:06','1',NULL,0),(322,2,6,'翁垟','','',3,'712，187',6,'2021-03-31 01:45:04','2021-03-31 06:28:06','1','1',0),(323,2,7,'黄华','','',3,'712，215',7,'2021-03-31 01:45:29','2021-03-31 06:28:06','1',NULL,0),(324,2,8,'灵昆','','',3,'694，309',8,'2021-03-31 01:45:55','2021-03-31 06:28:06','1',NULL,0),(325,2,9,'机场','','',3,'655，349',9,'2021-03-31 01:46:14','2021-03-31 06:28:06','1',NULL,0),(326,2,10,'永兴','','',3,'622，380',10,'2021-03-31 01:46:30','2021-03-31 06:28:06','1',NULL,0),(327,2,11,'沙城','','',3,'595，407',11,'2021-03-31 01:46:49','2021-03-31 06:28:06','1',NULL,0),(328,2,12,'天河','','',3,'567，437',12,'2021-03-31 01:47:15','2021-03-31 06:28:06','1','1',0),(329,2,13,'海城','','',3,'538，464',13,'2021-03-31 01:47:37','2021-03-31 06:28:06','1',NULL,0),(330,2,14,'塘下','','',3,'511，491',14,'2021-03-31 01:51:14','2021-03-31 06:28:06','1',NULL,0),(331,2,15,'清泉','','',3,'483，518',15,'2021-03-31 01:51:34','2021-03-31 06:28:06','1',NULL,0),(332,2,16,'汀田','','',3,'455，547',16,'2021-03-31 02:18:53','2021-03-31 06:28:06','1',NULL,0),(333,2,17,'莘滕','','',3,'427，576',17,'2021-03-31 02:19:21','2021-03-31 06:28:06','1',NULL,0),(334,2,18,'上望','','',3,'398，604',18,'2021-03-31 02:19:41','2021-03-31 06:28:06','1',NULL,0),(335,2,19,'世纪大道','','',3,'370，633',19,'2021-03-31 02:20:13','2021-03-31 06:28:06','1','1',0),(336,2,20,'人民路','','',3,'344，659',20,'2021-03-31 02:21:24','2021-03-31 06:28:06','1',NULL,0),(337,2,100,'下塘停车场','','',3,'717，32',100,'2021-03-31 02:21:24','2021-04-08 06:04:17','1','admin',0),(338,2,101,'瑞安车辆段','','',3,'457，569',101,'2021-03-31 02:21:24','2021-04-08 06:04:01','1','admin',0);
/*!40000 ALTER TABLE `position` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '角色名',
  `role_str` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '权限字符',
  `role_sort` int DEFAULT NULL COMMENT '角色排序',
  `status` tinyint DEFAULT '0' COMMENT '状态',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'admin','admin',1,0,NULL,'2021-01-29 09:33:13','2021-04-14 02:15:49','admin','admin',0);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_menu`
--

DROP TABLE IF EXISTS `role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `menu_id` bigint NOT NULL COMMENT '菜单权限id',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_menu`
--

LOCK TABLES `role_menu` WRITE;
/*!40000 ALTER TABLE `role_menu` DISABLE KEYS */;
INSERT INTO `role_menu` VALUES (1,1,1,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(2,1,2,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(3,1,3,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(4,1,4,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(5,1,5,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(6,1,6,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(7,1,7,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(8,1,8,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(9,1,9,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(10,1,10,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(11,1,11,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(12,1,12,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(13,1,13,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(14,1,14,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(15,1,15,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(16,1,16,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(17,1,17,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(18,1,18,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(19,1,19,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(20,1,20,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(21,1,21,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(22,1,22,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(23,1,23,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(24,1,24,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(25,1,25,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(26,1,26,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(27,1,27,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(28,1,28,'2021-03-03 03:31:11','2021-03-03 03:31:11','admin','admin',0),(77,1,29,'2021-03-31 02:57:15','2021-03-31 02:57:15','admin','admin',0),(101,1,30,'2021-04-07 06:36:44','2021-04-07 06:36:44','admin','admin',0),(102,1,31,'2021-04-07 06:36:44','2021-04-07 06:36:44','admin','admin',0);
/*!40000 ALTER TABLE `role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `snmp_alarm_code`
--

DROP TABLE IF EXISTS `snmp_alarm_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `snmp_alarm_code` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `system_id` bigint NOT NULL COMMENT '子系统表id',
  `positionId` bigint NOT NULL COMMENT '位置id',
  `element_type` varchar(35) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '网元类型',
  `snmp_code` varchar(125) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'snmp码',
  `code` int NOT NULL COMMENT '告警码',
  `reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '告警原因',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='SNMP告警码';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `snmp_alarm_code`
--

LOCK TABLES `snmp_alarm_code` WRITE;
/*!40000 ALTER TABLE `snmp_alarm_code` DISABLE KEYS */;
/*!40000 ALTER TABLE `snmp_alarm_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `snmp_slot`
--

DROP TABLE IF EXISTS `snmp_slot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `snmp_slot` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `system_id` bigint NOT NULL COMMENT '子系统id，subsystem表主键',
  `position_id` bigint NOT NULL COMMENT '位置id，position表主键',
  `slot_id` bigint DEFAULT NULL COMMENT '槽位id，slot表主键',
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'snmp槽位名称',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='snmp槽位配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `snmp_slot`
--

LOCK TABLES `snmp_slot` WRITE;
/*!40000 ALTER TABLE `snmp_slot` DISABLE KEYS */;
/*!40000 ALTER TABLE `snmp_slot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subsystem`
--

DROP TABLE IF EXISTS `subsystem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subsystem` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `pid` bigint NOT NULL DEFAULT '0' COMMENT '父节点id',
  `position_id` bigint NOT NULL COMMENT '线路id',
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '子系统名称',
  `sid` int DEFAULT NULL COMMENT 'SID',
  `server_ip` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '服务器ip',
  `server_port` int DEFAULT NULL COMMENT '服务器端口',
  `icon` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '子系统图标',
  `is_online` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态：0-离线，1-在线',
  `online_time` timestamp NULL DEFAULT NULL,
  `sort` int NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='子系统表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subsystem`
--

LOCK TABLES `subsystem` WRITE;
/*!40000 ALTER TABLE `subsystem` DISABLE KEYS */;
INSERT INTO `subsystem` VALUES (1,0,2,'告警',0,'127.0.0.1',56232,'group1/M00/00/00/wKgeZ2BIN7-Adp2KABwsPhhIAlg970.png',1,'2021-04-20 08:43:11',99,'2021-03-05 08:39:49','2021-04-20 08:43:24','1','1',0),(2,1,2,'传输',1,'127.0.0.1',8001,'group1/M00/00/00/wKgeZ2BIN9yAU0coABwsPhhIAlg379.png',1,'2021-04-20 08:38:11',1,'2021-03-05 08:39:55','2021-04-20 08:38:24','1','1',0),(3,1,2,'专用电话',2,'127.0.0.1',8002,'group1/M00/00/00/wKgeZ2Bj6KeAQAZBAADor8MMi2w825.png',1,'2021-04-25 09:44:01',2,'2021-03-31 03:12:41','2021-04-27 07:01:42','1','admin',0),(21,1,2,'无线',3,'127.0.0.1',8003,'group1/M00/00/00/wKgeZ2Bj6KeAQAZBAADor8MMi2w825.png',1,'2021-04-20 08:39:11',3,'2021-03-31 03:12:41','2021-04-20 08:39:43','1',NULL,0),(22,1,2,'CCTV',4,'127.0.0.1',8004,'group1/M00/00/00/wKgeZ2Bj6OSAe5MBAADor8MMi2w519.png',1,'2021-04-20 08:39:11',4,'2021-03-31 03:13:42','2021-04-20 08:39:53','1',NULL,0),(23,1,2,'广播',5,'127.0.0.1',8005,'group1/M00/00/00/wKgeZ2Bj6PeALDteAADor8MMi2w538.png',1,'2021-04-20 08:40:11',5,'2021-03-31 03:14:01','2021-04-20 08:40:24','1',NULL,0),(24,1,2,'时钟',6,'127.0.0.1',8006,'group1/M00/00/00/wKgeZ2Bj6QaAEHJVAADor8MMi2w858.png',1,'2021-04-20 08:40:11',6,'2021-03-31 03:14:16','2021-04-20 08:40:25','1',NULL,0),(25,1,2,'电源',7,'127.0.0.1',8007,'group1/M00/00/00/wKgeZ2Bj6RiAYoucAADor8MMi2w117.png',1,'2021-04-20 08:40:11',7,'2021-03-31 03:14:33','2021-04-20 08:40:29','1',NULL,0),(26,1,2,'乘客',8,'127.0.0.1',8008,'group1/M00/00/00/wKgeZ2B1LISAQE-2AABZBAQAzUY456.gif',1,'2021-04-20 08:41:11',8,'2021-03-31 03:14:51','2021-04-20 08:41:24','1','admin',0),(27,1,2,'公务电话',9,'127.0.0.1',8099,'group1/M00/00/00/wKgeZ2B1LM-ABA1SAAtVVDZqH34711.png',1,'2021-04-20 08:41:11',9,'2021-03-31 03:15:14','2021-04-20 08:41:24','1','zzz',0),(28,1,2,'OA',12,'127.0.0.1',8012,'group1/M00/00/00/wKgeZ2Bj6UGAW43yAADor8MMi2w609.png',1,'2021-04-20 08:43:11',12,'2021-03-31 03:15:14','2021-04-20 08:43:24','1',NULL,0);
/*!40000 ALTER TABLE `subsystem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_parameter`
--

DROP TABLE IF EXISTS `system_parameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_parameter` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parameter` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '参数',
  `parameter_describe` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '参数描述',
  `parameter_value` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '参数值',
  `value_describe` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '参数值描述',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统参数表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_parameter`
--

LOCK TABLES `system_parameter` WRITE;
/*!40000 ALTER TABLE `system_parameter` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_parameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户名',
  `user_real_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户真实姓名',
  `password` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '密码（加密）',
  `phone` char(11) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '手机号',
  `mail` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '邮箱',
  `status` int DEFAULT '0' COMMENT '状态',
  `remark` varchar(80) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='管理平台用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','管理员','xF0EJZJQ/2fzjYIlfwOG5w==','15658660926','wshmang@qq.com',0,NULL,'2021-02-18 01:49:13','2021-04-07 02:02:01','admin','admin',0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '更新人',
  `is_deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,1,1,'2021-02-18 08:47:50','2021-04-07 02:02:01','admin','admin',0);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-05-06  9:19:48
