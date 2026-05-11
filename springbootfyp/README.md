# 员工管理系统 (Staff Management System)

[![Java](https://img.shields.io/badge/Java-19-blue.svg)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue.js-3.x-brightgreen.svg)](https://vuejs.org/)
[![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-3.5.5-red.svg)](https://baomidou.com/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-orange.svg)](https://mysql.com/)

一个功能完善的企业级员工管理系统，涵盖员工信息管理、任务分配、工资管理、加班/请假审批、资产管理等核心业务模块。

## 📋 目录

- [系统功能](#-系统功能)
- [技术栈](#-技术栈)
- [数据库设计](#-数据库设计)
- [快速开始](#-快速开始)
- [环境配置](#-环境配置)
- [项目结构](#-项目结构)
- [联系方式](#-联系方式)

## 🎯 系统功能

### 核心业务模块

| 模块 | 功能说明 |
|:---|:---|
| **员工管理** | 员工信息增删改查、头像上传、技能管理 |
| **任务管理** | 任务发布、子任务拆分、进度跟踪、任务提交审核 |
| **工资管理** | 工资记录、月度统计、备注说明 |
| **加班申请** | 员工提交加班申请、管理员审批、加班时间记录 |
| **请假管理** | 请假申请、审批流程、时间区间记录 |
| **资产管理** | 设备登记、价值统计、故障/闲置情况跟踪 |
| **公告管理** | 公告发布、富文本编辑、发布时间记录 |
| **数据导出** | 支持 Excel 格式导出各类数据 |

### 角色权限

- **管理员 (admin)**：系统配置、任务发布、审批管理、数据导出
- **员工 (employee)**：任务查看、任务提交、加班/请假申请、个人信息维护

## 🛠 技术栈

### 后端技术

| 技术 | 版本 | 用途 |
|:---|:---|:---|
| Java | 19 | 编程语言 |
| Spring Boot | 3.2.0 | 核心框架 |
| MyBatis-Plus | 3.5.5 | ORM 框架 |
| PageHelper | 1.4.6 | 分页插件 |
| MySQL | 8.0+ | 数据库 |
| JWT | 4.3.0 | 身份认证 |
| Hutool | 5.8.18 | 工具库 |
| Apache POI | 4.1.2 | Excel 处理 |
| Lombok | - | 代码简化 |

### 前端技术

| 技术 | 版本 | 用途 |
|:---|:---|:---|
| Vue | 3.4.21 | 前端框架 |
| Vite | 5.2.8 | 构建工具 |
| Vue Router | 4.3.0 | 路由管理 |
| Element Plus | 2.7.2 | UI 组件库 |
| Axios | 1.6.8 | HTTP 请求 |
| ECharts | 5.5.0 | 数据图表 |
| WangEditor | 5.1.23 | 富文本编辑器 |

## 🗄 数据库设计

系统共包含 **10 张核心数据表**：

| 表名 | 说明 | 主要字段 |
|:---|:---|:---|
| `admin` | 管理员表 | 账号、密码、角色 |
| `employee` | 员工表 | 姓名、年龄、技能、联系方式 |
| `task_management` | 任务主表 | 任务名称、总量、截止时间 |
| `subtasks` | 子任务表 | 子任务名、进度、关联任务ID |
| `workrecord` | 任务提交记录 | 完成数量、审核状态 |
| `wages` | 工资管理表 | 员工ID、年月、工资金额 |
| `overwork` | 加班申请表 | 加班时间、审核状态、原因 |
| `qjrecord` | 请假记录表 | 请假时间区间、请假理由 |
| `asset` | 资产信息表 | 设备名称、价值、故障情况 |
| `notice` | 公告表 | 标题、正文、发布时间 |

### 数据库初始化

```sql
-- 创建数据库
CREATE DATABASE employee_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 导入初始化脚本
mysql -u root -p employee_management < init.sql

环境要求

    JDK 19 或更高版本

    Maven 3.6+

    MySQL 8.0+

    Node.js 16+

    npm/pnpm

后端启动（Spring Boot）

    克隆项目
    bash

git clone https://github.com/jhr33/staff-project.git
cd staff-project/springbootfyp

导入数据库
bash

# 使用项目中的 init.sql 文件
mysql -u root -p employee_management < init.sql

配置数据库连接

编辑 src/main/resources/application.yml：
yaml

spring:
  datasource:
    username: root        # 修改为你的数据库用户名
    password: ${DB_PASSWORD}  # 使用环境变量
    url: jdbc:mysql://localhost:3306/employee_management?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2b8

设置环境变量
bash

# Windows
set DB_PASSWORD=你的数据库密码

# Linux/macOS  
export DB_PASSWORD=你的数据库密码

运行项目
bash

mvn clean install
mvn spring-boot:run

    后端服务：http://localhost:8080

前端启动（Vue 3）

    安装依赖
    bash

cd ../vuefyp1
npm install

启动开发服务器
bash

npm run dev

前端服务：http://localhost:5173

生产构建
bash

npm run build

🔐 环境变量配置
变量名	说明	示例
DB_PASSWORD	MySQL 数据库密码	your_secure_password

IDEA 配置方式：

    Run → Edit Configurations → Environment variables

    添加：DB_PASSWORD=your_password

📁 项目结构
text

staff-project/
├── springbootfyp/                 # 后端项目
│   ├── src/main/java/com/example/
│   │   ├── controller/            # 控制器（API接口）
│   │   ├── service/               # 业务逻辑
│   │   ├── mapper/                # 数据访问层
│   │   ├── entity/                # 实体类（对应10张表）
│   │   └── config/                # JWT、跨域等配置
│   ├── src/main/resources/
│   │   ├── mapper/                # MyBatis XML映射
│   │   └── application.yml        # 配置文件
│   └── pom.xml                    # Maven依赖
├── vuefyp1/                       # 前端项目
│   ├── src/
│   │   ├── views/                 # 页面组件
│   │   │   ├── employee/          # 员工管理
│   │   │   ├── task/              # 任务管理
│   │   │   ├── wages/             # 工资管理
│   │   │   ├── overwork/          # 加班申请
│   │   │   ├── qjrecord/          # 请假记录
│   │   │   ├── asset/             # 资产管理
│   │   │   └── notice/            # 公告管理
│   │   ├── router/                # 路由配置
│   │   └── components/            # 公共组件
│   └── package.json               # npm依赖
└── init.sql                       # 数据库初始化脚本

🔧 常见问题
1. 数据库连接失败

    检查 MySQL 服务是否启动

    确认 DB_PASSWORD 环境变量已设置

    验证数据库名是否为 employee_management

2. 前端无法访问后端接口

    确认后端运行在 8080 端口

    检查前端 .env 文件中的 VITE_API_BASE_URL 配置

3. 文件上传失败

    确认 application.yml 中的文件大小限制（当前 100MB）

    检查服务器磁盘空间

📞 联系方式

项目维护者：jhr33

GitHub：jhr33

项目地址：https://github.com/jhr33/staff-project

⭐ 如果这个项目对你有帮助，欢迎 Star！

📝 版本：1.0.0 | 最后更新：2026-04-26




