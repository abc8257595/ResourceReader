# Resource Library 说明文档
## 设计原则
1. Design by contract: 资源文件按约定位置放置，同时提供接口进行自定义
2. 封装内部实现，对外统一接口，调用者无需考虑文件解析格式，只需调用Resource接口

## 文件约定
1. 文件夹：分为etc/, res/, i18n/ 三种文件夹，etc/文件夹下根据优先级不同进行查找
    * etc/
        * default.properties
            * setting.json (优先级低，属于系统默认配置)
            * user.conf
            * web.xml and so on
            * user/
                * setting.json (无指定用户，优先级中，查不到往上查找)
                * user.conf
                * max/
                    * setting.json (指定用户，优先级最高，查不到往上查找)
    * res/
        * img/
        * css/
    * i18n/
        * i18n_zh.properties
        * i18n_en.properties
2. 文件格式：根据文件后缀判定格式，从而选择不同的解析器，目前支持格式：**properties, json, conf(properties变形), xml**
3. 基于约定，根据文件名判定从哪个文件夹读取，
	- 文件名符合正则`i18n_(\w{2,5}).properties`则判定为i18n文件，从/i18n文件夹加载
	- 文件后缀为properties, json, conf, xml的，从/etc文件夹下按优先级加载
	- 其它格式从/res文件夹加载

## 类接口说明
Resource接口，定义如下基本操作：
``` java
    V get(K key); 

    V set(K key, V value);

    boolean save(); // 保存到文件
```
Resource接口实现类：PropertyResource, JsonResource, ConfResource, XmlResource, I18NResource

Resource工厂类：ResourceFactory定义如下工厂方法
``` java
Resource getStringResource(String fileName); // 根据约定按文件名查找文件
Resource getStringResource(String path, String fileName); // 指定文件查找路径
Resource getStringResource(Class<?> baseClass, String path, String fileName); // 指定基类，查找路径
```

## 其它
- 使用到Lambda表达式，JDK版本需指定为1.8及以上
- 未完待续...
