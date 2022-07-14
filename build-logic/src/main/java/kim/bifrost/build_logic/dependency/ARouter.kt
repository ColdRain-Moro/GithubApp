package kim.bifrost.build_logic.dependency

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/5/27 15:45
 */
@Suppress("MemberVisibilityCanBePrivate", "ObjectPropertyName", "SpellCheckingInspection")
object ARouter {
  // https://github.com/alibaba/ARouter
  const val arouter_version = "1.5.2"
  
  const val `arouter-api` = "com.alibaba:arouter-api:$arouter_version"
  const val `arouter-compiler` = "com.alibaba:arouter-compiler:$arouter_version"
}

fun Project.dependARouter() {
  dependencies {
    "implementation"(ARouter.`arouter-api`)
    "kapt"(ARouter.`arouter-compiler`)
  }
}