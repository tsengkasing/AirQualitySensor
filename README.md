# Social Media as a Sensor of Air Quality and Public Response

![](https://img.shields.io/badge/Spark-2.4.0-brightgreen.svg?style=flat-square) ![](https://img.shields.io/badge/scala-2.11-brightgreen.svg?style=flat-square)

## :rocket: 开始

### :package: 依赖安装

使用 Maven 管理项目，请根据 Maven 配置安装依赖。

### :hammer: 配置文件

Working In Progress.

### :beer: 运行

- 编译打包成 Jar 包
- 上传到集群
    ```shell
    $ scp -o ProxyCommand='ssh group05@202.45.128.135 -W %h:%p' out/artifacts/AirQualitySensor_jar/AirQualitySensor.jar hduser@gpu5:~/app/temp/
    ```
- 在集群上运行
    ```shell
    $ ssh ...
    $ cd ~/app/temp
    $ spark-submit --class AirQualitySensor.Analyzer ./AirQualitySensor.jar test04
    ```
- 开启 Kafka Producer (Option 1)
    ```shell
    $ kafka-console-producer.sh --broker-list gpu5:9092 --topic test04
    $> 输入测试用的文本
    ```
- 开启 Flume 搬迁数据到 Kafka
    > 配置文件里 hard code 了 topic test04
    ```shell
    $ flume-ng agent -c /home/hduser/app/flume-rate-controller -f rate-control-flume-conf.properties --name agent -Dflume.root.logger=INFO,console
    ```
    PS: 以上的 **test04** 是指 *Kafka* 的 **topic** , 可自定义(需创建)