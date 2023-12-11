CREATE TABLE IF NOT EXISTS `user` (
  `num` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `phone_number` varchar(20) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  `age` int DEFAULT '0',
  `qu_id` int NOT NULL,
  `qn_id` int NOT NULL,
  `ans` varchar(60) DEFAULT NULL,
  `date_time` datetime NOT NULL,
  PRIMARY KEY (`num`),
  UNIQUE KEY `email_UNIQUE` (`email`)
);

