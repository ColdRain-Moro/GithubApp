// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.2.1" apply false
    id("com.android.library") version "7.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.6.21" apply false
    id("org.jetbrains.kotlin.jvm") version "1.6.21" apply false
    id("com.google.devtools.ksp") version "1.6.21-1.0.5" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

//gradle.startParameter.taskNames
//    .find { it.contains(":app:assembleDebug") }
//    ?.apply {
//        tasks.whenTaskAdded {
//            if (name == "clean") {
//                dependsOn(this@apply)
//            }
//        }
////        subprojects.find { it.name.contains("ksp_inject") }
////            ?.tasks
////            ?.whenTaskAdded {
////                if (name.contains("clean")) {
////                    dependsOn(this@apply)
////                }
////            }
//    }

