## WxJava mp + spring security 的spring boot starter [![Maven Central](https://img.shields.io/maven-central/v/xyz.seansun/rambutan-spring-boot-starter.svg)](https://gitee.com/lyfuci/Rambutan)

该项目主要致力于降低微信服务号开发难度，提高服务号开发的安全性，在spring-boot-starter-web项目中，一个最简单的配置可能如下所示，即完成了公众号的服务接入。

application.yml: 
```yaml
wechat:
  mp:
    app-id: app-id #对应微信公众号 id
    secret: app-secret #微信公众号 secret
```
如果配置成功，则可以成功注入WxMpService bean。
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

详细的使用说明请参见[wiki](https://gitee.com/lyfuci/Rambutan/wikis/Home)