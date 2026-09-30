# Lite IMS Backend

一个基于 Spring Boot 3 和 MyBatis-Plus 实现的轻量级进销存/销售管理系统后端。项目围绕商品、商品分类、客户和销售订单展开，提供登录鉴权、基础资料维护、订单创建与库存扣减、订单状态管理、分页联表查询、Excel 数据导出、库存定时预警以及仪表盘统计等功能。

## 技术栈

- Java 17
- Spring Boot 3.4.4
- Spring MVC
- MyBatis-Plus 3.5.9
- MySQL
- Spring AOP
- Spring Transaction
- Spring Scheduling
- EasyExcel 3.3.2
- Knife4j / OpenAPI 3
- Lombok
- Maven

## 主要功能

| 模块 | 功能 |
| --- | --- |
| 用户登录 | 用户名、密码登录，使用 Session 保存登录状态，支持退出登录 |
| 商品分类 | 分类列表、新增、修改、逻辑删除 |
| 客户管理 | 客户列表、新增、修改、逻辑删除 |
| 商品管理 | 商品分页、按名称模糊查询、按分类筛选、新增、修改、逻辑删除 |
| 订单管理 | 订单分页、按订单号查询、创建订单、库存校验与原子扣减、订单金额计算、订单状态更新 |
| Excel 导出 | 查询订单与客户信息，转换订单状态并生成订单 Excel 文件 |
| 库存预警 | 定时扫描低库存商品，并通过日志输出预警信息 |
| 仪表盘 | 商品总数、今日订单数、今日销售额 |
| 通用能力 | 统一响应结构、登录拦截、请求日志、分页限制、逻辑删除、统一时间格式 |

基础资料模块主要通过 MyBatis-Plus 的通用 Service 完成增删改查；商品列表、订单列表和下单流程涉及多表数据与业务一致性，因此使用自定义 SQL、VO/DTO 分层和事务控制完成。

## 项目结构

```text
src/main/java/org/example/c/backend
├─ aspect/             # AOP 请求日志
├─ common/             # 通用返回结果
├─ config/             # MVC 拦截器、MyBatis-Plus 分页配置
├─ controller/         # REST 接口层
├─ dto/                # 接收前端请求的数据对象
├─ entity/             # 数据库实体
├─ mapper/             # MyBatis-Plus Mapper 接口
├─ service/            # 业务接口
├─ service/impl/       # 业务实现、事务与组合业务
├─ task/               # 定时任务，如低库存检查
└─ vo/                 # 页面响应及 Excel 导出视图对象

src/main/resources
├─ application.yml     # 服务、数据库、Jackson、MyBatis-Plus 配置
└─ org/example/c/backend/mapper
   └─ *.xml             # 自定义映射与联表查询 SQL
```

## 核心设计

### 1. 分层与数据对象

项目采用 `Controller -> Service -> Mapper -> Database` 的常见分层结构：

- Controller 负责接收 HTTP 参数和组装统一响应，不承载复杂业务。
- Service 负责业务规则、事务边界和多个数据表之间的协作。
- Mapper 负责数据库访问；单表操作复用 MyBatis-Plus，复杂查询写在 XML 中。
- Entity 与数据库表对应。
- DTO 只表达前端提交的数据。例如创建订单时，前端只需提交客户 ID、商品 ID 和数量。
- VO 只暴露页面真正需要的数据。例如商品列表额外返回分类名称，但不会把整个分类实体嵌入响应。

这种划分避免直接使用 Entity 同时承担请求、持久化和响应三种职责，也让复杂查询的返回结果更清晰。

### 2. `common`：统一响应结构

`Result<T>` 将所有接口响应统一为以下结构：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

类中提供 `success` 和 `fail` 静态工厂方法，Controller 无需重复创建响应对象。泛型 `T` 让同一个响应外壳可以承载实体、列表、分页结果或空数据，便于前端统一判断 `code`、展示 `message` 并读取 `data`。

### 3. `config`：登录保护与分页控制

#### Session 登录拦截

登录成功后，`LoginController` 将当前 `SysUser` 写入 Session。`LoginInterceptor` 在请求进入 Controller 前读取 Session 中的 `user`：

- 已登录：放行请求；
- 未登录：直接返回 HTTP 401 和 JSON 提示；
- `/api/login`、`/api/logout`：作为公开接口，不参与拦截；
- 其余 `/api/**`：由 `WebConfig` 统一纳入登录校验。

鉴权逻辑集中在拦截器中，业务 Controller 不需要在每个接口里重复检查登录状态。创建订单时则直接从 Session 取得当前用户 ID，记录订单操作人。

#### MyBatis-Plus 分页

`MybatisPlusConfig` 注册 `MybatisPlusInterceptor` 和 MySQL 分页插件。自定义 Mapper 接收 `Page` 参数后，插件会为查询生成分页 SQL，并自动返回总记录数、当前页和总页数。

单页上限被限制为 500 条，避免客户端传入过大的 `size` 导致一次加载大量数据；页码越界时返回空数据，而不是自动跳到最后一页。

### 4. `aspect`：无侵入请求日志

`LogAspect` 将所有 Controller 方法定义为切点，并使用环绕通知记录：

- HTTP 请求方法；
- 请求路径；
- Controller 入参；
- 接口执行耗时。

日志属于横切关注点，通过 AOP 统一处理后，不会散落在每个 Controller 中。环绕通知先记录请求，再执行原方法，最后计算耗时并原样返回执行结果。

### 5. 逻辑删除与字段映射

主要实体包含 `isDeleted` 字段并使用 `@TableLogic`。删除操作在业务代码中仍调用 `removeById`，实际执行时由 MyBatis-Plus 将记录标记为已删除，普通查询会自动忽略这些记录，保留历史数据。

配置中还启用了下划线转驼峰，例如数据库字段 `create_time` 会自动映射到 Java 属性 `createTime`；Jackson 统一使用 `yyyy-MM-dd HH:mm:ss` 和 `Asia/Shanghai` 时区输出时间。

## 复杂业务设计

### 商品分页与分类联表查询

商品列表不能只返回 `categoryId`，页面还需要直接展示分类名称。因此该功能没有使用简单的单表分页，而是在 `ProductMapper.xml` 中联表查询：

```sql
product p JOIN category c ON p.category_id = c.id
```

查询结果映射到 `ProductVO`，其中包含商品 ID、名称、价格、库存和 `categoryName`。SQL 使用动态条件实现组合筛选：

- `name` 有值时拼接 `LIKE`，支持商品名称模糊搜索；
- `categoryId` 有值时按分类精确筛选；
- 始终只读取未逻辑删除的商品。

Service 创建 `Page<ProductVO>` 并传给自定义 Mapper，分页插件负责 SQL 分页与总数统计；随后使用通用 `PageVO` 转换分页元数据，使前端得到统一的 `records`、`total`、`size`、`current` 和 `pages`。

### 订单列表与客户联表查询

订单表只保存 `customerId`，列表页面需要展示客户名称，因此 `SaleOrderMapper.xml` 使用订单表与客户表联查：

```sql
sale_order s
LEFT JOIN customer c
  ON s.customer_id = c.id
 AND c.is_deleted = 0
```

这里使用 `LEFT JOIN`，即使关联客户不存在或已删除，订单主体仍可以出现在列表中；客户的逻辑删除条件写在连接条件中，避免错误地把订单一起过滤掉。查询还具备以下特点：

- 订单自身只查询 `is_deleted = 0` 的记录；
- 支持按订单号精确查询；
- 按创建时间倒序排列；
- 映射为 `OrderVO`，直接返回 `customerName`；
- 使用与商品列表相同的分页封装。

### 创建订单：主从表、金额与库存一致性

创建订单同时涉及 `sale_order`、`sale_order_item` 和 `product` 三张表，是项目中最完整的组合业务。整体流程如下：

```text
接收订单 DTO
   ↓
校验订单项、商品与库存
   ↓
创建订单主表，取得自增 ID
   ↓
逐项原子扣减库存
   ↓
按当前商品价格写入订单明细
   ↓
累计明细金额并回写订单总额
```

具体设计如下：

1. Controller 从 Session 中取得当前登录用户，并将用户与订单 DTO 交给 Service。
2. Service 生成基于时间和随机数的订单号，设置客户、创建人、初始状态和初始金额。
3. 首轮遍历只做预校验：订单项不能为空，商品必须存在，当前库存必须满足数量要求。先完成全部校验，可减少执行到一半才发现普通业务错误的情况。
4. 保存订单主表。主键采用自增策略，插入成功后 `saleOrder.id` 被回填，供明细表作为 `orderId` 使用。
5. 第二轮遍历处理每一项。库存扣减不是“先读取库存再直接保存”，而是生成带条件的原子更新：

   ```sql
   UPDATE product
   SET stock = stock - ?
   WHERE id = ? AND stock >= ?
   ```

   `stock >= quantity` 在数据库执行更新的同一时刻再次校验库存。并发下如果库存已被其他订单占用，更新行数为 0，Service 抛出异常，从而降低超卖风险。
6. 使用数据库中读取到的商品价格计算 `price × quantity`，生成订单明细并累计订单总金额。价格由服务端确定，避免直接信任客户端传入的金额。
7. 所有明细完成后，将累计金额回写到订单主表。
8. `addOrder` 使用 `@Transactional(rollbackFor = Exception.class)`。任一商品扣减、明细写入或订单更新失败都会抛出异常，使主订单、订单明细和库存修改整体回滚，避免出现“有订单无明细”或“扣了库存但订单失败”的不一致状态。

### 仪表盘统计

仪表盘将多个业务数据组合为 `DashBoardVO`：

- 商品总数：统计当前有效商品；
- 今日订单数：构造 `[今天 00:00:00, 明天 00:00:00)` 的时间区间查询订单；
- 今日销售额：遍历今日订单并累加 `totalAmount`。

使用左闭右开的时间范围，可以覆盖今天的全部时间，同时不会误包含明天零点的数据。聚合结果通过一个接口一次返回，减少首页发起多次请求。

### 订单状态更新

订单状态通过独立接口更新，而不是要求前端提交完整订单对象：

```text
PUT /api/orders/{id}/status?status={status}
```

Controller 提取路径中的订单 ID 和请求参数中的目标状态，Service 根据 ID 查询订单、修改 `status` 字段，再调用 MyBatis-Plus 的 `updateById` 完成持久化。该设计缩小了接口的修改范围，避免更新状态时误覆盖订单编号、客户、金额等无关字段。

当前接口直接接收整数状态，尚未限制合法取值，也没有校验状态之间的流转顺序，适合基础管理场景。后续可以使用枚举统一状态含义，并增加“待处理只能进入已完成或已取消”等业务规则。

### Excel 订单导出

订单导出使用 EasyExcel 实现，处理流程如下：

```text
查询全部有效订单
   ↓
根据 customerId 查询客户名称
   ↓
将订单实体转换为 OrderExportVO
   ↓
将数字状态转换为中文状态
   ↓
生成 order.xlsx
```

`OrderExportVO` 专门定义导出列，通过 `@ExcelProperty` 设置“订单号、客户名称、订单金额、订单状态、创建时间”等表头，并通过 `@ColumnWidth` 控制默认列宽和时间列宽。导出过程不直接使用数据库实体，可以独立调整表格字段和展示格式，不影响订单表结构及普通接口响应。

当前状态转换规则为：状态 `1` 显示“已完成”，状态 `2` 显示“已取消”，其他状态显示“待处理”。生成的文件保存在运行后端服务的本机，接口返回统一的成功或失败结果；它目前不是直接返回文件流的下载接口。

### 定时库存预警

启动类使用 `@EnableScheduling` 开启 Spring 定时任务支持。`StockTask` 通过以下 Cron 表达式每分钟执行一次：

```text
0 * * * * ?
```

任务使用 MyBatis-Plus 条件构造器查询 `stock < 10` 的商品：没有低库存商品时记录库存充足日志；存在低库存商品时，逐个输出商品 ID、名称和当前库存的警告日志。该功能与接口请求解耦，即使没有用户访问系统，也会按计划持续检查库存。

## 数据模型关系

```text
category 1 ────── N product
customer 1 ────── N sale_order
sys_user 1 ────── N sale_order
sale_order 1 ──── N sale_order_item
product 1 ─────── N sale_order_item
```

- 分类与商品：一个分类下可以有多个商品。
- 客户与订单：每张订单属于一个客户。
- 用户与订单：订单记录创建该订单的系统用户。
- 订单与明细：一张订单包含多个商品明细。
- 商品与明细：订单明细保存商品、购买数量、成交单价和小计金额。

## 接口概览

除登录与退出接口外，以下 `/api/**` 请求均需要已登录的 Session。

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/login` | 用户登录 |
| GET | `/api/logout` | 退出登录 |
| GET | `/api/categories` | 获取分类列表 |
| POST | `/api/categories` | 新增分类 |
| PUT | `/api/categories` | 修改分类 |
| DELETE | `/api/categories/{id}` | 删除分类 |
| GET | `/api/customers` | 获取客户列表 |
| POST | `/api/customers` | 新增客户 |
| PUT | `/api/customers` | 修改客户 |
| DELETE | `/api/customers/{id}` | 删除客户 |
| GET | `/api/products` | 商品分页与条件查询 |
| POST | `/api/products` | 新增商品 |
| PUT | `/api/products` | 修改商品 |
| DELETE | `/api/products/{id}` | 删除商品 |
| GET | `/api/orders` | 订单分页与订单号查询 |
| POST | `/api/orders` | 创建订单并扣减库存 |
| PUT | `/api/orders/{id}/status` | 更新指定订单状态，使用 `status` 查询参数 |
| GET | `/api/excel/orders/export` | 在服务端生成订单 Excel 文件 |
| GET | `/api/dashboard` | 获取仪表盘统计 |

商品分页参数：`page`、`size`、可选的 `name` 和 `categoryId`。订单分页参数：`page`、`size` 和可选的 `orderNo`。

创建订单请求示例：

```json
{
  "customerId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 3,
      "quantity": 1
    }
  ]
}
```

## 本地运行

### 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.x

### 配置数据库

在 MySQL 中准备名为 `lite_ims` 的数据库及以下业务表：

- `sys_user`
- `category`
- `customer`
- `product`
- `sale_order`
- `sale_order_item`

本项目的 `src/main/resources/application.yml` 包含本机数据库连接信息，因此已加入 `.gitignore`，不会上传至 GitHub。首次下载项目后，可以创建该文件并填写本机配置：

```yaml
server:
  port: 8080

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/lite_ims?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 你的数据库密码
```

本机已有的 `application.yml` 可以继续直接使用，不需要设置环境变量。请不要使用强制添加命令将该文件提交到 Git。

> `createDatabaseIfNotExist=true` 只能帮助创建空数据库，不能自动创建业务表。当前项目也关闭了 SQL 初始化，因此需要提前准备表结构和初始用户数据。

### 启动项目

```powershell
mvn spring-boot:run
```

服务默认启动在：`http://localhost:8080`

Knife4j 接口文档通常可通过以下地址访问：

```text
http://localhost:8080/doc.html
```

## 上传 GitHub 前的注意事项

- 不要提交真实数据库账号和密码。建议使用环境变量或单独的本地配置文件，并提供脱敏后的示例配置。
- 当前登录密码仍按明文比较，适合作为学习项目；生产环境应使用 BCrypt 等方式进行不可逆哈希存储。
- 登录成功后会清除用户对象中的密码，密码字段同时使用 `@JsonIgnore` 禁止序列化，避免写入 Session 或返回给客户端。
- 登录 DTO 的密码字段已从 `toString()` 中排除，避免 AOP 记录请求参数时将密码写入日志。
- 若前后端跨域部署，还需要补充 CORS 与 Session Cookie 策略。
- 高并发场景下可进一步增加唯一订单号策略、数据库约束和库存并发测试。
- `order.xlsx` 是运行导出功能后生成的文件，不属于源代码，提交前应确认是否需要上传。

## 后续可优化方向

- 增加全局异常处理，统一业务异常与参数校验错误。
- 为 DTO 添加 Bean Validation 参数校验。
- 将密码改为 BCrypt，并避免向客户端返回敏感字段。
- 将数据库密码改为环境变量配置。
- 增加订单详情、状态合法性校验、状态流转约束，以及取消订单后的库存返还。
- 将 Excel 导出改为浏览器文件流下载，并将订单与客户的逐条查询优化为一次联表或批量查询。
- 将库存预警阈值和执行周期提取为可配置参数，并接入邮件、短信或站内通知。
- 将仪表盘统计下沉为数据库 `COUNT`、`SUM` 聚合查询。
- 补充建表 SQL、初始化数据、单元测试和接口集成测试。

## License

本项目可作为 Spring Boot 与 MyBatis-Plus 的学习和实践项目使用。若计划开源，请根据实际需求补充 LICENSE 文件。
