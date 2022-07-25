package kim.bifrost.ksp_inject

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec

/**
 * kim.bifrost.ksp_inject.InjectProcessor
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/23 19:58
 */
class InjectProcessor(
    private val environment: SymbolProcessorEnvironment
) : SymbolProcessor {

    private lateinit var resolver: Resolver
    var executed = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (executed) {
            environment.logger.info("executed")
            return emptyList()
        }
        environment.logger.info("execute!")
        this.resolver = resolver
        val fileSpecBuilder = FileSpec.builder(
            packageName = "kim.bifrost.inject",
            fileName = "Injector"
        ).addImport("kim.bifrost.github.utils", "getValue")
        val activityFunSpecBuilder = FunSpec.builder("injectAll")
            .addKdoc("在基类使用，为所有子类进行依赖注入")
            .receiver(ClassName.bestGuess("androidx.activity.ComponentActivity"))
            .returns(Unit::class.java)
            .addStatement("when (this) {")
        val fragmentFunSpecBuilder = FunSpec.builder("injectAll")
            .addKdoc("在基类使用，为所有子类进行依赖注入")
            .receiver(ClassName.bestGuess("androidx.fragment.app.Fragment"))
            .returns(Unit::class.java)
            .addStatement("when (this) {")
        resolver.getNewFiles()
            .flatMap { it.declarations }
            .filter { it is KSClassDeclaration }
            // 类型解析的代价很高，最好能有有效的方法过滤
            .filter { it.qualifiedName?.asString()?.endsWith("Activity") == true || it.qualifiedName?.asString()?.endsWith("Fragment") == true }
            .toList()
            .onEach {
                it.accept(InjectSymbolVisitor(environment, resolver), Unit)
            }.apply {
                filter { it.qualifiedName?.asString()?.endsWith("Activity") == true }.onEach {
                    fileSpecBuilder.addImport(it.packageName.asString(), "inject")
                    activityFunSpecBuilder.addStatement("is ${it.qualifiedName!!.asString()} -> inject()")
                }
                filter { it.qualifiedName?.asString()?.endsWith("Fragment") == true }.onEach {
                    fileSpecBuilder.addImport(it.packageName.asString(), "inject")
                    fragmentFunSpecBuilder.addStatement("is ${it.qualifiedName!!.asString()} -> inject()")
                }
            }
        activityFunSpecBuilder.addStatement("}")
        fragmentFunSpecBuilder.addStatement("}")
        fileSpecBuilder.addFunction(activityFunSpecBuilder.build())
        fileSpecBuilder.addFunction(fragmentFunSpecBuilder.build())
        environment.codeGenerator.createNewFile(
            dependencies = Dependencies(aggregating = false),
            packageName = "kim.bifrost.inject",
            fileName = "Injector"
        ).use { out ->
            out.writer()
                .use {
                    fileSpecBuilder.build().writeTo(it)
                }
        }
        executed = true
        return emptyList()
    }
}