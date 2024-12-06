# Flowable-Common

基于 Flowable 工作流引擎的通用工作流解决方案，提供灵活的工作流定义、表单配置和流程管理功能。

## 项目介绍

本项目是一个基于 Flowable 的工作流管理系统，实现了工作流的通用模板功能。主要特点：

- 基于 Flowable 6.7.2 工作流引擎
- 支持动态表单配置
- 灵活的审批流程定义
- 统一的工作流模板管理

## 系统架构

- Spring Boot 2.7.3
- Flowable 6.7.2 
- MyBatis-Plus 3.5.3
- MySQL 8.x

## 项目结构

```
calf-flowable/
├── calf-flowable-service/ # 服务层模块
│ ├── src/main/java
│ └── src/main/resources
│ ├── sql/ # SQL脚本
│ └── 通用审批流程.bpmn20.xml # 通用流程模板定义
└── calf-flowable-web/ # Web层模块
└── src/main/java
└── com/mxs/flowable/controller/ # 控制器
```

## 通用流程模板

项目提供了一个通用的审批流程模板（通用审批流程.bpmn20.xml），具有以下特点：

1. 标准化的审批流程
   - 提交申请
   - 审批节点
   - 修改申请
   - 流程结束

2. 灵活的配置
   - 支持动态指定审批人
   - 可配置多级审批
   - 支持驳回修改

3. 使用方式
   - 位于 resources 目录下
   - 系统启动时自动部署
   - 可通过流程定义关联具体业务

## 核心功能

1. 工作流定义管理
   - 支持工作流类型定义
   - 工单前缀配置
   - 灵活的流程模板管理

2. 表单配置
   - 动态表单字段配置
   - 支持多种字段类型
   - 表单验证规则配置

3. 流程管理
   - 审批流程配置
   - 处理人管理
   - 流程状态跟踪

4. 工单管理
   - 工单创建和跟踪
   - 审批流转
   - 状态管理

## 数据库设计

### 核心表结构

1. WORK_FLOW_DEF: 工作流定义表
   - 存储工作流类型定义
   - 工单编号规则配置

2. WORK_FLOW_FORM_FIELD: 表单字段配置表
   - 动态表单字段定义
   - 字段属性配置

3. WORK_FLOW_ASSIGNEE: 流程处理人配置表
   - 审批流程处理人配置
   - 处理顺序管理

4. WORK_FLOW_ORDER: 工作流工单表
   - 工单信息记录
   - 流程状态追踪

## 快速开始

1. 环境准备
   - JDK 8+
   - MySQL 8.x
   - Maven 3.x

2. 数据库初始化
   ```sql
   # 执行数据库初始化脚本
   source calf-flowable-service/src/main/resources/sql/init.sql

3. 配置修改

   - 修改数据库连接配置
   - 修改应用服务器端口等配置

4. 启动应用
   ```bash
   mvn clean package
   java -jar calf-flowable-web/target/calf-flowable-web.jar
   ```

## 参考文档

- Flowable 示例项目: [flowabletest](https://github.com/jxlhljh/flowabletest)
- [Flowable 官方文档](https://www.flowable.org/docs/userguide/index.html)

## 注意事项

1. 首次使用需要初始化数据库
2. 需要配置正确的数据库连接信息
3. 建议在开发环境进行充分测试后再部署到生产环境

## License

[MIT License](LICENSE)

## 联系方式

如有问题，请提交 Issue 或 Pull Request。
