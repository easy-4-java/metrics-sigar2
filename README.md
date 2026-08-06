# metrics-sigar2

[Overview](#1-project-overview) | [Features](#2-features--status) | [Requirements](#3-requirements--compatibility) | [Architecture](#4-architecture--modules) | [Installation](#5-installation) | [Quick Start](#6-quick-start) | [Configuration](#7-configuration) | [Core Usage](#8-core-usage--api) | [Testing & Build](#9-testing--build) | [Versioning](#10-versioning--branches) | [License](#11-contributing--license)

> **Status**: maintained on the `feature/3.0.x` line (JDK 21). Artifacts are not yet published to Maven Central; they are distributed through the project's private repository and GitHub Releases.

## 1. Project Overview

`metrics-sigar2` exposes OS-level metrics of the host JVM as Dropwizard Metrics gauges using [Hyperic Sigar](https://github.com/hyperic/sigar) (org.hyperic:sigar 1.6.5.132-6). Sigar calls OS native APIs to report CPU, memory, swap, filesystem and ulimit information; this module registers those values as `Gauge`s in a `MetricRegistry`.

What it is:

- `SigarMetrics` — a singleton facade (`registerGauges(MetricRegistry)`, `pid()`, `cpu()`, `memory()`, `filesystems()`, `ulimit()`) that registers the standard gauge set in one call;
- `CpuMetrics` — gauges for total cores, physical CPUs and CPU-time user/sys percentages (`RatioGauge`), plus a `CpuTime` value object;
- `MemoryMetrics` — gauges for free/actual-free/used/actual-used/total memory, used/free percent and swap free/pages-in/pages-out;
- `UlimitMetrics` — gauges for open-files limit and stack size;
- `FilesystemMetrics` — filesystem enumeration (`filesystems()`, `FileSystem` value object with device/mount/type/size); no gauges are auto-registered for filesystems;
- utilities — `JVMInfo` / `JVMProperty`, `OSEnvInfo` / `OSProperty`, `MemProperty`, `JMXInfo`, `MemoryInfo`, unit helpers (`CapacityUnit`, `CapacityUtils`, `VolumeUnit`, `AcreageUnit`, `LengthMetricUnit`, ...), `MemoryWarningSystem` (JMX memory-percentage warning listener);
- RMI remote access — `SigarRemoteService` (`getRuntime()` → `SigarRMIInfo`) with `DefaultSigarRemoteServiceImpl` and `SystemRuntime`.

What it is not:

- Not cross-platform out of the box — it needs the Sigar **native library** matching the OS/arch on `java.library.path` (see the table below);
- Not a reporter — you still choose how gauges are exported (JMX, Graphite, Prometheus, ...).

Typical scenarios:

| Scenario | What to use |
| :--- | :--- |
| Register all OS gauges in one call | `SigarMetrics.getInstance().registerGauges(registry)` |
| CPU cores / utilization | `SigarMetrics.cpu()` gauges |
| Memory / swap monitoring | `SigarMetrics.memory()` gauges |
| Low-memory warning | `MemoryWarningSystem` (JMX notification listener) |
| Remote runtime info over RMI | `SigarRemoteService` / `SystemRuntime` |

## 2. Features & Status

| Capability | Status | Notes |
| :--- | :--- | :--- |
| One-call gauge registration | Implemented | `SigarMetrics.registerGauges(MetricRegistry)` (pid + cpu + memory + ulimit) |
| CPU gauges | Implemented | total-cores, physical-cpus, cpu-time-user-percent, cpu-time-sys-percent |
| Memory gauges | Implemented | memory-free/actual-free/used/actual-used/total, used/free percent, swap free/pages-in/pages-out |
| Ulimit gauges | Implemented | ulimit-open-files, ulimit-stack-size |
| Filesystem access | Implemented | `FilesystemMetrics.filesystems()`; gauges intentionally **not** auto-registered |
| JVM/OS info helpers | Implemented | `JVMInfo`, `OSEnvInfo`, `JMXInfo`, `MemProperty`, ... |
| Low-memory warning | Implemented | `MemoryWarningSystem` (`setPercentageUsageThreshold`, `Listener`) |
| RMI remote service | Implemented | `SigarRemoteService` + `DefaultSigarRemoteServiceImpl` |
| Tests | Present | JUnit 4 + Mockito + Hamcrest (`SigarMetricsTest`, `CpuMetricsTest`, `MemoryMetricsTest`, `FilesystemMetricsTest`, `UlimitMetricsTest`, ...) |

## 3. Requirements & Compatibility

| Item | Requirement |
| :--- | :--- |
| JDK | 21+ |
| Maven | 3.0+ (Maven Wrapper `mvnw` included) |
| Dependencies | org.hyperic sigar 1.6.5.132-6, metrics-core 4.1.1, commons-lang3 3.20.0, commons-text 1.15.0, slf4j-api 2.0.18, lombok (provided) |
| Test deps | junit 4.13.2, hamcrest 1.3, mockito 2.23.4, slf4j-simple |
| Native library | Sigar native lib for your OS/arch on `java.library.path` (see below) |

Version lines:

| Branch | JDK | Version pattern |
| :--- | :---: | :--- |
| `feature/1.0.x` | 8 | `1.0.x.*` |
| `feature/2.0.x` | 17 | `2.0.x.*` |
| `feature/3.0.x` | 21 | `3.0.x.*` |

### Sigar native library vs platform

| Platform / arch | Native library file |
| :--- | :--- |
| Linux AMD/Intel 32-bit | `libsigar-x86-linux.so` |
| Linux AMD/Intel 64-bit | `libsigar-amd64-linux.so` |
| Linux PowerPC 32-bit | `libsigar-ppc-linux.so` |
| Linux PowerPC 64-bit | `libsigar-ppc64-linux.so` |
| Linux Itanium 64-bit | `libsigar-ia64-linux.so` |
| Linux zSeries 64-bit | `libsigar-s390x-linux.so` |
| Windows AMD/Intel 32-bit | `sigar-x86-winnt.dll` |
| Windows AMD/Intel 64-bit | `sigar-amd64-winnt.dll` |
| AIX PowerPC 32-bit | `libsigar-ppc-aix-5.so` |
| AIX PowerPC 64-bit | `libsigar-ppc64-aix-5.so` |
| HP-UX PA-RISC 32-bit | `libsigar-pa-hpux-11.sl` |
| HP-UX Itanium 64-bit | `libsigar-ia64-hpux-11.sl` |
| Solaris Sparc 32-bit | `libsigar-sparc-solaris.so` |
| Solaris Sparc 64-bit | `libsigar-sparc64-solaris.so` |
| Solaris AMD/Intel 32-bit | `libsigar-x86-solaris.so` |
| Solaris AMD/Intel 64-bit | `libsigar-amd64-solaris.so` |
| Mac OS X PPC/Intel 32-bit | `libsigar-universal-macosx.dylib` |
| Mac OS X PPC/Intel 64-bit | `libsigar-universal64-macosx.dylib` |
| FreeBSD 5.x AMD/Intel 32-bit | `libsigar-x86-freebsd-5.so` |
| FreeBSD 6.x AMD/Intel 64-bit | `libsigar-x86-freebsd-6.so` |
| FreeBSD 6.x AMD/Intel 64-bit | `libsigar-amd64-freebsd-6.so` |

## 4. Architecture & Modules

```text
Application (MetricRegistry)
        |
        v
SigarMetrics (singleton)
   |--> CpuMetrics     (cores, cpu-time user/sys %)
   |--> MemoryMetrics  (memory free/used/total, swap ...)
   |--> FilesystemMetrics (filesystems() enumeration, no gauges)
   `--> UlimitMetrics  (open files, stack size)
        |
        v
org.hyperic.sigar.Sigar (JNI -> OS native API)
```

Single-module jar. Packages under `com.codahale.metrics.sigar`:

| Package | Contents |
| :--- | :--- |
| `com.codahale.metrics.sigar` | `SigarMetrics`, `CpuMetrics`, `MemoryMetrics`, `FilesystemMetrics`, `UlimitMetrics`, `AbstractSigarMetric`, `CanRegisterGauges` |
| `com.codahale.metrics.sigar.utils` | `JVMInfo`, `JVMProperty`, `JVMOSProperty`, `OSEnvInfo`, `OSProperty`, `MemProperty`, `JMXInfo`, `MemoryInfo`, `MemoryWarningSystem`, unit helpers (`CapacityUnit`, `CapacityUtils(2)`, `VolumeUnit`, `AcreageUnit`, `LengthMetricUnit`, `LengthBritishUnit`) |
| `com.codahale.metrics.sigar.rmi` | `SigarRemoteService`, `SigarRMIInfo`, `SystemRuntime`, `impl.DefaultSigarRemoteServiceImpl` |
| resources | `sigar_zh_CN.properties` (Chinese labels) |

## 5. Installation

```xml
<dependency>
    <groupId>io.github.easy4j</groupId>
    <artifactId>metrics-sigar2</artifactId>
    <version>3.0.x.x.20260630-SNAPSHOT</version>
</dependency>
```

Gradle:

```groovy
implementation 'io.github.easy4j:metrics-sigar2:3.0.x.x.20260630-SNAPSHOT'
```

The snapshot is served from the project's private repository (see `distributionManagement` in the pom). No Maven Central release is available yet. Sigar native binaries come from the [Sigar project](https://sourceforge.net/projects/sigar/) / Hyperic releases and must be placed on `java.library.path`.

## 6. Quick Start

```java
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.sigar.SigarMetrics;

MetricRegistry registry = new MetricRegistry();
SigarMetrics.getInstance().registerGauges(registry);

// registry now contains gauges such as:
//   com.codahale.metrics.sigar.SigarMetrics.pid
//   com.codahale.metrics.sigar.CpuMetrics.total-cores
//   com.codahale.metrics.sigar.CpuMetrics.cpu-time-user-percent
//   com.codahale.metrics.sigar.MemoryMetrics.memory-used
//   com.codahale.metrics.sigar.UlimitMetrics.ulimit-open-files
```

Run with the native library on the path, e.g. on 64-bit Linux:

```bash
java -Djava.library.path=/opt/sigar-native -jar your-app.jar
```

When the native library cannot be loaded, Sigar throws at first access — verify with the bundled check (`CheckSigarLoadsOk` test) before deploying.

## 7. Configuration

No property-file configuration. Runtime requirements:

- native library on `java.library.path` (see platform table in section 3);
- `MemoryWarningSystem.setPercentageUsageThreshold(double)` to configure the low-memory threshold and register a `Listener` for `memoryUsageLow(usedMemory, maxMemory)` notifications.

## 8. Core Usage / API

### 8.1 Individual metric sets

```java
SigarMetrics sigar = SigarMetrics.getInstance();

sigar.cpu().registerGauges(registry);        // CPU gauges only
sigar.memory().registerGauges(registry);     // memory + swap gauges
sigar.ulimit().registerGauges(registry);     // ulimit gauges

long pid = sigar.pid();                      // JVM process id
List<FilesystemMetrics.FileSystem> fss = sigar.filesystems().filesystems();
```

### 8.2 Low-memory warning

```java
import com.codahale.metrics.sigar.utils.MemoryWarningSystem;

MemoryWarningSystem.setPercentageUsageThreshold(0.90); // warn above 90% heap usage
MemoryWarningSystem mws = new MemoryWarningSystem();
mws.addListener((usedMemory, maxMemory) ->
        System.out.println("memory low: " + usedMemory + "/" + maxMemory));
```

## 9. Testing & Build

```bash
./mvnw clean verify
```

The build is configured with:

- JUnit 4 + Mockito + Hamcrest; test suite covers `SigarMetricsTest`, `CpuMetricsTest`, `MemoryMetricsTest`, `FilesystemMetricsTest`, `UlimitMetricsTest`, unit helpers (`CapacityUnit_Test`, `JMXInfo_Test`, `OSEnvInfo_Test`, `JVMInfo_Test`) plus `CheckSigarLoadsOk` (native-library smoke check) and samples (`MyApp`, `RuntimeTest`, `SugarRemoteClient`);
- JaCoCo coverage reporting plus a line-coverage check rule with a 90% minimum target (`haltOnFailure=false`);
- Source and Javadoc jars attached at package time;
- a `central` release profile (GPG signing + Central publishing) reserved for official releases.

## 10. Versioning & Branches

Three parallel version lines, each bound to a JDK baseline:

| Branch | JDK | Version pattern | Maintenance |
| :--- | :---: | :--- | :--- |
| `feature/1.0.x` | 8 | `1.0.x.*` | Current development line |
| `feature/2.0.x` | 17 | `2.0.x.*` | Maintained in parallel |
| `feature/3.0.x` | 21 | `3.0.x.*` | Maintained in parallel |

Snapshots on this branch are versioned `3.0.x.x.20260630-SNAPSHOT`.

## 11. Contributing & License

Contributions are welcome — open an issue or pull request on GitHub. All source files are licensed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt).

### Known troubleshooting notes (community knowledge)

- On macOS/Eclipse, set `java.library.path` to the directory holding the native library (Project properties → Java Build Path → Libraries → native library location).
- The Sigar jar provides no network-transfer-rate method — sample twice and compute the rate yourself.
- With multiple NICs, RMI calls may time out on a LAN IP that is not reachable from the client; switch to a reachable address (e.g. a dual-homed host with `192.168.191.*` and `172.29.131.*` addresses — the client on `192.168.191.*` may fail on the other subnet).
