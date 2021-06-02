# x-runtime

x-runtime 是一个开发平台，用于**极速开发生产级别应用**。

## 为什么需要 x-runtime

当看到 Agora 的灵动课堂（aPaaS）产品时，我眼前一亮，因为它能够在大部分场景下极大的提升我开发在线课堂类应用的速度。

但是实际构建应用时，我的还有许多业务逻辑在灵动课堂之外，这部分业务的开发低效且质量低。

![](https://user-images.githubusercontent.com/13651389/120494788-b1f22000-c3ee-11eb-8987-dd4e20f286e9.png)

x-runtime 则是为解决这一情况所诞生的。x-runtime 会提供一组应用开发设施，帮助开发者高效构建高质量的业务逻辑，使得整个应用的开发效率与质量都达到生产级别。

## x-runtime 设施

x-runtime 中包含了 4 项设施：

- Data: 高可用 PostegreSQL 集群 + GraphQL CRUD API
- Event: 事件引擎，用于触发基于事件驱动的业务逻辑
- Function：函数服务，可以将业务逻辑容器化后部署为函数服务
- UIgen：自动生成与 Data 连接的 UI

除此之外，x-runtime 本身定义了一套元数据（meta），并提供一套用于快速描述元数据的 DSL。开发者可以使用 DSL 定义 Data 和 UIgen。

整体架构如图所示：

![](https://user-images.githubusercontent.com/13651389/120497397-f1217080-c3f0-11eb-82e2-6618a469e3bf.png)

## 生产级别

1. 高可用。x-runtime 部署的所有组件都保证高可用、无单点故障。
2. 负载均衡。x-runtime 为 Data 与 Function 提供负载均衡、弹性扩/缩容、灰度发布等生产就绪特性。
3. 全链路类型安全。通过大量严格细致的 codegen，x-runtime 能够保证从 DB schema 到 UI typescript 代码的类型安全，静态检查即可保证运行时代码正确。
4. UI 最佳实践。UIgen 生成的代码自带 i18n、可访问性处理，帮助应用从第一天开始达到交付标准。
5. 部署一致性。x-runtime 所有组件部署在 kubernetes 之上，通过应用 x-runtime 提供的 k8s manifests，即可在任意 k8s 集群中部署一致的 x-runtime 平台及应用。

部署一致性示意：

![](https://user-images.githubusercontent.com/13651389/120500179-3cd51980-c3f3-11eb-9f16-27aaacad1b6a.png)

## 灵活拓展

1. UI 渐进式自定义渲染。作为需求最为灵活多变的 UI 部分，x-runtime UIgen 既保障生成的默认 UI 优雅易用，也提供多层级（组件、资源类型、资源字段、交叉组合）自定义渲染。同时全链路类型安全也保证在持续的迭代中自定义 UI 始终表现正确。
2. Function 服务不限制编程语言与运行时。x-runtime Function 可以将任何标准的容器化服务部署在平台上，结合 Event 事件机制可以灵活拓展业务需求。
3. 组件可替换。x-runtime 在组件交互处做了清晰的切分，开发者可以按需替换核心组件。例如在测试环境使用 x-runtime 自部署的 PostgreSQL 集群，在生产环境使用 AWS Aurora。