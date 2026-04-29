CREATE DATABASE  IF NOT EXISTS `library_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `library_db`;
-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: library_db
-- ------------------------------------------------------
-- Server version	8.0.44

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
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `id` int NOT NULL AUTO_INCREMENT,
  `book_barcode` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `isbn` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `author` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `publisher` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `publish_year` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `edition` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `category` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `call_number` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `price` int DEFAULT NULL,
  `state` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `book_barcode` (`book_barcode`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (1,'B00000001','9783890838632','把時間留給重要的人：管理的9個練習','鍾佩瑜; 譯者 何怡君','商周出版','2021','修訂版','100','100.04/001',280,2),(2,'B00000002','9786155940781','重新開始也不晚：管理的自我修復','杜冠廷','木馬文化','2014','2版','700','700.97/002',420,0),(3,'B00000003','9783164752558','一本讀懂人際溝通：核心概念與實作指南','邱冠霖; 譯者 張心瑜','遠流','2022','2版','100','100.98/003',380,3),(4,'B00000004','9780305641399','人際溝通筆記：給忙碌人的整理術','黃筱晴','木馬文化','2022','增訂版','300','300.28/004',420,2),(5,'B00000005','9783287101226','你可以更勇敢：閱讀策略思維的12堂課','許哲維; 譯者 何怡君','上奇','2016','增訂版','500','500.54/005',600,0),(6,'B00000006','9785146270487','重新開始也不晚：人際溝通的自我修復','曾柏鈞; 譯者 何怡君','天下文化','2022','1版','600','600.87/006',520,1),(7,'B00000007','9780154303912','情緒管理地圖：穿越迷惘的路標','李承翰','圓神','2020','增訂版','200','200.00/007',600,0),(8,'B00000008','9783834657879','人際溝通的溫柔力量：寫給正在努力的你','沈宜庭','上奇','2014','3版','100','100.67/008',600,0),(9,'B00000009','9780518347385','你可以更自在：簡報思維的7堂課','邱冠霖','日月文化','2015','1版','100','100.09/009',280,0),(10,'B00000010','9781065133384','重新開始也不晚：閱讀策略的自我修復','吳柏翰; 譯者 蔡承哲','三采','2018','修訂版','200','200.59/010',280,1),(11,'B00000011','9788013267733','在校園遇見創意思考：一段關於選擇的故事','蔡佩珊','商周出版','2019','增訂版','700','700.12/011',280,1),(12,'B00000012','9787234309802','把時間留給重要的人：程式設計的12個練習','許哲維','旗標','2016','修訂版','600','600.89/012',520,1),(13,'B00000013','9783619399093','把時間留給重要的人：專注力的15個練習','林怡君','商周出版','2025','2版','500','500.08/013',600,0),(14,'B00000014','9781079911831','一本讀懂學習方法：核心概念與實作指南','蔡佩珊; 譯者 陳柏廷','晨星','2022','3版','100','100.58/014',420,3),(15,'B00000015','9788412411829','資料分析筆記：給忙碌人的整理術','何孟儒; 譯者 鄭雅婷','上奇','2023','3版','800','800.78/015',520,0),(16,'B00000016','9786400524278','寫作筆記：給忙碌人的整理術','沈宜庭','木馬文化','2020','修訂版','400','400.32/016',280,0),(17,'B00000017','9785053315868','一本讀懂理財：核心概念與實作指南','彭怡安; 譯者 黃詩涵','日月文化','2020','2版','800','800.55/017',320,0),(18,'B00000018','9781607337546','在港都遇見簡報：一段關於選擇的故事','洪郁婷; 譯者 黃詩涵','悅知','2024','2版','900','900.34/018',320,2),(19,'B00000019','9780142940198','你可以更敏捷：理財思維的21堂課','鄭凱文; 譯者 張心瑜','木馬文化','2017','增訂版','300','300.51/019',520,0),(20,'B00000020','9783561595147','從今天起學會學習方法：工作與生活的平衡法','邱冠霖; 譯者 李文心','青林','2018','1版','600','600.66/020',520,1),(21,'B00000021','9788044369956','創意思考筆記：給忙碌人的整理術','彭怡安; 譯者 蔡承哲','晨星','2018','2版','800','800.78/021',600,0),(22,'B00000022','9783433200377','時間管理的溫柔力量：寫給正在努力的你','鍾佩瑜; 譯者 張心瑜','皇冠','2020','增訂版','700','700.42/022',300,2),(23,'B00000023','9783287083171','時間管理入門：從零開始的12天計畫','廖思妤','時報出版','2022','1版','900','900.96/023',300,1),(24,'B00000024','9784873471433','學習方法的溫柔力量：寫給正在努力的你','彭怡安','圓神','2023','修訂版','200','200.57/024',380,1),(25,'B00000025','9787603669094','從今天起學會學習方法：工作與生活的平衡法','李承翰; 譯者 陳柏廷','青林','2015','1版','400','400.53/025',450,1),(26,'B00000026','9786706562721','理財的溫柔力量：寫給正在努力的你','洪郁婷','親子天下','2020','增訂版','600','600.28/026',480,0),(27,'B00000027','9787556464173','理財地圖：穿越迷惘的路標','葉承哲','旗標','2014','1版','900','900.33/027',450,0),(28,'B00000028','9789232719379','專注力地圖：穿越迷惘的路標','潘子涵','遠流','2024','1版','000','000.31/028',350,2),(29,'B00000029','9783149190580','從今天起學會程式設計：工作與生活的平衡法','楊詩涵; 譯者 李文心','皇冠','2021','修訂版','400','400.50/029',350,0),(30,'B00000030','9786284987763','從今天起學會人際溝通：工作與生活的平衡法','洪郁婷','聯經出版','2018','3版','600','600.81/030',480,1),(31,'B00000031','9785075273542','學習方法筆記：給忙碌人的整理術','許哲維','麥田','2024','修訂版','900','900.72/031',600,1),(32,'B00000032','9787701436345','管理筆記：給忙碌人的整理術','王子豪; 譯者 蔡承哲','碁峰資訊','2020','2版','500','500.88/032',480,0),(33,'B00000033','9781351823371','從今天起學會閱讀策略：工作與生活的平衡法','鍾佩瑜','三采','2023','修訂版','900','900.34/033',380,0),(34,'B00000034','9780842710947','重新開始也不晚：心理韌性的自我修復','蔡佩珊','皇冠','2012','增訂版','400','400.16/034',380,0),(35,'B00000035','9780229413188','時間管理的溫柔力量：寫給正在努力的你','許哲維','碁峰資訊','2013','1版','700','700.51/035',480,0),(36,'B00000036','9781334123283','重新開始也不晚：簡報的自我修復','謝宗翰; 譯者 郭子豪','悅知','2016','增訂版','600','600.79/036',280,0),(37,'B00000037','9789361832420','在海邊遇見理財：一段關於選擇的故事','楊詩涵; 譯者 吳冠廷','青林','2016','修訂版','100','100.09/037',350,3),(38,'B00000038','9781906594015','把時間留給重要的人：程式設計的30個練習','謝宗翰; 譯者 吳冠廷','青林','2018','3版','100','100.64/038',520,0),(39,'B00000039','9786717565513','重新開始也不晚：理財的自我修復','潘子涵','碁峰資訊','2020','修訂版','300','300.35/039',320,3),(40,'B00000040','9781680876031','從今天起學會創意思考：工作與生活的平衡法','沈宜庭','上奇','2012','修訂版','200','200.11/040',420,0),(41,'B00000041','9789324808615','從今天起學會閱讀策略：工作與生活的平衡法','吳柏翰; 譯者 陳柏廷','皇冠','2019','修訂版','800','800.15/041',280,3),(42,'B00000042','9782639821461','時間管理入門：從零開始的7天計畫','杜冠廷; 譯者 吳冠廷','悅知','2025','修訂版','300','300.60/042',350,0),(43,'B00000043','9785588675338','管理地圖：穿越迷惘的路標','鍾佩瑜; 譯者 郭子豪','日月文化','2022','修訂版','500','500.19/043',480,0),(44,'B00000044','9780289517185','一本讀懂簡報：核心概念與實作指南','鄭凱文','青林','2024','修訂版','900','900.49/044',320,0),(45,'B00000045','9785865780915','把時間留給重要的人：資料分析的12個練習','廖思妤','木馬文化','2017','增訂版','700','700.88/045',450,0),(46,'B00000046','9780050455623','簡報筆記：給忙碌人的整理術','張雅婷','青林','2025','1版','300','300.56/046',320,1),(47,'B00000047','9787474074829','創意思考地圖：穿越迷惘的路標','黃筱晴; 譯者 蔡承哲','小天下','2022','2版','300','300.63/047',600,0),(48,'B00000048','9785944064097','健康飲食的溫柔力量：寫給正在努力的你','杜冠廷; 譯者 吳冠廷','大塊文化','2018','修訂版','100','100.13/048',350,0),(49,'B00000049','9782104709522','重新開始也不晚：閱讀策略的自我修復','周語婕','麥田','2022','2版','000','000.79/049',380,1),(50,'B00000050','9787451712362','學習方法筆記：給忙碌人的整理術','吳柏翰; 譯者 何怡君','三采','2020','增訂版','100','100.34/050',320,0),(51,'B00000051','9785137098595','從今天起學會人際溝通：工作與生活的平衡法','許哲維','旗標','2017','3版','800','800.74/051',450,2),(52,'B00000052','9782675869267','時間管理入門：從零開始的21天計畫','張雅婷; 譯者 李文心','皇冠','2019','1版','300','300.12/052',350,2),(53,'B00000053','9785064317134','創意思考的溫柔力量：寫給正在努力的你','吳柏翰','麥田','2025','3版','100','100.12/053',420,0),(54,'B00000054','9783529042287','把時間留給重要的人：專注力的30個練習','李承翰','大塊文化','2013','增訂版','900','900.26/054',600,0),(55,'B00000055','9782681177585','在圖書館遇見情緒管理：一段關於選擇的故事','陳冠宇','麥田','2021','3版','400','400.02/055',320,0),(56,'B00000056','9787661771159','時間管理入門：從零開始的15天計畫','鍾佩瑜','晨星','2020','3版','900','900.58/056',280,0),(57,'B00000057','9781836736578','在校園遇見人際溝通：一段關於選擇的故事','郭姿妤','博碩','2016','修訂版','900','900.64/057',600,0),(58,'B00000058','9781528098854','一本讀懂情緒管理：核心概念與實作指南','沈宜庭; 譯者 陳柏廷','晨星','2019','1版','600','600.11/058',300,0),(59,'B00000059','9787315851497','一本讀懂健康飲食：核心概念與實作指南','楊詩涵','三采','2013','增訂版','100','100.64/059',350,0),(60,'B00000060','9786120183663','一本讀懂理財：核心概念與實作指南','黃筱晴; 譯者 吳冠廷','新經典','2017','1版','300','300.23/060',320,0),(61,'B00000061','9780147679765','從今天起學會程式設計：工作與生活的平衡法','潘子涵','小天下','2013','1版','700','700.19/061',320,1),(62,'B00000062','9786900343249','你可以更高效：情緒管理思維的10堂課','楊詩涵','日月文化','2019','增訂版','400','400.85/062',380,0),(63,'B00000063','9780607159691','從今天起學會情緒管理：工作與生活的平衡法','黃筱晴; 譯者 何怡君','青林','2015','增訂版','400','400.71/063',420,0),(64,'B00000064','9783696816452','一本讀懂心理韌性：核心概念與實作指南','鄭凱文; 譯者 郭子豪','旗標','2025','3版','600','600.11/064',450,2),(65,'B00000065','9782127799791','從今天起學會程式設計：工作與生活的平衡法','潘子涵; 譯者 陳柏廷','麥田','2013','2版','300','300.32/065',350,0),(66,'B00000066','9781477005415','從今天起學會學習方法：工作與生活的平衡法','廖思妤','皇冠','2024','3版','900','900.75/066',280,0),(67,'B00000067','9787151820374','重新開始也不晚：職涯規劃的自我修復','邱冠霖','新經典','2021','修訂版','100','100.64/067',320,3),(68,'B00000068','9781864492514','程式設計地圖：穿越迷惘的路標','郭姿妤','新經典','2022','增訂版','700','700.06/068',420,0),(69,'B00000069','9788168505421','在夜市遇見健康飲食：一段關於選擇的故事','周語婕','商周出版','2016','增訂版','900','900.48/069',420,2),(70,'B00000070','9785929622298','從今天起學會閱讀策略：工作與生活的平衡法','李承翰; 譯者 張心瑜','博碩','2024','增訂版','300','300.94/070',520,0),(71,'B00000071','9787359774684','專注力的溫柔力量：寫給正在努力的你','謝宗翰','親子天下','2024','修訂版','200','200.29/071',520,0),(72,'B00000072','9784124782615','一本讀懂程式設計：核心概念與實作指南','許哲維; 譯者 鄭雅婷','三采','2020','1版','800','800.91/072',520,2),(73,'B00000073','9781522047278','健康飲食的溫柔力量：寫給正在努力的你','郭姿妤','麥田','2018','1版','300','300.47/073',350,0),(74,'B00000074','9784341036973','重新開始也不晚：理財的自我修復','吳柏翰','上奇','2023','增訂版','700','700.67/074',450,1),(75,'B00000075','9781851888887','閱讀策略入門：從零開始的15天計畫','杜冠廷; 譯者 蔡承哲','天下文化','2021','3版','100','100.30/075',600,0),(76,'B00000076','9785205852777','Applied Java: Case Studies and Real-World Projects','Emily Carter','O\'Reilly Media','2016','1st ed.','L','QA76.73.J38 H94 2016',720,2),(77,'B00000077','9788740345056','The APIs Handbook: Patterns, Practices, and Pitfalls','Alex Johnson','Pearson','2023','2nd ed.','B','QA76.76.A65 K49 2023',1200,3),(78,'B00000078','9788416169283','Mastering Project Management (5th Edition)','Ethan Smith','Penguin Random House','2016','2nd ed.','P','HD69.P75 W73 2016',1100,3),(79,'B00000079','9785964016588','Thinking in Project Management: Build Better Systems','Olivia Davis','Simon & Schuster','2020','4th ed.','F','HD69.P75 W66 2020',1100,0),(80,'B00000080','9785571928564','The Clean Code Handbook: Patterns, Practices, and Pitfalls','Alex Johnson & David Kim','Routledge','2016','4th ed.','S','QA76.76.C65 B87 2016',800,1),(81,'B00000081','9789473121726','Cybersecurity for Busy People: A Fast Track Course','Ethan Smith & Noah Williams','Penguin Random House','2025','1st ed.','I','QA76.9.A25 Y43 2025',1350,0),(82,'B00000082','9788313237054','Foundations of Systems Design: Principles and Techniques','Ethan Smith & Noah Williams','Addison-Wesley','2025','3rd ed.','F','QA76.9.S88 W99 2025',800,2),(83,'B00000083','9789125177859','Modern Algorithms: Strategies for the Next Decade','Ava Brown','MIT Press','2015','4th ed.','W','QA76.6 X71 2015',1200,2),(84,'B00000084','9783584143844','Software Architecture Essentials: Tools, Tips, and Workflows','Hiro Tanaka','Pearson','2024','1st ed.','Q','QA76.76.A65 E48 2024',800,0),(85,'B00000085','9789001094393','Software Architecture for Busy People: A Fast Track Course','Noah Williams','Elsevier','2020','2nd ed.','F','QA76.76.A65 V89 2020',1100,2),(86,'B00000086','9786773592553','Modern Data Visualization: Strategies for the Next Decade','Olivia Davis','No Starch Press','2014','4th ed.','J','QA76.9.I52 O19 2014',650,0),(87,'B00000087','9780469632592','Thinking in Testing: Build Better Systems','Noah Williams','Prentice Hall','2014','4th ed.','D','QA76.76.T48 I67 2014',880,0),(88,'B00000088','9783950047974','Thinking in Testing: Build Better Systems','Noah Williams','Addison-Wesley','2018','1st ed.','C','QA76.76.T48 M74 2018',1350,3),(89,'B00000089','9783584168786','Project Management Essentials: Tools, Tips, and Workflows','Michael Chen','Routledge','2023','4th ed.','L','HD69.P75 B61 2023',650,0),(90,'B00000090','9780025787292','Modern Project Management: Strategies for the Next Decade','Priya Nair','Prentice Hall','2019','3rd ed.','Y','HD69.P75 E35 2019',1200,0),(91,'B00000091','9784705516943','Systems Design Essentials: Tools, Tips, and Workflows','Michael Chen','CRC Press','2022','3rd ed.','S','QA76.9.S88 K74 2022',1350,1),(92,'B00000092','9789623091305','Clean Code: A Practical Guide to deploy in 2023','Olivia Davis','Elsevier','2020','2nd ed.','X','QA76.76.C65 B84 2020',1350,0),(93,'B00000093','9788578652784','Thinking in User Experience: Build Better Systems','Ethan Smith','Manning','2023','4th ed.','X','HF5548.32 X17 2023',800,0),(94,'B00000094','9787575844192','Cybersecurity Essentials: Tools, Tips, and Workflows','Sofia Martinez','Packt','2019','3rd ed.','H','QA76.9.A25 J46 2019',880,1),(95,'B00000095','9787338484214','APIs Essentials: Tools, Tips, and Workflows','Isabella Garcia','Addison-Wesley','2011','3rd ed.','W','QA76.76.A65 C54 2011',1350,1),(96,'B00000096','9781208267730','Modern Cloud Computing: Strategies for the Next Decade','Olivia Davis','McGraw-Hill','2021','2nd ed.','B','QA76.585 D62 2021',1100,1),(97,'B00000097','9786724004999','Clean Code for Busy People: A Fast Track Course','Benjamin Lee','Elsevier','2014','1st ed.','F','QA76.76.C65 N32 2014',650,3),(98,'B00000098','9787420549326','Thinking in Databases: Build Better Systems','Benjamin Lee','Manning','2018','3rd ed.','G','QA76.9.D3 Z24 2018',1100,0),(99,'B00000099','9783745499049','Applied Cloud Computing: Case Studies and Real-World Projects','Priya Nair','Cambridge University Press','2013','2nd ed.','P','QA76.585 K41 2013',650,2),(100,'B00000100','9783327279564','Thinking in APIs: Build Better Systems','Liam O\'Connor','Penguin Random House','2021','3rd ed.','W','QA76.76.A65 D91 2021',950,0);
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `borrow_records`
--

DROP TABLE IF EXISTS `borrow_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `borrow_records` (
  `id` int NOT NULL AUTO_INCREMENT,
  `reader_id` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `book_barcode` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `borrow_date` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `due_date` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `return_date` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `admin_note` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `borrow_records`
--

LOCK TABLES `borrow_records` WRITE;
/*!40000 ALTER TABLE `borrow_records` DISABLE KEYS */;
INSERT INTO `borrow_records` VALUES (1,'R00000001','B00000001','2026-02-04','2026-02-18','2026-02-25','讀者申請歸還:讀者申請歸還'),(2,'R00000002','B00000002','2026-02-03','2026-02-17',NULL,''),(3,'R00000003','B00000003','2026-02-02','2026-02-16',NULL,''),(4,'R00000004','B00000004','2026-02-01','2026-02-15',NULL,''),(5,'R00000005','B00000005','2026-01-31','2026-02-14',NULL,''),(6,'R00000006','B00000006','2026-01-30','2026-02-13',NULL,''),(7,'R00000007','B00000007','2026-01-29','2026-02-12',NULL,''),(8,'R00000008','B00000008','2026-01-28','2026-02-11',NULL,''),(9,'R00000009','B00000009','2026-01-27','2026-02-10',NULL,''),(10,'R00000010','B00000010','2026-01-26','2026-02-09',NULL,''),(11,'R00000001','B00000011','2026-01-05','2026-01-19','2026-02-18','讀者已送出歸還申請'),(12,'R00000002','B00000012','2026-01-04','2026-01-18','2026-02-20','讀者已送出歸還申請'),(13,'R00000003','B00000013','2026-01-03','2026-01-17','2026-02-19','讀者已送出歸還申請'),(14,'R00000004','B00000014','2026-01-02','2026-01-16','2026-02-18','讀者已送出歸還申請'),(15,'R00000005','B00000015','2026-01-01','2026-01-15','2026-02-20','讀者已送出歸還申請'),(16,'R00000006','B00000016','2025-10-12','2025-10-26','2025-10-22','已確認歸還:完好'),(17,'R00000007','B00000017','2025-10-11','2025-10-25','2025-10-21','已確認歸還:完好'),(18,'R00000008','B00000018','2025-10-10','2025-10-24','2025-10-20','已確認歸還:完好'),(19,'R00000009','B00000019','2025-10-09','2025-10-23','2025-10-19','已確認歸還:完好'),(20,'R00000010','B00000020','2025-10-08','2025-10-22','2025-10-18','已確認歸還:完好'),(21,'R00000001','B00000021','2025-10-07','2025-10-21','2025-10-17','已確認歸還:完好'),(22,'R00000002','B00000022','2025-10-06','2025-10-20','2025-10-16','已確認歸還:完好'),(23,'R00000003','B00000023','2025-10-05','2025-10-19','2025-10-15','已確認歸還:完好'),(24,'R00000004','B00000024','2025-10-04','2025-10-18','2025-10-14','已確認歸還:完好'),(25,'R00000005','B00000025','2025-10-03','2025-10-17','2025-10-13','已確認歸還:完好'),(26,'R00000001','B00000099','2026-02-25','2026-03-27','2026-02-25','讀者申請歸還:讀者申請歸還'),(27,'R00000001','B00000074','2026-02-25','2026-03-27',NULL,''),(28,'R00000001','B00000025','2026-02-25','2026-03-27',NULL,'');
/*!40000 ALTER TABLE `borrow_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `messages` (
  `id` int NOT NULL AUTO_INCREMENT,
  `reader_id` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `message_type` int DEFAULT NULL,
  `content` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_read` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` VALUES (1,'R00000001',1,'系統通知1：您的預約/借閱狀態已更新。','2026-02-24',1),(2,'R00000001',1,'系統通知2：您的預約/借閱狀態已更新。','2026-02-23',0),(3,'R00000001',1,'系統通知3：您的預約/借閱狀態已更新。','2026-02-22',0),(4,'R00000001',1,'系統通知4：您的預約/借閱狀態已更新。','2026-02-21',1),(5,'R00000001',1,'系統通知5：您的預約/借閱狀態已更新。','2026-02-20',1),(6,'R00000001',1,'系統通知6：您的預約/借閱狀態已更新。','2026-02-19',1),(7,'R00000001',1,'系統通知7：您的預約/借閱狀態已更新。','2026-02-18',1),(8,'R00000001',1,'系統通知8：您的預約/借閱狀態已更新。','2026-02-17',1),(9,'R00000001',1,'系統通知9：您的預約/借閱狀態已更新。','2026-02-16',1),(10,'R00000001',1,'系統通知10：您的預約/借閱狀態已更新。','2026-02-15',1),(11,'R00000002',2,'提醒1：請留意到期日與取書日。','2026-02-14',1),(12,'R00000003',2,'提醒2：請留意到期日與取書日。','2026-02-13',1),(13,'R00000004',2,'提醒3：請留意到期日與取書日。','2026-02-12',1),(14,'R00000005',2,'提醒4：請留意到期日與取書日。','2026-02-11',1),(15,'R00000006',2,'提醒5：請留意到期日與取書日。','2026-02-10',1);
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `readers`
--

DROP TABLE IF EXISTS `readers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `readers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `reader_id` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reader_name` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `national_id` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `birthday` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `online_username` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `online_password` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reward_points` int DEFAULT '0',
  `reader_level` int DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `reader_id` (`reader_id`),
  UNIQUE KEY `online_username` (`online_username`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `readers`
--

LOCK TABLES `readers` WRITE;
/*!40000 ALTER TABLE `readers` DISABLE KEYS */;
INSERT INTO `readers` VALUES (1,'R00000001','陳品妤','A123456789','1998-07-12','新北市板橋區文化路一段88號','0912001122','pinyu.chen@example.com','demo','demo1234',38,2),(2,'R00000002','林冠宇','B234567890','1996-11-03','台北市中山區南京東路三段120號','0922333444','guanyu.lin@example.com','guanyu96','pass1234',15,1),(3,'R00000003','王雅婷','C345678901','2000-02-20','台中市西屯區臺灣大道四段500號','0933555666','yating.wang@example.com','yating_w','pw0000',60,3),(4,'R00000004','張柏翰','D456789012','1995-05-28','高雄市苓雅區中正一路25號','0955007788','bohan.zhang@example.com','bohan95','1234',5,1),(5,'R00000005','許子瑄','E567890123','2001-09-14','台南市東區長榮路二段10號','0977111222','zixuan.xu@example.com','zixuan01','zixuan123',25,2),(6,'R00000006','吳承翰','F678901234','1999-12-01','桃園市中壢區中大路300號','0988222333','chenghan.wu@example.com','chwu_99','abcd1234',0,1),(7,'R00000007','蔡宜蓁','G789012345','1997-03-09','新竹市東區光復路二段101號','0900111222','yizhen.tsai@example.com','yizhen97','1111',10,1),(8,'R00000008','郭孟軒','H890123456','1994-08-22','基隆市仁愛區仁二路5號','0911222333','mengxuan.kuo@example.com','mxkuo94','2222',45,2),(9,'R00000009','周思妤','J901234567','2002-01-30','彰化縣彰化市中山路二段100號','0922444555','siyu.chou@example.com','siyu2002','3333',18,1),(10,'R00000010','劉家豪','K012345678','1993-10-17','花蓮縣花蓮市中華路200號','0933666777','jiahao.liu@example.com','jiahao93','4444',8,1);
/*!40000 ALTER TABLE `readers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservations`
--

DROP TABLE IF EXISTS `reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservations` (
  `id` int NOT NULL AUTO_INCREMENT,
  `reader_id` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `book_barcode` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reserved_at` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `expected_pickup_date` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `state` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservations`
--

LOCK TABLES `reservations` WRITE;
/*!40000 ALTER TABLE `reservations` DISABLE KEYS */;
INSERT INTO `reservations` VALUES (1,'R00000004','B00000001','2026-02-22','2026-03-01',0),(2,'R00000005','B00000002','2026-02-21','2026-02-28',0),(3,'R00000006','B00000003','2026-02-20','2026-02-27',0),(4,'R00000007','B00000004','2026-02-19','2026-02-26',0),(5,'R00000008','B00000005','2026-02-18','2026-02-25',0),(6,'R00000009','B00000016','2026-02-17','2026-02-24',0),(7,'R00000010','B00000017','2026-02-16','2026-02-23',0),(8,'R00000001','B00000069','2026-02-25','2026-03-01',0);
/*!40000 ALTER TABLE `reservations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review_drafts`
--

DROP TABLE IF EXISTS `review_drafts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review_drafts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `reader_id` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `slot` int NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `isbn` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pen_name` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `book_intro` text COLLATE utf8mb4_unicode_ci,
  `book_excerpt` text COLLATE utf8mb4_unicode_ci,
  `content` text COLLATE utf8mb4_unicode_ci,
  `word_count` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_reader_slot` (`reader_id`,`slot`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review_drafts`
--

LOCK TABLES `review_drafts` WRITE;
/*!40000 ALTER TABLE `review_drafts` DISABLE KEYS */;
INSERT INTO `review_drafts` VALUES (1,'R00000001',0,NULL,NULL,NULL,NULL,NULL,NULL,0),(2,'R00000001',1,NULL,NULL,NULL,NULL,NULL,NULL,0),(3,'R00000001',2,NULL,NULL,NULL,NULL,NULL,NULL,0),(4,'R00000002',0,NULL,NULL,NULL,NULL,NULL,NULL,0),(5,'R00000002',1,NULL,NULL,NULL,NULL,NULL,NULL,0),(6,'R00000002',2,NULL,NULL,NULL,NULL,NULL,NULL,0),(7,'R00000003',0,NULL,NULL,NULL,NULL,NULL,NULL,0),(8,'R00000003',1,NULL,NULL,NULL,NULL,NULL,NULL,0),(9,'R00000003',2,NULL,NULL,NULL,NULL,NULL,NULL,0),(10,'R00000004',0,NULL,NULL,NULL,NULL,NULL,NULL,0),(11,'R00000004',1,NULL,NULL,NULL,NULL,NULL,NULL,0),(12,'R00000004',2,NULL,NULL,NULL,NULL,NULL,NULL,0),(13,'R00000005',0,NULL,NULL,NULL,NULL,NULL,NULL,0),(14,'R00000005',1,NULL,NULL,NULL,NULL,NULL,NULL,0),(15,'R00000005',2,NULL,NULL,NULL,NULL,NULL,NULL,0),(16,'R00000006',0,NULL,NULL,NULL,NULL,NULL,NULL,0),(17,'R00000006',1,NULL,NULL,NULL,NULL,NULL,NULL,0),(18,'R00000006',2,NULL,NULL,NULL,NULL,NULL,NULL,0),(19,'R00000007',0,NULL,NULL,NULL,NULL,NULL,NULL,0),(20,'R00000007',1,NULL,NULL,NULL,NULL,NULL,NULL,0),(21,'R00000007',2,NULL,NULL,NULL,NULL,NULL,NULL,0),(22,'R00000008',0,NULL,NULL,NULL,NULL,NULL,NULL,0),(23,'R00000008',1,NULL,NULL,NULL,NULL,NULL,NULL,0),(24,'R00000008',2,NULL,NULL,NULL,NULL,NULL,NULL,0),(25,'R00000009',0,NULL,NULL,NULL,NULL,NULL,NULL,0),(26,'R00000009',1,NULL,NULL,NULL,NULL,NULL,NULL,0),(27,'R00000009',2,NULL,NULL,NULL,NULL,NULL,NULL,0),(28,'R00000010',0,NULL,NULL,NULL,NULL,NULL,NULL,0),(29,'R00000010',1,NULL,NULL,NULL,NULL,NULL,NULL,0),(30,'R00000010',2,NULL,NULL,NULL,NULL,NULL,NULL,0);
/*!40000 ALTER TABLE `review_drafts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `review_view`
--

DROP TABLE IF EXISTS `review_view`;
/*!50001 DROP VIEW IF EXISTS `review_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `review_view` AS SELECT 
 1 AS `review_id`,
 1 AS `reader_id`,
 1 AS `reader_name`,
 1 AS `book_barcode`,
 1 AS `isbn`,
 1 AS `title`,
 1 AS `author`,
 1 AS `publisher`,
 1 AS `publish_year`,
 1 AS `category`,
 1 AS `call_number`,
 1 AS `content`,
 1 AS `word_count`,
 1 AS `review_state`,
 1 AS `is_public`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `id` int NOT NULL AUTO_INCREMENT,
  `reader_id` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `book_barcode` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `content` text COLLATE utf8mb4_unicode_ci,
  `word_count` int DEFAULT NULL,
  `state` int DEFAULT '0',
  `is_public` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
INSERT INTO `reviews` VALUES (1,'R00000001','B00000001','《中文書籍範例 1》讀後感：我最喜歡作者在章節間穿插的小例子，讓概念變得很具體。閱讀時我一邊做筆記，一邊把書中的方法套用到自己的學習與工作上，特別是「先拆解再行動」的流程，讓我更容易開始。若你正在尋找一本文字清楚、節奏明快、又能帶來實際啟發的書，這本會是很好的入門選擇。',134,0,0),(2,'R00000002','B00000002','《中文書籍範例 2》讀後感：我最喜歡作者在章節間穿插的小例子，讓概念變得很具體。閱讀時我一邊做筆記，一邊把書中的方法套用到自己的學習與工作上，特別是「先拆解再行動」的流程，讓我更容易開始。若你正在尋找一本文字清楚、節奏明快、又能帶來實際啟發的書，這本會是很好的入門選擇。',134,1,1),(3,'R00000003','B00000003','《中文書籍範例 3》讀後感：我最喜歡作者在章節間穿插的小例子，讓概念變得很具體。閱讀時我一邊做筆記，一邊把書中的方法套用到自己的學習與工作上，特別是「先拆解再行動」的流程，讓我更容易開始。若你正在尋找一本文字清楚、節奏明快、又能帶來實際啟發的書，這本會是很好的入門選擇。',134,0,0),(4,'R00000004','B00000004','《中文書籍範例 4》讀後感：我最喜歡作者在章節間穿插的小例子，讓概念變得很具體。閱讀時我一邊做筆記，一邊把書中的方法套用到自己的學習與工作上，特別是「先拆解再行動」的流程，讓我更容易開始。若你正在尋找一本文字清楚、節奏明快、又能帶來實際啟發的書，這本會是很好的入門選擇。',134,1,1),(5,'R00000005','B00000005','《中文書籍範例 5》讀後感：我最喜歡作者在章節間穿插的小例子，讓概念變得很具體。閱讀時我一邊做筆記，一邊把書中的方法套用到自己的學習與工作上，特別是「先拆解再行動」的流程，讓我更容易開始。若你正在尋找一本文字清楚、節奏明快、又能帶來實際啟發的書，這本會是很好的入門選擇。',134,0,0),(6,'R00000006','B00000006','《中文書籍範例 6》讀後感：我最喜歡作者在章節間穿插的小例子，讓概念變得很具體。閱讀時我一邊做筆記，一邊把書中的方法套用到自己的學習與工作上，特別是「先拆解再行動」的流程，讓我更容易開始。若你正在尋找一本文字清楚、節奏明快、又能帶來實際啟發的書，這本會是很好的入門選擇。',134,0,0),(7,'R00000007','B00000007','《中文書籍範例 7》讀後感：我最喜歡作者在章節間穿插的小例子，讓概念變得很具體。閱讀時我一邊做筆記，一邊把書中的方法套用到自己的學習與工作上，特別是「先拆解再行動」的流程，讓我更容易開始。若你正在尋找一本文字清楚、節奏明快、又能帶來實際啟發的書，這本會是很好的入門選擇。',134,1,1),(8,'R00000008','B00000008','《中文書籍範例 8》讀後感：我最喜歡作者在章節間穿插的小例子，讓概念變得很具體。閱讀時我一邊做筆記，一邊把書中的方法套用到自己的學習與工作上，特別是「先拆解再行動」的流程，讓我更容易開始。若你正在尋找一本文字清楚、節奏明快、又能帶來實際啟發的書，這本會是很好的入門選擇。',134,1,1),(9,'R00000009','B00000009','《中文書籍範例 9》讀後感：我最喜歡作者在章節間穿插的小例子，讓概念變得很具體。閱讀時我一邊做筆記，一邊把書中的方法套用到自己的學習與工作上，特別是「先拆解再行動」的流程，讓我更容易開始。若你正在尋找一本文字清楚、節奏明快、又能帶來實際啟發的書，這本會是很好的入門選擇。',134,1,1),(10,'R00000010','B00000010','《中文書籍範例 10》讀後感：我最喜歡作者在章節間穿插的小例子，讓概念變得很具體。閱讀時我一邊做筆記，一邊把書中的方法套用到自己的學習與工作上，特別是「先拆解再行動」的流程，讓我更容易開始。若你正在尋找一本文字清楚、節奏明快、又能帶來實際啟發的書，這本會是很好的入門選擇。',135,1,1),(11,'R00000001','B00000011','《中文書籍範例 11》讀後感：我最喜歡作者在章節間穿插的小例子，讓概念變得很具體。閱讀時我一邊做筆記，一邊把書中的方法套用到自己的學習與工作上，特別是「先拆解再行動」的流程，讓我更容易開始。若你正在尋找一本文字清楚、節奏明快、又能帶來實際啟發的書，這本會是很好的入門選擇。',135,1,1),(12,'R00000002','B00000012','《中文書籍範例 12》讀後感：我最喜歡作者在章節間穿插的小例子，讓概念變得很具體。閱讀時我一邊做筆記，一邊把書中的方法套用到自己的學習與工作上，特別是「先拆解再行動」的流程，讓我更容易開始。若你正在尋找一本文字清楚、節奏明快、又能帶來實際啟發的書，這本會是很好的入門選擇。',135,1,1),(13,'R00000003','B00000013','《中文書籍範例 13》讀後感：我最喜歡作者在章節間穿插的小例子，讓概念變得很具體。閱讀時我一邊做筆記，一邊把書中的方法套用到自己的學習與工作上，特別是「先拆解再行動」的流程，讓我更容易開始。若你正在尋找一本文字清楚、節奏明快、又能帶來實際啟發的書，這本會是很好的入門選擇。',135,1,1),(14,'R00000004','B00000014','《中文書籍範例 14》讀後感：我最喜歡作者在章節間穿插的小例子，讓概念變得很具體。閱讀時我一邊做筆記，一邊把書中的方法套用到自己的學習與工作上，特別是「先拆解再行動」的流程，讓我更容易開始。若你正在尋找一本文字清楚、節奏明快、又能帶來實際啟發的書，這本會是很好的入門選擇。',135,1,1),(15,'R00000005','B00000015','《中文書籍範例 15》讀後感：我最喜歡作者在章節間穿插的小例子，讓概念變得很具體。閱讀時我一邊做筆記，一邊把書中的方法套用到自己的學習與工作上，特別是「先拆解再行動」的流程，讓我更容易開始。若你正在尋找一本文字清楚、節奏明快、又能帶來實際啟發的書，這本會是很好的入門選擇。',135,1,1);
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `review_view`
--

/*!50001 DROP VIEW IF EXISTS `review_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `review_view` AS select `r`.`id` AS `review_id`,`r`.`reader_id` AS `reader_id`,`rd`.`reader_name` AS `reader_name`,`b`.`book_barcode` AS `book_barcode`,`b`.`isbn` AS `isbn`,`b`.`title` AS `title`,`b`.`author` AS `author`,`b`.`publisher` AS `publisher`,`b`.`publish_year` AS `publish_year`,`b`.`category` AS `category`,`b`.`call_number` AS `call_number`,`r`.`content` AS `content`,`r`.`word_count` AS `word_count`,`r`.`state` AS `review_state`,`r`.`is_public` AS `is_public` from ((`reviews` `r` join `readers` `rd` on((`r`.`reader_id` = `rd`.`reader_id`))) join `books` `b` on((`r`.`book_barcode` = `b`.`book_barcode`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-25  7:41:26
