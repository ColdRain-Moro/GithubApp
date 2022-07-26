# GithubApp

> 红岩网校暑假考核

## 注意事项

> 使用`gradle :app:kspDebugKotlin`刷新ksp缓存	

## 亮点

### 功能较全

也不能算亮点，说实话全是业务代码，快给我写吐了。当然在写业务的过程中也是很有收获的，对MVVM和Flow的理解都有很大提升，业务开发的过程中也会暴露出自己的很多不足。同时我也没有为了堆代码而堆代码，而是尽量的少写代码，尽量的在某些界面相似的地方复用某些使用频率较高的fragment或者activity。因为这次时间比较充足，所以注意了很多细节。

### 封装了一些简单的工具

#### 依赖注入

> 其实挺没必要的（

使用ksp实现了简单的依赖注入，只需使用`@AutoWired`注解即可

~~~kotlin
@AutoWired
lateinit var type: Type
@AutoWired
var repository: Repository? = null
~~~

最开始采用反射实现，后来考虑到性能问题就换成了ksp。实际上是因为懒得一个个删的原因。

不过这个工具也有一些问题，比如对类型的要求比较严格，如果我希望它可空就必须把他声明成可空类型，而不像之前反射一样，即便是空值，只要不使用就不会有问题。而且我其实设计了可以在基类使用的一劳永逸的注入方法`injectAll`,但由于我只在app模块使用了ksp，位于lib_common的基类无法直接调用app模块的ksp生成的代码，如果使用反射又本末倒置了。

其实也可以用asm插桩把这段代码手动插入

### 使用build-logic

这个其实没什么好说的，依赖部分图快抄的郭神的，不过其余逻辑还算是自己设计的，跟郭神的不一样。

当时整这个主要是觉得可能会用到transform（新的资料太少，没怎么了解）进行字节码插桩，但我还是高估自己了，实在不想折腾了。

### 使用了Room的一些相对高级的用法

> 有一种我在写后端的错觉

#### 联表查询

~~~kotlin
@Entity(tableName = "bookmarks", indices = [
    Index(value = ["user_id"], unique = true),
    Index(value = ["repo_id"], unique = true)
])
data class BookmarksEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    val id: Int? = null,
    @ColumnInfo(name = "user_id")
    val userId: Int? = null,
    @ColumnInfo(name = "repo_id")
    val repoId: Int? = null,
    val time: Date,
    // repo / user
    val type: String,
)

@Entity(
    tableName = "local_repos"
)
data class LocalRepoEntity(
    // 使用网络请求返回数据的id，不用自己维护一个id
    @PrimaryKey
    val id: Int,
    val owner: String,
    @ColumnInfo(name = "avatar_url", typeAffinity = ColumnInfo.TEXT)
    val avatarUrl: String,
    val name: String,
    val desc: String?,
    val star: Int,
    val forks: Int,
    val language: String?,
    @ColumnInfo(name = "language_color", typeAffinity = ColumnInfo.TEXT)
    val languageColor: String?
)

@Entity(
    tableName = "local_user"
)
data class LocalUserEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String,
)

data class BookmarksQueryResult(
    @Embedded
    val entity: BookmarksEntity,
    @Relation(parentColumn = "user_id", entityColumn = "id")
    val user: LocalUserEntity? = null,
    @Relation(parentColumn = "repo_id", entityColumn = "id")
    val repo: LocalRepoEntity? = null
)

// BookmarksDao.kt

@Transaction
@Query("SELECT * FROM bookmarks LIMIT :limit OFFSET :offset")
suspend fun queryByPage(limit: Int, offset: Int): List<BookmarksQueryResult>
~~~

首先声明实体类，再声明我们的查询类`BookmarksQueryResult`，使用`@Embedded`引入主要查询实体的全部字段

使用`@Relation`注解关联不同表的两个字段（这两个字段必须是主键或唯一键），然后就可以让Dao直接查询这个实体。需要注意的一点是：这种联表查询事实上查询次数大于一次，所以为了安全起见，我们应当在一个数据库事务中调用它，可以直接使用`@Transaction`让这些查询操作在数据库事务中进行。

#### Paging & Room

在使用Room的一般查询操作来创建PagingSource的情况下我们会发现这个PagingSource创建出来的流无法感知数据库的变化，这也没有办法，毕竟没有办法把Room查询返回的流交给PagingSource处理，这样我们就不得不在每一次数据库增删查改操作时手动调用`PagingAdapter#refersh`更新数据。

然而Paging和Room其实是有联动用法的，我们可以让Room直接返回一个PagingSource，通过这个PagingSource产生的流就可以感知数据库的变化了，并且还会帮你自动分页查询，不需要自己写分页逻辑。

~~~kotlin
@Transaction
@Query("SELECT * FROM bookmarks ORDER BY time DESC")
fun pagingSource(): PagingSource<Int, BookmarksQueryResult>
~~~

甚至也支持联表查询:)

### ~~简单搓了个获取语言对应颜色的后端~~

由于github的接口没有给颜色，于是我自己在github上找了数据上传到数据库，然后用ktor写了个简单的接口

[ColdRain-Moro/gh-language-color-api: 提供获取github语言对应颜色的接口](https://github.com/ColdRain-Moro/gh-language-color-api)

## 开发过程中遇到的问题

### KSP Multiple round processing & Incremental processing

ksp采用多轮处理的方式进行code processing，说实话我没弄明白这个所谓的多轮处理要怎么适配，于是就加了一个判断，让它始终只会进行一次有效的code processing。

但是又因为ksp增量处理的特性，ksp在第二次执行时会从没有解析过的文件开始解析，导致生成的代码不完整，导致了每次都需要`gradle clean`再进行全量编译

最后关闭了增量编译勉强解决

> ksp.incremental=false

### MaterialContainerTransform

整不会了，还好有其他的转场动画

### flowWithLifeCycle导致的数据倒灌

由于郭神最先封装过一个拓展函数，里面直接使用了这个方法，导致了一个鬼畜的现象 -> 我进行请求数据，collect这个flow，在收到数据时跳转页面。而这个页面退出后数据进行了倒灌，于是我又进入了刚才退出的页面。

~~~kotlin
@OptIn(ExperimentalCoroutinesApi::class)
public fun <T> Flow<T>.flowWithLifecycle(
    lifecycle: Lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
): Flow<T> = callbackFlow {
    lifecycle.repeatOnLifecycle(minActiveState) {
        this@flowWithLifecycle.collect {
            send(it)
        }
    }
    close()
}
~~~

看了源码之后发现这是`flowWithLifecycle`的特性，这个方法产生一个冷流，在生命周期发生改变时重新**唤醒上游**，进行一个数据的倒灌。

## ProcessPhoenix源码分析

> 由于直接抄了ProcessPhoenix, 所以在这里做一下源码分析

由于使用ProcessPhoenix首先需要在manifest里面声明一个activity

~~~xml
<activity android:name=".ProcessPhoenix"
            android:process=":phoenix"
            android:exported="true">
</activity>
~~~

所以可以大致猜到，ProcessPhoenix的原理是在崩溃时启动一个Activity

发现这个activity的声明比一般的activity的声明多了一句话

> android:process=":phoenix"

那先看看这个属性的注释

> Specify a specific process that the associated code is to run in. Use with the application tag (to supply a default process for all application components), or with the activity, receiver, service, or provider tag (to supply a specific icon for that component). Application components are normally run in a single process that is created for the entire application. You can use this tag to modify where they run. If the process name begins with a ':' character, a new process private to that application will be created when needed to run that component (allowing you to spread your application across multiple processes). If the process name begins with a lower-case character, the component will be run in a global process of that name, provided that you have permission to do so, allowing multiple applications to share one process to reduce resource usage.
>
> 指定相关代码将在其中运行的特定进程。与应用程序标记（为所有应用程序组件提供默认进程）或活动、接收器、服务或提供程序标记（为其提供特定图标）一起使用零件）。应用程序组件通常在为整个应用程序创建的单个进程中运行。您可以使用此标签来修改它们的运行位置。**如果进程名称以 ':' 字符开头，则在需要运行该组件时将创建一个专用于该应用程序的新进程（允许您将应用程序分布在多个进程中）**。如果进程名称以小写字符开头，则组件将在该名称的全局进程中运行，前提是您有权这样做，从而允许多个应用程序共享一个进程以减少资源使用。

读了这段注释便可以了解到，这个activity将会在一个新的进程中启动。这就好玩了，我还从来没有接触过多进程的app，以往接触的app都是只运行在一个进程上的。

再看看`ProcessPhoenix.java`，这是一个Activity，先从启动它的方法开始分析

~~~kotlin
public static void triggerRebirth(Context context) {
      triggerRebirth(context, getRestartIntent(context));
}
~~~

跳转到它的同名重载

~~~java
public static void triggerRebirth(Context context, Intent... nextIntents) {
        if (nextIntents.length < 1) {
            throw new IllegalArgumentException("intents cannot be empty");
        }
        // create a new task for the first activity.
        nextIntents[0].addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);

        Intent intent = new Intent(context, ProcessPhoenix.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK); // In case we are called with non-Activity context.
        intent.putParcelableArrayListExtra(KEY_RESTART_INTENTS, new ArrayList<>(Arrays.asList(nextIntents)));
        intent.putExtra(KEY_MAIN_PROCESS_PID, Process.myPid());
        context.startActivity(intent);
    }
~~~

这里可以发现，这里启动了这个activity，并且把当前进程的pid作为参数传了进去，当然还传进去了一堆intents，这个等会分析

由于可能需要app作为context来启动activity，所以需要加上FLAG_ACTIVITY_NEW_TASK，同时再加上FLAG_ACTIVITY_CLEAR_TASK，把之前activity栈中的activity清空。

再来看看`getRestartIntent`

~~~java
    private static Intent getRestartIntent(Context context) {
        String packageName = context.getPackageName();
        Intent defaultIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (defaultIntent != null) {
            return defaultIntent;
        }

        throw new IllegalStateException("Unable to determine default activity for "
            + packageName
            + ". Does an activity specify the DEFAULT category in its intent filter?");
    }
~~~

这个就好说了，首先获取当前程序包名，再从通过包名从包管理器里找到对应app的启动Intent并返回。

所以上面作为参数传入的intent数组实际上是启动应用第一个activity的intent。

然后我们看看最后的核心逻辑`onCreate`

~~~java
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Process.killProcess(getIntent().getIntExtra(KEY_MAIN_PROCESS_PID, -1)); // Kill original main process

        ArrayList<Intent> intents = getIntent().getParcelableArrayListExtra(KEY_RESTART_INTENTS);
        startActivities(intents.toArray(new Intent[intents.size()]));
        finish();
        Runtime.getRuntime().exit(0); // Kill kill kill!
    }
~~~

通过传入的上一个进程的pid杀死上一个进程，拿到传入的intent，再通过传入的intent启动一个新的activity——之前我们传入了一个app的启动intent，那么就意味着我们重启了这个应用（在另外一条进程上）。最后这条进程已经没用了，直接杀掉进程，结束。

## 我的图图呢？

![飞书20220725-205643](https://persecution-1301196908.cos.ap-chongqing.myqcloud.com/image_bed/%E9%A3%9E%E4%B9%A620220725-205643.jpg)

![飞书20220725-205649](https://persecution-1301196908.cos.ap-chongqing.myqcloud.com/image_bed/%E9%A3%9E%E4%B9%A620220725-205649.jpg)

![飞书20220725-205652](https://persecution-1301196908.cos.ap-chongqing.myqcloud.com/image_bed/%E9%A3%9E%E4%B9%A620220725-205652.jpg)

![飞书20220725-205655](https://persecution-1301196908.cos.ap-chongqing.myqcloud.com/image_bed/%E9%A3%9E%E4%B9%A620220725-205655.jpg)

![飞书20220725-205700](https://persecution-1301196908.cos.ap-chongqing.myqcloud.com/image_bed/%E9%A3%9E%E4%B9%A620220725-205700.jpg)

![飞书20220725-205703](https://persecution-1301196908.cos.ap-chongqing.myqcloud.com/image_bed/%E9%A3%9E%E4%B9%A620220725-205703.jpg)

![飞书20220725-205708](https://persecution-1301196908.cos.ap-chongqing.myqcloud.com/image_bed/%E9%A3%9E%E4%B9%A620220725-205708.jpg)
