# metrics-sigar2

[English](./README.md) | [简体中文](./README.zh-CN.md)

> **项目状态**：`feature/2.0.x` 版本线维护中（JDK 17）。制品尚未发布到 Maven Central，通过项目私服与 GitHub Releases 分发。

## 目录

- [1. 项目概述](#1-项目概述)
- [2. 功能与状态](#2-功能与状态)
- [3. 环境要求与兼容性](#3-环境要求与兼容性)
- [4. 架构与模块](#4-架构与模块)
- [5. 安装](#5-安装)
- [6. 快速开始](#6-快速开始)
- [7. 配置](#7-配置)
- [8. 核心用法 / API](#8-核心用法--api)
- [9. 测试与构建](#9-测试与构建)
- [10. 版本与分支](#10-版本与分支)
- [11. 贡献与许可](#11-贡献与许可)

## 1. 项目概述

`metrics-sigar2` 使用 [Hyperic Sigar](https://github.com/hyperic/sigar)（org.hyperic:sigar 1.6.5.132-6）将宿主机操作系统级指标暴露为 Dropwizard Metrics gauge。Sigar 通过调用操作系统原生 API 上报 CPU、内存、交换分区、文件系统与 ulimit 信息；本模块将这些值注册为 `MetricRegistry` 中的 `Gauge`。

是什么：

- `SigarMetrics`——单例门面（`registerGauges(MetricRegistry)`、`pid()`、`cpu()`、`memory()`、`filesystems()`、`ulimit()`），一次调用注册标准 gauge 集；
- `CpuMetrics`——总核数、物理 CPU 数与 CPU 时间 user/sys 百分比（`RatioGauge`），另有 `CpuTime` 值对象；
- `MemoryMetrics`——free/actual-free/used/actual-used/total 内存、used/free 百分比与 swap free/pages-in/pages-out gauge；
- `UlimitMetrics`——打开文件数与栈大小 gauge；
- `FilesystemMetrics`——文件系统枚举（`filesystems()`、含 device/mount/type/size 的 `FileSystem` 值对象）；文件系统不自动注册 gauge；
- 工具类——`JVMInfo` / `JVMProperty`、`OSEnvInfo` / `OSProperty`、`MemProperty`、`JMXInfo`、`MemoryInfo`、单位换算（`CapacityUnit`、`CapacityUtils`、`VolumeUnit`、`AcreageUnit`、`LengthMetricUnit` 等）、`MemoryWarningSystem`（基于 JMX 内存百分比阈值的告警监听）；
- RMI 远程访问——`SigarRemoteService`（`getRuntime()` → `SigarRMIInfo`）及 `DefaultSigarRemoteServiceImpl`、`SystemRuntime`。

不是什么：

- 不是开箱即用的跨平台组件——需要与操作系统/架构匹配的 Sigar **原生库**位于 `java.library.path`（见下文对照表）；
- 不是 reporter——gauge 的导出方式（JMX、Graphite、Prometheus 等）由你自行选择。

典型场景：

| 场景 | 使用 |
| :--- | :--- |
| 一次调用注册全部系统 gauge | `SigarMetrics.getInstance().registerGauges(registry)` |
| CPU 核数 / 利用率 | `SigarMetrics.cpu()` 系列 gauge |
| 内存 / 交换分区监控 | `SigarMetrics.memory()` 系列 gauge |
| 内存不足告警 | `MemoryWarningSystem`（JMX 通知监听） |
| 通过 RMI 获取远程运行时信息 | `SigarRemoteService` / `SystemRuntime` |

## 2. 功能与状态

| 能力 | 状态 | 说明 |
| :--- | :--- | :--- |
| 一次调用注册 gauge | 已实现 | `SigarMetrics.registerGauges(MetricRegistry)`（pid + cpu + memory + ulimit） |
| CPU gauge | 已实现 | total-cores、physical-cpus、cpu-time-user-percent、cpu-time-sys-percent |
| 内存 gauge | 已实现 | memory-free/actual-free/used/actual-used/total、used/free 百分比、swap free/pages-in/pages-out |
| Ulimit gauge | 已实现 | ulimit-open-files、ulimit-stack-size |
| 文件系统访问 | 已实现 | `FilesystemMetrics.filesystems()`；有意**不**自动注册 gauge |
| JVM/OS 信息辅助 | 已实现 | `JVMInfo`、`OSEnvInfo`、`JMXInfo`、`MemProperty` 等 |
| 低内存告警 | 已实现 | `MemoryWarningSystem`（`setPercentageUsageThreshold`、`Listener`） |
| RMI 远程服务 | 已实现 | `SigarRemoteService` + `DefaultSigarRemoteServiceImpl` |
| 测试 | 已有 | JUnit 4 + Mockito + Hamcrest（`SigarMetricsTest`、`CpuMetricsTest`、`MemoryMetricsTest`、`FilesystemMetricsTest`、`UlimitMetricsTest` 等） |

## 3. 环境要求与兼容性

| 项目 | 要求 |
| :--- | :--- |
| JDK | 17+ |
| Maven | 3.0+（内置 Maven Wrapper `mvnw`） |
| 依赖 | org.hyperic sigar 1.6.5.132-6、metrics-core 4.1.1、commons-lang3 3.20.0、commons-text 1.15.0、slf4j-api 2.0.18、lombok（provided） |
| 测试依赖 | junit 4.13.2、hamcrest 1.3、mockito 2.23.4、slf4j-simple |
| 原生库 | 与操作系统/架构匹配的 Sigar 原生库位于 `java.library.path`（见下表） |

版本线：

| 分支 | JDK | 版本模式 |
| :--- | :---: | :--- |
| `feature/1.0.x` | 8 | `1.0.x.*` |
| `feature/2.0.x` | 17 | `2.0.x.*` |
| `feature/3.0.x` | 21 | `3.0.x.*` |

### Sigar 原生库与平台对照

| 平台 / 架构 | 原生库文件 |
| :--- | :--- |
| Linux AMD/Intel 32 位 | `libsigar-x86-linux.so` |
| Linux AMD/Intel 64 位 | `libsigar-amd64-linux.so` |
| Linux PowerPC 32 位 | `libsigar-ppc-linux.so` |
| Linux PowerPC 64 位 | `libsigar-ppc64-linux.so` |
| Linux Itanium 64 位 | `libsigar-ia64-linux.so` |
| Linux zSeries 64 位 | `libsigar-s390x-linux.so` |
| Windows AMD/Intel 32 位 | `sigar-x86-winnt.dll` |
| Windows AMD/Intel 64 位 | `sigar-amd64-winnt.dll` |
| AIX PowerPC 32 位 | `libsigar-ppc-aix-5.so` |
| AIX PowerPC 64 位 | `libsigar-ppc64-aix-5.so` |
| HP-UX PA-RISC 32 位 | `libsigar-pa-hpux-11.sl` |
| HP-UX Itanium 64 位 | `libsigar-ia64-hpux-11.sl` |
| Solaris Sparc 32 位 | `libsigar-sparc-solaris.so` |
| Solaris Sparc 64 位 | `libsigar-sparc64-solaris.so` |
| Solaris AMD/Intel 32 位 | `libsigar-x86-solaris.so` |
| Solaris AMD/Intel 64 位 | `libsigar-amd64-solaris.so` |
| Mac OS X PPC/Intel 32 位 | `libsigar-universal-macosx.dylib` |
| Mac OS X PPC/Intel 64 位 | `libsigar-universal64-macosx.dylib` |
| FreeBSD 5.x AMD/Intel 32 位 | `libsigar-x86-freebsd-5.so` |
| FreeBSD 6.x AMD/Intel 64 位 | `libsigar-x86-freebsd-6.so` |
| FreeBSD 6.x AMD/Intel 64 位 | `libsigar-amd64-freebsd-6.so` |

## 4. 架构与模块

```text
应用 (MetricRegistry)
        |
        v
SigarMetrics (单例)
   |--> CpuMetrics     (核数, cpu-time user/sys %)
   |--> MemoryMetrics  (memory free/used/total, swap ...)
   |--> FilesystemMetrics (filesystems() 枚举，不注册 gauge)
   `--> UlimitMetrics  (open files, stack size)
        |
        v
org.hyperic.sigar.Sigar (JNI -> 操作系统原生 API)
```

单模块 jar。`com.codahale.metrics.sigar` 下的包结构：

| 包 | 内容 |
| :--- | :--- |
| `com.codahale.metrics.sigar` | `SigarMetrics`、`CpuMetrics`、`MemoryMetrics`、`FilesystemMetrics`、`UlimitMetrics`、`AbstractSigarMetric`、`CanRegisterGauges` |
| `com.codahale.metrics.sigar.utils` | `JVMInfo`、`JVMProperty`、`JVMOSProperty`、`OSEnvInfo`、`OSProperty`、`MemProperty`、`JMXInfo`、`MemoryInfo`、`MemoryWarningSystem`、单位换算（`CapacityUnit`、`CapacityUtils(2)`、`VolumeUnit`、`AcreageUnit`、`LengthMetricUnit`、`LengthBritishUnit`） |
| `com.codahale.metrics.sigar.rmi` | `SigarRemoteService`、`SigarRMIInfo`、`SystemRuntime`、`impl.DefaultSigarRemoteServiceImpl` |
| resources | `sigar_zh_CN.properties`（中文标签） |

## 5. 安装

```xml
<dependency>
    <groupId>io.github.easy4j</groupId>
    <artifactId>metrics-sigar2</artifactId>
    <version>2.0.x.x.20260630-SNAPSHOT</version>
</dependency>
```

Gradle：

```groovy
implementation 'io.github.easy4j:metrics-sigar2:2.0.x.x.20260630-SNAPSHOT'
```

快照版本由项目私服提供（见 pom 中 `distributionManagement`）。尚未发布 Maven Central 正式版。Sigar 原生二进制来自 [Sigar 项目](https://sourceforge.net/projects/sigar/) / Hyperic 发布物，需放到 `java.library.path`。

## 6. 快速开始

```java
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.sigar.SigarMetrics;

MetricRegistry registry = new MetricRegistry();
SigarMetrics.getInstance().registerGauges(registry);

// registry 中将包含如下 gauge：
//   com.codahale.metrics.sigar.SigarMetrics.pid
//   com.codahale.metrics.sigar.CpuMetrics.total-cores
//   com.codahale.metrics.sigar.CpuMetrics.cpu-time-user-percent
//   com.codahale.metrics.sigar.MemoryMetrics.memory-used
//   com.codahale.metrics.sigar.UlimitMetrics.ulimit-open-files
```

运行前将原生库放入加载路径，例如 64 位 Linux：

```bash
java -Djava.library.path=/opt/sigar-native -jar your-app.jar
```

原生库无法加载时，Sigar 会在首次访问时抛异常——部署前可用随附的检查（`CheckSigarLoadsOk` 测试）验证。

## 7. 配置

无属性文件配置。运行时要求：

- 原生库位于 `java.library.path`（见第 3 节平台对照表）；
- `MemoryWarningSystem.setPercentageUsageThreshold(double)` 配置低内存阈值，并注册 `Listener` 接收 `memoryUsageLow(usedMemory, maxMemory)` 通知。

## 8. 核心用法 / API

### 8.1 单独注册指标集

```java
SigarMetrics sigar = SigarMetrics.getInstance();

sigar.cpu().registerGauges(registry);        // 仅 CPU gauge
sigar.memory().registerGauges(registry);     // 内存 + 交换分区 gauge
sigar.ulimit().registerGauges(registry);     // ulimit gauge

long pid = sigar.pid();                      // JVM 进程 ID
List<FilesystemMetrics.FileSystem> fss = sigar.filesystems().filesystems();
```

### 8.2 低内存告警

```java
import com.codahale.metrics.sigar.utils.MemoryWarningSystem;

MemoryWarningSystem.setPercentageUsageThreshold(0.90); // 堆使用超过 90% 时告警
MemoryWarningSystem mws = new MemoryWarningSystem();
mws.addListener((usedMemory, maxMemory) ->
        System.out.println("memory low: " + usedMemory + "/" + maxMemory));
```

## 9. 测试与构建

```bash
./mvnw clean verify
```

构建配置：

- JUnit 4 + Mockito + Hamcrest；测试套件覆盖 `SigarMetricsTest`、`CpuMetricsTest`、`MemoryMetricsTest`、`FilesystemMetricsTest`、`UlimitMetricsTest`、单位工具（`CapacityUnit_Test`、`JMXInfo_Test`、`OSEnvInfo_Test`、`JVMInfo_Test`），另有 `CheckSigarLoadsOk`（原生库冒烟检查）与示例（`MyApp`、`RuntimeTest`、`SugarRemoteClient`）；
- JaCoCo 覆盖率报告 + 行覆盖率检查规则，最低目标 90%（`haltOnFailure=false`）；
- package 阶段附加源码包与 Javadoc 包；
- 提供 `central` 发布 profile（GPG 签名 + Central 发布插件），仅用于正式发布。

## 10. 版本与分支

三条并行版本线，各自绑定一个 JDK 基线：

| 分支 | JDK | 版本模式 | 维护状态 |
| :--- | :---: | :--- | :--- |
| `feature/1.0.x` | 8 | `1.0.x.*` | 当前开发线 |
| `feature/2.0.x` | 17 | `2.0.x.*` | 并行维护 |
| `feature/3.0.x` | 21 | `3.0.x.*` | 并行维护 |

本分支快照版本为 `2.0.x.x.20260630-SNAPSHOT`。

## 11. 贡献与许可

欢迎通过 GitHub Issue 或 Pull Request 参与贡献。所有源码基于 [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt)。

### 已知问题排查（社区经验）

- macOS/Eclipse 下配置 `java.library.path`：项目属性 → Java Build Path → Libraries 中选择 sigar.jar → Native library location 指定原生库所在目录。
- jar 不提供网络传输速率方法——需要采样两次自行计算速率。
- 多网卡时 RMI 可能无法连接：网络中存在多个网卡切换时，建议更换可达的 IP 访问。例如服务器同时处于 `192.168.191.*` 与 `172.29.131.*` 两个局域网，客户端位于 `192.168.191.*` 时连接该网段可能响应超时，连接 `172.29.131.*` 则可正常使用。
