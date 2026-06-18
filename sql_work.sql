-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: sql_work
-- ------------------------------------------------------
-- Server version	8.0.45

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
-- Table structure for table `game_tags`
--

DROP TABLE IF EXISTS `game_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game_tags` (
  `game_id` int NOT NULL,
  `tag_id` int NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`game_id`,`tag_id`),
  KEY `FK_tag_id` (`tag_id`),
  CONSTRAINT `FK_game_id` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON DELETE CASCADE,
  CONSTRAINT `FK_tag_id` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`tag_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game_tags`
--

LOCK TABLES `game_tags` WRITE;
/*!40000 ALTER TABLE `game_tags` DISABLE KEYS */;
INSERT INTO `game_tags` VALUES (1,1,NULL),(1,2,NULL),(1,3,NULL),(1,4,NULL),(1,5,NULL),(1,6,NULL),(2,7,NULL),(2,8,NULL),(2,9,NULL),(2,10,NULL),(2,11,NULL),(2,12,NULL),(3,13,NULL),(3,14,NULL),(3,15,NULL),(3,16,NULL),(3,17,NULL),(3,18,NULL),(3,19,NULL),(4,20,NULL),(4,21,NULL),(4,22,NULL),(4,23,NULL),(4,24,NULL),(4,25,NULL),(5,1,NULL),(5,24,NULL),(5,26,NULL),(5,27,NULL),(5,28,NULL),(5,29,NULL),(6,21,NULL),(6,24,NULL),(6,27,NULL),(6,30,NULL),(6,31,NULL),(6,32,NULL),(6,33,NULL),(7,11,NULL),(7,34,NULL),(7,35,NULL),(7,36,NULL),(7,37,NULL),(7,38,NULL),(7,39,NULL),(8,11,NULL),(8,13,NULL),(8,34,NULL),(8,36,NULL),(8,37,NULL),(8,38,NULL),(8,40,NULL),(9,11,NULL),(9,13,NULL),(9,34,NULL),(9,36,NULL),(9,37,NULL),(9,40,NULL),(9,41,NULL),(10,17,NULL),(10,42,NULL),(10,43,NULL),(10,44,NULL),(10,45,NULL);
/*!40000 ALTER TABLE `game_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `games`
--

DROP TABLE IF EXISTS `games`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `games` (
  `game_id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `developer` varchar(100) NOT NULL,
  `price` int NOT NULL DEFAULT '0',
  `release_date` date NOT NULL,
  PRIMARY KEY (`game_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `games`
--

LOCK TABLES `games` WRITE;
/*!40000 ALTER TABLE `games` DISABLE KEYS */;
INSERT INTO `games` VALUES (1,'逃離鴨科夫','Team Soda',328,'2025-10-16'),(2,'深海迷航 : 氷點之下','Unknown Worlds Entertainment',899,'2021-05-14'),(3,'超級雞馬','Clever Endeavour Games',299,'2016-03-15'),(4,'暈暈電波症候群','Alliance Arts',330,'2026-04-24'),(5,'憶我: 一個性格測試遊戲','Lizardry',152,'2023-11-14'),(6,'魔法少女的魔女審判','Acacia',398,'2025-07-18'),(7,'Euro Truck Simulator 2','SCS Software',428,'2012-10-18'),(8,'Forza Horizon 5','Playground Games',1688,'2021-11-09'),(9,'Forza Horizon 6','Playground Games',1990,'2026-05-19'),(10,'Hollow Knight ','Team Cherry',368,'2017-02-25');
/*!40000 ALTER TABLE `games` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `review_id` int NOT NULL AUTO_INCREMENT,
  `game_id` int NOT NULL,
  `player_name` varchar(50) NOT NULL,
  `rating` int DEFAULT NULL,
  `comment` text,
  PRIMARY KEY (`review_id`),
  KEY `FK_reviews_game_id` (`game_id`),
  CONSTRAINT `FK_reviews_game_id` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON DELETE CASCADE,
  CONSTRAINT `reviews_chk_1` CHECK ((`rating` between 1 and 5))
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
INSERT INTO `reviews` VALUES (1,1,'HardcoreDuck',5,'這是我玩過最硬核的鴨子射擊遊戲，撤離失敗直接破防，但太好玩了！'),(2,1,'Gamer_99',4,'畫面很有氛圍，就是對新手不太友善，每次進去都被老鳥當鴨子打。'),(3,1,'SaltedEgg',2,'外掛有點多，而且死掉裝備全噴真的太搞心態了，先給兩星。'),(4,2,'SubnauticaFan',5,'極地冰層下的世界美到令人窒息！生態圈設計得非常有深度，神作無誤。'),(5,2,'SeaPhobia',4,'一邊看風景一邊被深海巨獸嚇尿，不自覺就玩了五十個小時。'),(6,2,'IceIceBaby',5,'比第一代多了更多陸地探索，劇情線也更清晰，生存建造迷必入！'),(7,2,'DiveMaster',4,'雖然地圖比一代小了一點，但精緻度完全不減，音樂超讚。'),(8,3,'FriendshipKiller',5,'昨晚跟朋友玩這個差點打起來，陷阱擺得好，朋友走得早，推爆！'),(9,3,'PartyAnimal',5,'聚會必備神器！每局都能笑到肚子痛，看著大家一起卡死在起點超好笑。'),(10,4,'RadioWave',4,'視覺風格極度強烈，電波對上了就會超級沉浸，音樂非常有毒！'),(11,4,'ConfusedPlayer',3,'美術很棒，但劇情和玩法稍微有點抽象，可能比較挑玩家。'),(12,5,'SoulSearcher',5,'這不只是一個遊戲，更像是一面鏡子。測出來的性格準到發毛，強烈推薦！'),(13,5,'StoryLover',5,'安靜、細膩，玩完之後會陷入沉思，非常感謝作者做出這樣的作品。'),(14,6,'MadokaMagica',4,'看似軟萌的魔法少女，背景設定卻超級黑，這種反差感我很喜歡！'),(15,6,'JusticeHammer',3,'題材很有新意，不過審判部分的玩法可以再優化一下，略顯重複。'),(16,7,'Trucker_AhDong',5,'點播一首浪子回頭，開著我的斯堪尼亞載著貨物奔馳在歐洲高速公路上，這就是男人的浪漫。'),(17,7,'RadioListener',5,'下班後最棒的放鬆遊戲。一邊聽著真實的歐洲廣播電台，一邊看著窗外的雨景送貨，不知不覺天就亮了。'),(18,7,'GPS_Hater',4,'遊戲絕對是滿分，但導航有時候會把我騙進狹窄的小巷子，讓我的聯結車卡死在裡面，氣到給四星。'),(19,7,'Tycoon_CEO',5,'從一開始借貸買車的窮司機，到現在擁有全歐洲最大卡車帝國、雇用了幾十個員工的 CEO，成就感滿滿！'),(20,8,'MexicoRacer',5,'墨西哥的風景太美了！從火山開到海灘的體驗超級震撼，車輛收藏多到根本開不完。'),(21,8,'CarPhotographer',4,'畫面放到今天依然是業界頂級，拍照模式隨便拍都是桌布，當作旅遊模擬器玩也很值。'),(22,8,'DriftKing99',5,'開放世界的賽車天花板，操控性在擬真與娛樂之間拿捏得剛剛好，神作！'),(23,9,'HorizonHype2026',5,'2026最強賽車神作降臨！這次的新地圖大到不可思議，光線追蹤和動態天氣特效直接把顯卡榨乾！'),(24,9,'SpeedDemon',5,'首發直接衝了！開場的空降儀式還是一樣讓人熱血沸騰，音效和引擎聲比 5 代又更上一層樓。'),(25,9,'WheelUser',4,'方向盤的力回饋優化得非常好，路感超清晰。缺點是首發稍微有點小 Bug，但瑕不掩瑜。'),(26,9,'CasualDriver',5,'跟朋友在線上組車隊開圖太爽了，物理引擎感覺更細膩了，賽車迷這代必入！'),(27,10,'BugKnight',5,'毫無疑問的滿分神作！地圖設計、音樂、打擊感都是天花板級別。'),(28,10,'RadianceSlayer',5,'苦痛之路雖然讓我走到快中風，但通關的那一刻感動到哭出來。'),(29,10,'NoobPlayer',2,'這遊戲太難了吧... 第一個 Boss 都打不過，一直迷路死掉掉錢，被勸退了。');
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tags` (
  `tag_id` int NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(100) NOT NULL,
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `tag_name` (`tag_name`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tags`
--

LOCK TABLES `tags` WRITE;
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
INSERT INTO `tags` VALUES (29,'2D'),(26,'互動小說'),(1,'冒險'),(5,'刷寶射擊'),(28,'劇情豐富'),(2,'動作'),(30,'可愛'),(10,'單人'),(16,'單機合作'),(44,'困難'),(13,'多人'),(25,'多結局'),(27,'女主人翁'),(45,'好評原聲帶'),(6,'射擊'),(17,'平台'),(4,'府視角射擊'),(21,'心理恐怖'),(12,'恐怖'),(32,'懸疑'),(41,'探索'),(31,'推理'),(15,'搞笑'),(3,'撤離射擊'),(38,'擬真'),(39,'放鬆'),(22,'日本動畫'),(36,'模擬'),(9,'水底'),(37,'汽機車模擬'),(14,'派對'),(18,'派對遊戲'),(19,'玩家對戰'),(8,'生存'),(40,'競速'),(20,'節奏'),(33,'血腥'),(24,'視覺小說'),(35,'運輸'),(11,'開放世界'),(7,'開放世界生存工藝'),(23,'音樂'),(42,'類銀河戰士惡魔城'),(43,'類魂'),(34,'駕駛');
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-12 14:47:43
