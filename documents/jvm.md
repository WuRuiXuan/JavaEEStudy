##### java控制台命令

```shell
# 让java代码在后台运行
nohup java [java_reference] &
# 编译java文件，生成字节码文件
java -c [java_file]
# 编译java文件，生成字节码文件，保留方法中参数的名称信息
javac -parameters -d . [java_file]
# 显示反编译字节码文件后的详细信息
javap -v [class_file]
# 查看当前所有java进程的id
jps
```

##### 程序计数器（寄存器）

- 记住下一条jvm指令的执行地址

- 线程私有
- 不会存在内存溢出

##### Stack虚拟机栈

- 每个线程运行时所需要的内存，称为虚拟机栈
- 每个栈由多个栈帧（Frame）组成，对应着每次方法调用时所占用的内存
- 每个线程只能有一个活动栈帧，对应着当前正在执行的那个方法
- 设置栈内存的最大值：-Xss [size]

##### 栈内存溢出

- 栈帧过多
- 栈帧过大

##### 线程运行诊断

监测后台进程的资源占用情况

```shell
top
```

查看指定进程中线程的CPU占用情况

```shell
ps H -eo pid,tid,%cpu | grep [pid]
```

使用jstack工具

```shell
# 通过进程id诊断（死锁）
jstack [pid]
# 通过线程id诊断，把十进制tid换算成十六进制nid，在结果中查找定位
jstack [tid]
```

##### 本地方法栈

给本地方法（C、C++编写的调用操作系统底层的方法）提供内存空间

##### Heap堆内存

- 通过new关键字创建对象都会使用堆内存
- 是线程共享的，堆内存中对象都需要考虑线程安全问题
- 有垃圾回收机制
- 设置堆内存的最大值：-Xmx [size]

##### 堆内存溢出

不断产生新对象，且新对象一直被使用，无法被回收

##### 堆内存诊断

```shell
# 查看当前系统中有哪些java进程
jps
# 查看当前时间点堆内存占用情况
jmap -heap [pid]
# 图形界面的、多功能的监测工具，可以连续监测
jconsole
# 图形化、可视化的监测工具，比jconsole更好用
jvisualvm
```

##### PermGen方法区

- 存储类信息、常量、静态变量等，与堆内存一样，是各个线程共享的内存空间
- 1.7以前是永久代
- 1.7以后是元空间

##### 方法区内存溢出

- 类加载个数过多

- 永久代内存溢出：永久代使用JVM内存，空间相对紧张，垃圾回收效率低，容易溢出
- 元空间内存溢出：元空间使用系统内存，默认无上限，空间相对充裕，不容易溢出

##### Constant Pool方法区常量池

- 常量池：在class文件中的一张表，虚拟机指令根据这张表找到要执行的类名、方法名、参数类型、字面量等信息
- 运行时常量池：当class文件被加载，它的常量池信息就会被放入运行时常量池，并把里面的符号地址（#数字）变为真实地址（内存地址）

##### StringTable串池

- 结构为hash表，相同的字符串对象只存在一份
- 1.7以前存在永久代中，1.7以后存在堆中

- 常量池中的字符串仅是符号，第一次用到时才变为对象
- 利用串池的机制，来避免重复创建字符串对象
- 字符串变量拼接的原理是StringBuilder（1.8）
- 字符串常量拼接的原理是编译期优化
- new String对象存在堆中，而字符串常量对象存在串池中

可以使用intern方法，主动将串池中还没有的字符串对象尝试放入串池，如果有，则不会放入，会返回串池中的对象，如果没有，则：

- 1.7以前，会将原对象拷贝一份放入串池中，并返回串池中的对象（这时候返回的对象与原对象不相等）
- 1.7以后，会将原对象放入串池中，并返回串池中的对象（这时候返回的对象与原对象相等）

##### StringTable垃圾回收

打印串池统计信息：-XX:+PrintStringTableStatistics

打印垃圾回收详细信息：-XX:+PrintGCDetails -verbose:gc

##### StringTable性能调优

用途：有大量字符串常量，且重复较多，入池操作会节省内存

调整桶（Buckets）的数量：-XX:StringTableSize=[number]

入池操作，常量较多时适当增加桶的数量，桶越多越占用空间，桶越少越费时间

```java
list.add(line.intern()); // 先入池再添加进list，防止重复
```

##### Direct Memory直接内存

- 常见于NIO操作时，用于数据缓冲区
- 分配回收成本较高，但读写性能高
- 不受JVM内存回收管理

```java
ByteBuffer.allocateDirect(1024 * 1024); // 分配直接内存
```

##### 直接内存分配和回收原理

- 在任务管理器查看内存占用情况
- 使用了Unsafe对象完成直接内存的分配回收，并且回收需要主动调用freeMemory方法
- ByteBuffer的实现类内部，使用了Cleaner（虚引用）来监测ByteBuffer对象，一旦ByteBuffer对象被垃圾回收，那么就会由ReferenceHandler线程通过Cleaner的clean方法调用freeMemory来释放直接内存

禁用显示的垃圾回收：-XX:+DisableExplicitGC

```java
System.gc(); // 显示的垃圾回收，Full GC
```

##### 可达性分析

把堆内存当前运行的状态转储成一个文件

```shell
jmap -dump:format=b,live,file=[bin_file_name] [pid]
```

使用Eclipse Memory Analyzer打开bin文件（File -> Open Heap Dump）

##### 四种引用

- 强引用
  指创建一个对象并把这个对象赋给一个引用变量
  如果某个对象与强引用关联，那么JVM即使在内存不足的情况下，宁愿抛出Out Of Memory Error错误，也不会回收该对象
  只有所有GC Roots对象都不通过“强引用”引用该对象，该对象才能被垃圾回收

- 软引用（SoftReference）
  如果某个对象与软引用关联，那么在垃圾回收后，内存仍不足时，JVM会再次发出垃圾回收，回收该对象
  可以配合引用队列（ReferenceQueue）来释放软引用自身
- 弱引用（WeakReference）
  如果某个对象与弱引用关联，那么当JVM在进行垃圾回收时，无论内存是否充足，都会回收该对象
  可以配合引用队列（ReferenceQueue）来释放弱引用自身
- 虚引用（PhantomReference）
  如果某个对象与虚引用关联，那么在任何时候都可能被JVM回收掉
  虚引用不能单独使用，必须配合引用队列（ReferenceQueue）一起使用
  主要配合ByteBuffer使用，被引用对象回收时，会将虚引用入队，由Reference Handler线程调用虚引用相关方法释放直接内存
- 终结器引用（FinalReference）
  无需手动编码，但其内部配合引用队列（ReferenceQueue）使用，在垃圾回收时，终结器引用入队（被引用对象暂时没有被回收），再由Finalizer线程通过终结器引用找到被引用对象，并调用它的finalize方法，第二次GC时才能回收被引用对象

##### 垃圾回收算法

- 标记清除（Mark Sweep）：速度较快，会造成内存碎片
- 标记整理（Mark Compact）：速度慢，没有内存碎片
- 复制（Copy）：没有内存碎片，需要占用双倍内存空间

##### 分代垃圾回收

![分代垃圾回收](https://gitee.com/wuruixuan/markdown-images/raw/master/images/分代垃圾回收.png)

- 对象首先分配在伊甸园区域
- 新生代空间不足时，触发minor gc，伊甸园和幸存区from存活的对象使用copy复制到to中，存活的对象年龄加1并且交换幸存区from和to
- minor gc会引发stop the world（STW），暂停其他用户的线程，等垃圾回收结束，用户线程才恢复运行
- 当对象寿命超过阈值时，会晋升至老年代，最大寿命是15（4bit）
- 当老年代空间不足，会先尝试触发minor gc，如果之后空间仍不足，那么触发full gc，STW的时间更长

##### 垃圾回收相关VM参数

| 含义               | 参数                                                        |
| ------------------ | ----------------------------------------------------------- |
| 堆初始大小         | -Xms [size]                                                 |
| 堆最大大小         | -Xmx [size] 或 -XX:MaxHeapSize=[size]                       |
| 新生代大小         | -Xmn [size] 或 -XX:NewSize=[size] -XX:MaxNewSize=[size]     |
| 幸存区比例（动态） | -XX:InitialSurvivorRatio=[ratio] -XX:+UseAdaptiveSizePolicy |
| 幸存区比例         | -XX:SurvivorRatio=[ratio]                                   |
| 晋升阈值           | -XX:MaxTenuringThreshold=[number]                           |
| 晋升详情           | -XX:+PrintTenuringDistribution                              |
| GC详情             | -XX:+PrintGCDetails -verbose:gc                             |
| FullGC前MinorGC    | -XX:+ScavengeBeforeFullGC                                   |
| 永久代大小         | -XX:MaxPermSize=[size]                                      |
| 元空间大小         | -XX:MaxMetaspaceSize=[size]                                 |

##### 垃圾回收器

- 串行
  单线程
  堆内存较小
  适合个人电脑
- 并行
  多线程，并行执行，垃圾回收时用户进程暂停
  堆内存较大，需要多核CPU支持，适合服务器
  吞吐量优先，让单位时间内，STW的时间最短
- 并发
  多线程，并发执行，垃圾回收时用户进程继续
  堆内存较大，需要多核CPU支持，适合服务器
  响应时间优先，让单次STW的时间最短

##### SerialGC串行垃圾回收器

![串行垃圾回收器](https://gitee.com/wuruixuan/markdown-images/raw/master/images/串行垃圾回收器.png)

- 开启串行垃圾回收器
  -XX:+UseSerialGC
  新生代：SerialGC
  老年代：SerialOldGC

##### ParallelGC并行垃圾回收器

![并行垃圾回收器](https://gitee.com/wuruixuan/markdown-images/raw/master/images/并行垃圾回收器.png)

- 开启并行垃圾回收器
  新生代：-XX:+UseParallelGC
  老年代：-XX:+UseParallelOldGC

- 指定并行垃圾回收线程数（一般为CPU核心数）
  -XX:ParallelGCThreads=[number]

- 自适应调节新生代大小和比例
  -XX:+UseAdaptiveSizePolicy

- 垃圾回收时间占总运行时间的比例目标
  -XX:GCTimeRatio=[number]
  计算方式：1 / (1 + number) * 100%

- 最大暂停毫秒数
  -XX:MaxGCPauseMillis=[ms]
  暂停时间越短，垃圾回收时间越短，堆的大小越小，导致吞吐量变小

##### CMS并发垃圾回收器

![CMS垃圾回收器](https://gitee.com/wuruixuan/markdown-images/raw/master/images/CMS垃圾回收器.png)

- 开启CMS垃圾回收器
  新生代：-XX:+UseParNewGC
  老年代：-XX:+UseConcMarkSweepGC

- 指定并发垃圾回收线程数（一般为CPU核心数的四分之一）
  -XX:ConcGCThreads=[number]
- 指定垃圾回收的时机（老年代内存占比达到指定比例时，执行CMS垃圾回收）
  -XX:CMSInitiatingOccupancyFraction=[percent]
- 重新标记前对新生代做一次CMS垃圾回收
  -XX:+CMSScavengeBeforeRemark

##### G1垃圾回收器

- 同时注重吞吐量和低延迟，默认的暂停目标是200ms，取代CMS垃圾回收器
- 超大堆内存，会将堆划分为多个大小相等的Region
- 整体上是标记加整理算法，两个区域之间是复制算法
- 开启G1垃圾回收器：-XX:UseG1GC
- 指定Region大小：-XX:G1HeapRegionSize=[size]
- 老年代占用堆空间比例达到指定阈值时，进行并发标记（不会STW）：-XX:InitiatingHeapOccupancyPercent=[percent]

##### minor gc和full gc

- 新生代内存不足，触发minor gc（SerialGC、ParallelGC、CMS、G1）
- 老年代内存不足，触发full gc（SerialGC、ParallelGC）
- 并发失败（垃圾产生的速度高于垃圾回收的速度），退化为SerialOldGC，并触发full gc（CMS、G1）

##### 最新GC官方文档

https://docs.oracle.com/en/java/javase/15/gctuning/

##### 最新VM参数官方文档

https://docs.oracle.com/en/java/javase/11/tools/java.html

##### 查看当前JVM运行参数

```shell
java -XX:+PrintFlagsFinal -version | findstr "GC"
```

##### 调优相关工具

jmap、jconsole、jvisualvm、mat

##### 选择合适的垃圾回收器

- CMS：低延迟
- ParallelGC：高吞吐量
- G1：结合低延迟和高吞吐量
- ZGC：延迟最低，12版本引入，处于体验阶段
- Zing：对外宣称零停顿，而且可以管理超大内存

##### 新生代调优

- 相比老年代，回收速度快很多
- 并不是空间设置的越大越好（挤占老年代空间，触发full gc），建议设置为堆大小的25%-50%
- 理想值计算：新生代大小 = 一次请求到响应所占用的内存大小 * 并发量
- 幸存区大到能够保留当前活跃对象加需要晋升对象
- 晋升阈值配置适当，让长时间存活的对象尽快晋升

##### 老年代调优

- 老年代内存越大越好
- 如果没有full gc，那么不需要调优老年代
- 观察full gc时老年代内存占用，将老年代内存尝试调大1/4~1/3
- 指定老年代内存占比达到一定比例时，执行CMS垃圾回收（建议75%~85%）

##### JVM字节码规范

https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html

##### 演示多态原理VM参数

禁用指针压缩：-XX:-UseCompressedOops -XX:-UseCompressedClassPointers

##### HSDB工具

进入JDK安装目录，执行

```shell
java -cp ./lib/sa-jdi.jar sun.jvm.hotspot.HSDB
```

进入图形界面，File -> Attach to HotSpot process -> 输入要查看的java进程id

##### 多态调用方法的查找过程

当执行invokevirtual指令时：

1. 先通过栈帧中的对象引用找到对象
2. 分析对象头，找到对象的实际Class
3. Class结构中有vtable，它在类加载的链接阶段就已经根据方法的重写规则生成好了
4. 查表得到方法的具体地址
5. 执行方法的字节码

##### 语法糖

指java编译器把java源码编译为class字节码的过程中，自动生成和转换的一些代码，主要是为了减轻程序员的负担，是java编译器提供的一个福利（糖）

常见语法糖有：

- 自动拆装箱
- 泛型擦除
- 泛型反射
- foreach（数组的foreach遍历会被编译器转换为for循环，List的foreach遍历会被编译器转换为迭代器）
- switch（switch-string：先比较HashCode，再用equals比较；switch-enum：定义一个int数组，保存枚举中所有的序数，再与参数的序数来比较）
- 枚举（本质上是一个class，构造方法参数有name和ordinal）
- try with resources
- 重写桥接（桥接方法：仅对jvm可见，与原来方法的方法名、参数、返回值、修饰符都相同，但没有命名冲突）
- 匿名内部类（把局部变量作为匿名内部类的构造参数）

##### instanceKlass

java类的描述（c++），1.8以前存储在方法区，1.8以后存储在元空间（ _java_mirror 是存储在堆中）

组成部分：

- _java_mirror：java类镜像（class文件）的指针，类镜像同时也有instanceKlass的指针
- _super：父类
- _fields：成员变量
- _methods：方法
- _constants：常量池
- _class_loader：类加载器
- _vtable：虚方法表
- _itable：接口方法表

##### 链接

- 验证：类是否符合JVM规范，安全性检查
- 准备：为static变量分配空间，设置默认值
- 解析：将常量池中的符号引用解析为直接引用
- 初始化：执行类构造器方法的过程

##### 类初始化时机

导致类初始化的情况：

- main方法所在的类，总会被首先初始化
- 首次访问这个类的静态变量或静态方法时
- 子类初始化，如果父类还没初始化，会先触发父类的初始化
- 子类访问父类的静态变量，只会触发父类的初始化
- Class.forName
- new会导致初始化

不会导致类初始化的情况：

- 访问类的static final静态常量（基本类型和字符串）
- 类对象.class
- 创建该类的数组
- 类加载器的loadClass方法
- Class.forName的参数2为false时

##### 惰性初始化模式实现单例

```java
public final class Singleton {
    private Singleton() {}
    // 内部类中保存单例
    private static class LazyHolder {
        static final Singleton INSTANCE = new Singleton();
    }
    // 第一次调用getInstance方法，才会导致内部类加载和初始化其静态成员
    public static Singleton getInstance() {
        return LazyHolder.INSTANCE;
    }
}
```

##### 类加载器

| 名称                                        | 加载哪的类            | 说明                        |
| ------------------------------------------- | --------------------- | --------------------------- |
| 启动类加载器（Bootstrap ClassLoader）       | JAVA_HOME/jre/lib     | 无法直接访问                |
| 扩展类加载器（Extension ClassLoader）       | JAVA_HOME/jre/lib/ext | 上级为Bootstrap，显示为null |
| 应用程序类加载器（Application ClassLoader） | classpath             | 上级为Extension             |
| 自定义类加载器                              | 自定义                | 上级为Application           |

##### 双亲委派机制

当某个类加载器需要加载某个.class文件时，它首先把这个任务委托给它的上级类加载器，递归这个操作，如果上级的类加载器没有加载，自己才会去加载这个类

<img src="https://gitee.com/wuruixuan/markdown-images/raw/master/images/双亲委派机制.png" alt="双亲委派机制" style="zoom: 50%;" />

##### 自定义类加载器

使用：

- 想加载非classpath随意路径中的类文件
- 都是通过接口来实现，希望解耦时，常用在框架设计
- 隔离一些类，不同应用的同名类都可以加载，不冲突，常见于tomcat容器

步骤：

- 继承ClassLoader父类
- 要遵从双亲委派机制，重写findClass方法（注意不是重写loadClass方法，否则不会走双亲委派机制）
- 读取类文件的字节码
- 调用父类的defineClass方法来加载类
- 使用者调用该类加载器的loadClass方法

##### 逃逸分析

Java Hotspot 虚拟机可以分析新创建对象的使用范围，并决定是否在 Java 堆上分配内存的一项技术

逃逸分析的原理：

- 发生在第二编译阶段（JVM 通过解释字节码将其翻译成对应的机器指令）

- 为解决传统 JVM 解释器（Interpreter）的效率问题，引入了 JIT（即时编译）技术
- 当 JVM 发现某个方法或代码块运行特别频繁的时候，就会认为这是“热点代码”（Hot Spot Code），然后 JIT 会把部分“热点代码”翻译成当前平台特定的机器码，并缓存起来，下次遇到相同的代码，直接执行，无需再编译

逃逸分析的 JVM 参数：

- 开启逃逸分析：-XX:+DoEscapeAnalysis
- 关闭逃逸分析：-XX:-DoEscapeAnalysis
- 显示分析结果：-XX:+PrintEscapeAnalysis

逃逸分析的情况：

- 方法逃逸：当一个对象在方法里被定义后，它可能被外部方法所引用，例如作为调用参数传递到其他方法中
- 线程逃逸：被外部线程访问到，譬如赋值给可以在其他线程中访问的实例变量
- 从不逃逸

##### 方法内联

如果发现某个方法是热点方法，并且代码长度不太长时，会进行内联，就是把方法内代码拷贝、粘贴到调用者的位置，还能够进行常量折叠的优化

方法内联的 JVM 参数：

- 输出方法内联信息：-XX:+PrintCompilation -XX:+UnlockDiagnosticVMOptions -XX:+PrintInlining
- 禁用方法内联：-XX:ComplieCommand=dontinline, *JIT2.square

##### 内存模型

JMM定义了一套在多线程读写共享数据（成员变量、数组）时，对数据的可见性、有序性和原子性的规则和保障

##### volatile类型修饰符

- 使线程每次访问该静态变量都从主存中获取最新的值，避免从 JIT 产生的高速缓存中获取过期的值
- 可见性：前一条程序指令的执行结果，可以被后一条指令读到
- 只能保证在多个线程之间，一个线程对 volatile 变量的修改对另一个线程可见，不能保证原子性
- volatile 仅用在一个写线程，多个读线程的情况
- synchronized 既可以保证可见性，又可以保证原子性，属于重量级操作，性能相对更低

##### 指令重排

- 编译器及CPU为了优化代码和执行效率而执行的优化操作
- 在单线程场景下，指令重排不会干扰代码逻辑的执行顺序，而在并发多线程场景下，指令重排会产生不确定的执行效果（需压测才能复现）
- 使用 volatile 修饰变量，则对该变量的读写线程不会产生指令重排的问题

##### double-checked locking模式实现单例

```java
public final class Singleton {
    private Singleton() {}
    // volatile防止指令重排（JDK5以后）
    private volatile static Singleton INSTANCE = null;
    public static Singleton getInstance() {
        // 实例没创建，才会进入内部的synchronized代码块
        if (INSTANCE == null) {
            synchronized (Singleton.class) {
                // 也许有其它线程已经创建了实例，所以再判断一次
                if (INSTANCE == null) {
                    INSTANCE = new Singleton();
                }
            }
        }
        return INSTANCE;
    }
}
```

##### happens-before

规定了哪些写操作对其它线程的读操作可见，是可见性与有序性的一套规则总结：

- 线程解锁之前对变量的写，对于接下来对该变量加锁的其它线程对该变量的读可见
- 线程对 volatile 变量的写，对接下来其它线程对该变量的读可见
- 线程 start 前对变量的写，对该线程开始后对该变量的读可见
- 线程结束前对变量的写，对其它线程得知它结束后的读可见
- 线程 t1 打断（interrupt） t2 前对变量的写，对于其它线程得知 t2 被打断后对变量的读可见
- 对变量默认值（0、false、null）的写，对其它线程对该变量的读可见
- 具有传递性，如果 x happens-before y 且 y happens-before z，那么有 x happens-before z

##### CAS乐观锁

- CAS 是英文单词 Compare And Swap 的缩写，翻译过来就是比较并替换，体现一种乐观锁的思想
- 在多个线程对一个共享变量执行操作时，结合 CAS 和 volatile 可以实现无锁并发，适用于竞争不激烈、多核 CPU 的场景
- CAS 机制当中使用了3个基本操作数：内存地址 V 、旧的预期值 A 、要修改的新值 B
- 更新一个共享变量的时候，只有当旧的预期值 A 和内存地址 V 当中的实际值相同时，才会将内存地址 V 对应的值修改为 B

##### java乐观锁和悲观锁

- 乐观锁 CAS：允许多个线程同时修改共享变量，如果一个线程改的过程中发现有其它线程改过了，就放弃修改，重新尝试
- 悲观锁 synchronized：多个线程去争抢修改共享变量的机会，同一时间只有一个线程修改变量，其它线程都处于等待的状态，等抢到的那个线程改完后，其它线程才继续争抢

##### synchronized优化

- 轻量级锁
- 锁膨胀
- 重量级锁
- 自旋
- 偏向锁

##### 锁优化

- 减少上锁时间：同步代码块中尽量短
- 减少锁的粒度：将一个锁拆分为多个锁提高并发度
- 锁粗化：多次循环进入同步块不如同步块内多次循环，另外 JVM 可能会做如下优化，把多次 append 的加锁操作粗化为一次
- 锁消除：JVM 会进行代码的逃逸分析，例如某个加锁对象是方法内局部变量，不会被其它线程所访问到，这时候就会被即时编译器忽略掉所有同步操作
- 读写分离：CopyOnWriteArrayList、CopyOnWriteSet

