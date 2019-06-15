# HomeworkUpload
一个作业在线提交平台。

这是Levyy的后端，使用前后端分离的形式开发。项目的配置在application.properties

# Deploy
需要JRE以及MongoDB。
### 开发环境
将homeworkupload.isdev设置为true，前端地址写在homeworkupload.dev.fronturl里。
### 生产环境
将homeworkupload.isdev设置为false，默认下homeworkupload.fronturl留空表示不使用CORS，此时后端通过Nginx反代使用同域访问。
