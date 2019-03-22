# Social Media as a Sensor of Air Quality and Public Response

![](https://img.shields.io/badge/Spark-2.4.0-brightgreen.svg?style=flat-square) ![](https://img.shields.io/badge/scala-2.11-brightgreen.svg?style=flat-square)

## :rocket: 开始

### :package: 依赖安装

使用 Maven 管理项目，请根据 Maven 配置安装依赖。

### :hammer: 配置文件

Working In Progress.

### :beer: 运行

- 先上传到集群
    ```shell
    $ scp -o ProxyCommand='ssh group05@202.45.128.135 -W %h:%p' out/artifacts/AirQualitySensor_jar/AirQualitySensor.jar hduser@gpu5:~/app/temp/
    ```
- 到集群上运行
    ```shell
    $ ssh ...
    $ cd ~/app/temp
    $ spark-submit --class AirQualitySensor.Analyzer ./AirQualitySensor.jar
    ```
