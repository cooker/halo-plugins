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
