# 项目目录说明

```
src/
 └── main/
     ├── java/
     │   └── com/
     │       └── example/
     │           ├── Application.java                # 启动类
     │           ├── config/                          # 配置类
     │           ├── controller/                      # 控制器层 (Web 层)
     │           ├── service/                         # 服务层 (业务逻辑)
     │           ├── dao/                             # 数据访问层 (MyBatis Mapper)
     │           ├── entity/                          # 实体类
     │           ├── exception/                       # 异常处理
     │           ├── mapper/                          # MyBatis 映射文件 (XML)
     │           ├── utils/                           # 工具类
     │           └── repository/                      # 数据仓库层
     └── resources/
         ├── application.properties                  # Spring Boot 配置文件
         ├── mapper/                                 # MyBatis 映射文件目录
         ├── static/                                 # 静态资源
         ├── templates/                              # 模板文件（如果使用 Thymeleaf）
         └── application.yml                         # 配置文件
```

1. `congfig/` 这个目录包含 Spring Boot 应用的配置类。在这里定义数据库连接池、MyBatis 配置等。
2. `controller/`  Web 层，负责处理 HTTP 请求和返回 HTTP 响应。
3. `service/` 服务层，封装了业务逻辑，负责调用数据访问层（`dao/`）进行数据库操作。`service/` 类通过 依赖注入（`@Autowired`）调用数据访问层的接口进行 CRUD 操作。
4. `dao/` 包含了 **MyBatis Mapper** 接口，这些接口定义了与数据库交互的方法（通常是通过 SQL 查询）。每个接口方法通常对应一个数据库操作。
5. `entity/ `包含数据库的实体类（通常对应数据库中的表）。这些实体类通常包含数据库表的字段，以及一些对应的 Getter 和 Setter 方法（使用lombok）。
6. `mapper/` MyBatis 的 **XML 映射文件**，每个映射文件通常对应一个 `Mapper` 接口。你可以在这些 XML 文件中写 SQL 语句，或者使用 MyBatis 提供的动态 SQL 功能。
7. `exception/` 集中管理应用中的异常处理。可以包含全局异常处理类或自定义异常类。
8. `utils/` 放一些常用的工具类，如日期工具类、字符串工具类等。
9. `repository/` 数据访问层的封装层