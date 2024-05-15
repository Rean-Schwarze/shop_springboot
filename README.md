# 商店后端

API Document：[https://3u4leyl0cw.apifox.cn](https://3u4leyl0cw.apifox.cn)

------

## 已实现

### 用户

新用户的自主**注册**

注册用户的**登录，注销**

注册用户的**浏览，查询，购买**等操作流程（浏览/查询→添加至购物篮→付款→发送电子邮件确认发货）

未登录用户浏览销售商品

<br>

用户头像上传、修改

用户订单列表查询

用户收货地址的添加、修改

用户基本信息的修改

<br>

### 销售人员

所负责销售商品的**添加、删除**

所负责销售商品类别信息的**修改**（销售价格，在库数目）

所负责销售商品类别的销售状态信息的**监控**

用户购买其所负责销售商品的 浏览/购买 的 **日志** 记录

<br>

所负责销售商品的图片上传

<br>

### 管理员

对销售人员ID的**管理**（销售人员的ID添加，删除）

对销售人员ID的登录口令的**重置**

各个商品类别的销售统计报表， 销售状态，库存管理

<br>

获取销售人员列表

<br>

## 收集记录的数据信息（大数据）

### 用户

登录以及退出的时间和IP地址

访问的商品类别以及停留的时长（单位：秒）

购买商品的记录（商品类别，购买日期，购买的单价以及数量）

<br>

### 销售人员&管理员

登录以及退出的时间和IP地址

操作日志（包括查询在内的所有操作记录：操作时间，内容，IP地址，账号，etc.）

<br>

## 基于大数据的数据分析以及推荐系统

暂无

<br>

## 其他可选的附加功能

反爬虫侦测和应对

<br>

## Todo

### 用户

敏感信息修改（密码等）

订单列表页同订单分商家显示（如图）

![image-20240507154529363](https://rean-blog-bucket.oss-cn-guangzhou.aliyuncs.com/assets/img/image-20240507154529363.png)

订单详情页

取消订单

确认收货、评价

**关闭**窗口前发送日志（`beforeunload`只对刷新起作用）

#### 浏览方面

搜索

收藏

评论查看

……

<br>

### 销售人员

所负责销售商品的**其他信息**的修改

订单状态、信息修改

销售统计

工单系统

……

<br>

### 管理员

销售人员信息修改

销售人员注册 - 添加头像

对销售人员负责商品类别的销售业绩的查询和监控

各个商品类别的销售业绩的查询和统计

……

### 大数据相关

用户画像构建（从地域，购买力，主要购买物品类别等因素对用户的分类）

用户行为分析、数据挖掘、个性化推荐（购买趋势预测与评估）

商品销售趋势预测与评估

销售异常的判别与实时监控

数据的导入和导出

数据分析结果的在线可视化
