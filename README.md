# 安装步骤
1. 拉取halo代码
git clone https://github.com/halo-dev/halo.git
2. 添加依赖
implementation "com.github.cooker:halo-plugin-loader:1.0.0-SNAPSHOT"
3. 打包halo
mvn clean install
4. 拉取halo-plugins、打包
5. 将halo-plugin-template编译后的target文件
  > *.yaml
  > *.jar
  放入~/.halo/plugins

# 目标
- [ ] 1.启动插件，载入相关功能
    - [x] 1.1Controller 加载
    - [ ] 1.2DAO 加载
- [ ] 2.支持上下文操作
- [ ] 3.加载静态资源
- [ ] 4.类加载器隔离
- [ ] 5.动态脚本语言，实现DB、上下文操作
- [ ] 6.插件组件细分：判断、转换、视图、输入、输出等..
- [x] 7.插件模板工程
- [x] 8.mvn打包工程



# 参考工程
> https://github.com/pentaho/pentaho-kettle
> 
> https://github.com/apache/tomcat