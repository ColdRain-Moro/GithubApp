package kim.bifrost.lib_common.extensions

import java.lang.RuntimeException
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.util.concurrent.ConcurrentHashMap
/**
 * kim.bifrost.lib_common.extensions.Reflection
 * SummerExamine
 *
 * @author 寒雨
 * @since 2022/7/13 23:36
 */

/**
 * Get property
 * 获取字段
 *
 * @param path 路径 (使用 / 分隔以递归获取)
 */
@Suppress("UNCHECKED_CAST")
fun <T> Any.getProperty(path: String): T? {
    val reflectClass = ReflectClass.find(this::class.java)
    val deep = path.indexOf('/')
    if (deep == -1) {
        return reflectClass.findField(path)?.get(this) as T?
    }
    // 递归获取
    return reflectClass.findField(path.substring(0..deep))?.get(this)
        ?.getProperty(path.substring(deep))
}

/**
 * Set property
 * 覆写字段
 *
 * @param path 路径 (使用 / 分隔以递归获取)
 * @param any 值
 */
fun Any.setProperty(path: String, any: Any?) {
    val reflectClass = ReflectClass.find(this::class.java)
    val deep = path.indexOf('/')
    if (deep == -1) {
        reflectClass.findField(path)?.set(this, any)
        return
    }
    // 递归覆写
    reflectClass.findField(path.substring(0..deep))?.get(this)
        ?.setProperty(path.substring(deep), any)
}

/**
 * Invoke method
 * 执行实例方法
 *
 * @param path 方法名称
 * @param args 参数
 * @return 返回值
 */
@Suppress("UNCHECKED_CAST")
fun <T> Any.invokeMethod(path: String, vararg args: Any?): T? {
    val reflectClass = ReflectClass.find(this::class.java)
    return (reflectClass.findMethod(path, *args) ?: error("Method not found")).invoke(this,
        *args) as T?
}

/**
 * Construct
 * 使用构造器实例化对象
 *
 * @param T 类型
 * @param args 参数
 * @return 实例
 */
@Suppress("UNCHECKED_CAST")
fun <T> Class<T>.construct(vararg args: Any?): T {
    return (ReflectClass.find(this::class.java).findConstructor(*args)?.newInstance(args)
        ?: error("No constructor find")) as T
}

/**
 * Invoke static
 * 执行静态方法
 *
 * @param T 返回值类型
 * @param path 方法名
 * @param args 参数
 * @return 返回值
 */
@Suppress("UNCHECKED_CAST")
fun <T> Class<*>.invokeStatic(path: String, vararg args: Any?): T? {
    return (ReflectClass.find(this).findMethod(path, *args) ?: error("Method not found")).invoke(null, *args) as T?
}

class ReflectClass(private val clazz: Class<*>) {
    // 超类缓存
    private var superclass: ReflectClass? = null

    // 接口缓存
    private val interfaces = ArrayList<ReflectClass>()

    // 字段缓存
    val savingField = ArrayList<Field>()

    // 方法缓存
    val savingMethod = ArrayList<Method>()

    // 构造器缓存
    val savingConstructor = ArrayList<Constructor<*>>()

    init {
        kotlin.runCatching {
            savingField.addAll(clazz.declaredFields.map {
                it.isAccessible = true
                it
            })
            savingMethod.addAll(clazz.declaredMethods.map {
                it.isAccessible = true
                it
            })
            savingConstructor.addAll(clazz.declaredConstructors.map {
                it.isAccessible = true
                it
            })
            if (clazz.superclass != null && clazz.superclass != Object::class.java) {
                superclass = ReflectClass(clazz.superclass)
            }
            clazz.interfaces.forEach {
                interfaces.add(ReflectClass(it))
            }
        }
    }

    // 获取字段
    fun findField(f: String): Field? {
        savingField.firstOrNull { it.name == f }?.run {
            return this
        }
        superclass?.findField(f)?.run {
            return this
        }
        interfaces.forEach {
            it.findField(f)?.run {
                return this
            }
        }
        return null
    }

    // 获取方法
    fun findMethod(m: String, vararg parameter: Any?): Method? {
        // 优先从当前类中获取
        savingMethod.firstOrNull {
            it.name == m && compare(it.parameterTypes,
                parameter.map { p -> p?.javaClass }.toTypedArray())
        }?.run {
            return this
        }
        // 从超类中获取
        superclass?.findMethod(m, *parameter)?.run {
            return this
        }
        // 从接口中获取
        interfaces.forEach {
            it.findMethod(m, *parameter)?.run {
                return this
            }
        }
        return null
    }

    private fun compare(primary: Array<Class<*>>, secondary: Array<Class<*>?>): Boolean {
        if (primary.size != secondary.size) {
            return false
        }
        for (index in primary.indices) {
            val primaryClass = primary[index].nonPrimitive()
            val secondaryClass = secondary[index]?.nonPrimitive() ?: continue
            if (primaryClass == secondaryClass || primaryClass.isAssignableFrom(secondaryClass)) {
                continue
            }
            return false
        }
        return true
    }

    // 获取构造器
    fun findConstructor(vararg parameter: Any?): Constructor<*>? {
        savingConstructor.firstOrNull {
            if (it.typeParameters.size == parameter.size) {
                var checked = true
                it.parameterTypes.forEachIndexed { index, p ->
                    if (parameter[index] != null && !p.nonPrimitive()
                            .isInstance(parameter[index])
                    ) {
                        checked = false
                    }
                }
                return@firstOrNull checked
            }
            return@firstOrNull false
        }?.run {
            return this
        }
        return null
    }

    // 取得装箱之后的类型
    private fun Class<*>.nonPrimitive(): Class<*> {
        return when {
            this == Integer.TYPE -> Integer::class.java
            this == Character.TYPE -> Character::class.java
            this == java.lang.Byte.TYPE -> java.lang.Byte::class.java
            this == java.lang.Long.TYPE -> java.lang.Long::class.java
            this == java.lang.Double.TYPE -> java.lang.Double::class.java
            this == java.lang.Float.TYPE -> java.lang.Float::class.java
            this == java.lang.Short.TYPE -> java.lang.Short::class.java
            this == java.lang.Boolean.TYPE -> java.lang.Boolean::class.java
            else -> this
        }
    }

    companion object {
        private val savingClass = ConcurrentHashMap<String, ReflectClass>()

        fun find(clazz: Class<*>): ReflectClass {
            if (!savingClass.containsKey(clazz.name)) {
                savingClass[clazz.name] = ReflectClass(clazz)
            }
            return savingClass[clazz.name]!!
        }
    }
}

@Suppress("UNCHECKED_CAST")
inline fun <K : E, reified E> getGenericClassFromSuperClass(javaClass: Class<*>): Class<K> {
    val genericSuperclass = javaClass.genericSuperclass // 得到继承的父类填入的泛型（必须是具体的类型，不能是 T 这种东西）
    if (genericSuperclass is ParameterizedType) {
        val typeArguments = genericSuperclass.actualTypeArguments
        for (type in typeArguments) {
            if (type is Class<*> && E::class.java.isAssignableFrom(type)) {
                return type as Class<K>
            } else if (type is ParameterizedType) { // 泛型中有泛型时并不为 Class<*>
                val rawType = type.rawType // 这时 rawType 一定是 Class<*>
                if (rawType is Class<*> && E::class.java.isAssignableFrom(rawType)) {
                    return rawType as Class<K>
                }
            }
        }
    }
    throw RuntimeException("你父类的泛型为: $genericSuperclass, 其中不存在 ${E::class.java.simpleName}")
}