## WxJava mp + spring security 的spring boot starter 
[![Maven Central](https://img.shields.io/maven-central/v/xyz.seansun/rambutan-spring-boot-starter.svg)](https://gitee.com/lyfuci/Rambutan) 
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/f003a15557a2423cade8fad04a2b6327)](https://www.codacy.com/manual/lyfuci/rambutan?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=lyfuci/rambutan&amp;utm_campaign=Badge_Grade)

该项目主要致力于降低微信服务号开发难度，提高服务号开发的安全性，在spring-boot-starter-web项目中，一个最简单的配置可能如下所示，即完成了公众号的服务接入。

首先，在pom文件中添加如下依赖

```xml
        <dependency>
            <groupId>xyz.seansun</groupId>
            <artifactId>rambutan-spring-boot-starter</artifactId>
            <version>1.0.0</version>
        </dependency>
```

然后，在application.yml中增加如下配置，即完成了一个最基础的公众号服务接入配置

```yaml
wechat:
  mp:
    app-id: app-id #对应微信公众号 id
    secret: app-secret #微信公众号 secret
```

如果配置成功，则可以成功注入WxMpService bean。
WxMpService及相关API的使用请参见[WxJava项目](https://github.com/Wechat-Group/WxJava)的[Wiki](https://github.com/Wechat-Group/WxJava/wiki)

```java
@SpringBootApplication
public class PegasusApplication {

    public static void main(String[] args) {
        SpringApplication.run(PegasusApplication.class, args);
    }

    
    @Autowired
    private WxMpService wxMpService;
}
```

> 因为[WxJava项目](https://github.com/Wechat-Group/WxJava)已经增加了相应的starter模块,故声明一下，本项目与WxJava提供的starter相比主要提供了一些安全方面的配置，以及更多的简化配置。

目前本项目已完成的简化配置如下:

1.本地memory和Redis服务的切换，主要区别在于AccessToken的存储位置一个在本地，一个在redis.
2.参数配置即可接入微信服务号消息,即使用2-3个参数配置，即可完成微信服务号的消息接入功能
3.基于spring security和微信oauth2.0的用户认证及鉴权机制

本项目的详细使用说明请参见[wiki](https://gitee.com/lyfuci/Rambutan/wikis/Home)