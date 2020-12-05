
# 使用Maven安装Node和Npm包管理器构建开发环境  
执行```mvn frontend:install-node-and-npm```命令生成node文件夹  
执行```node --version```检查node版本  
执行```./npm --version```检查npm版本  

# 安装angular cli 客户端  
执行```./npm install @angular/cli```命令安装angular cli   
执行```./ng --version```命令检查angular cli版本  

# 使用angular cli初始化项目   
执行```./ng new my-app```命令初始化一个my-app的工程  
初始化选项  
1. 不添加Angular routing  
2. 使用CSS样式 
```
? Would you like to add Angular routing? No
? Which stylesheet format would you like to use? CSS
```  


./npm install bootstrap@3 jquery --save