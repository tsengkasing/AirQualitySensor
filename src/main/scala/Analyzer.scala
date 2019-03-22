package AirQualitySensor

import scala.io.Source
import org.apache.log4j.Logger
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010.{KafkaUtils, LocationStrategies, ConsumerStrategies}

object Analyzer extends Serializable {
  @transient val logger: Logger = Logger.getLogger(Analyzer.getClass)
  // 关键词
  val keywords: Seq[String] = Seq("天气", "污染", "空气", "呼吸", "肺部", "烟雾", "雾霾", "咳嗽")

  /**
    * 从 kafka 读取
    */
  def loadFromKafka() : Unit = {
    val conf = new SparkConf().setAppName("AirQualitySensor")
    val ssc = new StreamingContext(conf, Seconds(10)) // streaming 的时间间隔, 当前为 1 秒

    val kafkaParams = Map[String, Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "10.42.2.25:9092", // broker
      ConsumerConfig.GROUP_ID_CONFIG -> "0",
      ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "latest",
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer]
    )
    val topics = Set("test03")

    val kafkaStream = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topics, kafkaParams)
    )

    def hasKeywords(text: String): Boolean = {
      logger.info(text.slice(0, 10)) // 打印前10个字符
      for (keyword <- keywords
           if text.contains(keyword)) {
        return true
      }
      false
    }

    val lines = kafkaStream.map(record => record.value).cache()
    // 总微博条数
    val totalNumLines = lines.count()
    totalNumLines.print()

    val num = lines
      .filter(hasKeywords)
      .count()
    num.print()

    print(s"\n ========================= BEGIN =========================\n\n")
    // 开始
    ssc.start()
    ssc.awaitTermination()

    print(s"\n ========================= !!!END!!! =========================\n\n")
  }

  /**
    * 从本地磁盘加载
    * @param filePath 文件路径
    */
  def loadFromDisk(filePath: String) : Unit = {
    val lines = Source.fromFile(filePath).getLines()

    // 总微博条数
    var totalNumLines = 0

    def hasKeywords(text: String): Boolean = {
      totalNumLines += 1
      logger.info(text.slice(0, 10)) // 打印前10个字符
      for (keyword <- keywords
           if text.contains(keyword)) {
        return true
      }
      false
    }
    // 含关键词的微博条数
    val numHasKeywords = lines.count(hasKeywords)
    logger.info(s"========== Total Lines: $totalNumLines, Num of containing keywords: $numHasKeywords =============")
  }
  def main(args: Array[String]): Unit = {
    // 从磁盘文本读取
    // loadFromDisk("/home/hduser/app/spark-text/weibo-15M.txt")

    // 从 kafka 读取
    loadFromKafka()
  }
}
