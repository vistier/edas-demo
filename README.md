

[EDAS官方文档](https://help.aliyun.com/document_detail/42934.html)

 1. 下载工具
    1. AliTomcat    -- 对应 /soft/taobao-tomcat-7.0.59.tgz
    2. Pandora      -- 对应 /soft/taobao-hsf.tgz         
    3. EDAS 配置中心 -- 对应 /soft/edas-config-center.zip
 2. 安装Pandora 在AliTomcat下的deploy下解压**taobao-hsf.sar**
 3. 安装IntelliJ IDEA 配置AliTomcat插件
    1. 从菜单中或者工具栏中点击 Run，选择 Edit Configuration
    2. 单击界面的加号（+），选择 Tomcat Server，单击 Local，以增加一个本地 Tomcat 的启动配置
    3. 配置 AliTomcat：在右侧页面选择 Server 选项卡，在 Application Server 区域，点击 Configure。 然后在弹出的页面中选择在前面步骤中下载安装的 AliTomcat路径，如：d:\work\tomcat\
    4. 配置好 AliTomcat 后，在 Application Server 区域的下拉菜单中，选择刚刚配置好的 AliTomcat 实例。
    5. 在 VM Options 区域，增加 JVM 启动参数指向 Pandora 的路径，如：-Dpandora.location=d:\work\tomcat\deploy\taobao-hsf.sar
 4. 安装轻量配置中心(在局域网找一个电脑放置就可以)
    1. 正确配置环境变量 JAVA_HOME，指向一个 1.6 或 1.6 以上版本的 JDK。 (统一使用1.7)
    2. 确认 8080 和 9600 端口未被使用。由于启动 EDAS 配置中心将会占用此台机器的 8080 和 9600 端口，因此推荐您找一台专门的机器启动 EDAS 配置中心，比如某台测试机器。如果您是在一台机器上进行测试，请将 Web 项目的端口修改为其它未被占用的端口。
    3. 进入 edas-config-center 目录启动配置中心
    4. 在开发机器配置HOST  ```配置中心IP jmenv.tbsite.net```


 ## 运行项目
    根据上面3配置两Toncat实例 一个发布server, 一个发布client

 运行项目后访问client下```/member/1```既可访问   
