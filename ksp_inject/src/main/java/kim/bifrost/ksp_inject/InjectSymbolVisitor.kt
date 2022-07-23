package kim.bifrost.ksp_inject

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import kim.bifrost.annotations.AutoWired

/**
 * kim.bifrost.ksp_inject.InjectSymbolVisitor
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/23 20:22
 */
class InjectSymbolVisitor(
    private val env: SymbolProcessorEnvironment,
    private val resolver: Resolver
) : KSVisitorVoid() {

    private lateinit var classDeclaration: KSClassDeclaration
    private val properties = mutableListOf<KSPropertyDeclaration>()

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        env.logger.info("executed")
        this.classDeclaration = classDeclaration
        resolver.getSymbolsWithAnnotation(AutoWired::class.qualifiedName!!)
            .filterIsInstance<KSPropertyDeclaration>()
            .filter { it in classDeclaration.getAllProperties() }
            .forEach { it.accept(this, Unit) }

        val fileSpec = FileSpec.builder(
            packageName = classDeclaration.packageName.asString(),
            fileName = classDeclaration.simpleName.asString() + "_Inject"
        ).apply {
            addFunction(
                FunSpec.builder("inject")
                    .receiver(ClassName.bestGuess(classDeclaration.qualifiedName!!.asString()))
                    .returns(Unit::class)
                    .apply {
                        addImport("kim.bifrost.github.utils", "getValue")
                        if (classDeclaration.qualifiedName!!.asString().endsWith("Activity")) {
                            properties.forEach { property ->
                                val annotation = property.annotations
                                    .find {
                                        it.annotationType.resolve().declaration.qualifiedName!!.asString() == AutoWired::class.qualifiedName!!
                                    } ?: error("annotation not found.")
                                val id = (annotation.arguments.first().value as String).ifEmpty { property.simpleName.asString() }
                                val type = property.type.resolve()
                                var typeStr = type.declaration.qualifiedName!!.asString()
                                if (type.isMarkedNullable) {
                                    typeStr += "?"
                                }
                                addStatement("this.${property.simpleName.asString()} = intent.getValue<${typeStr}>(\"$id\")")
                            }
                        } else {
                            // fragment
                            properties.forEach { property ->
                                val annotation = property.annotations
                                    .find {
                                        it.annotationType.resolve().declaration.qualifiedName!!.asString() == AutoWired::class.qualifiedName!!
                                    } ?: error("annotation not found.")
                                val id = (annotation.arguments.first().value as String).ifEmpty { property.simpleName.asString() }
                                val type = property.type.resolve()
                                var typeStr = type.declaration.qualifiedName!!.asString()
                                if (type.isMarkedNullable) {
                                    typeStr += "?"
                                }
                                addStatement("this.${property.simpleName.asString()} = requireArguments().getValue<${typeStr}>(\"$id\")")
                            }
                        }
                    }
                    .build()
            )
        }.build()
        env.codeGenerator.createNewFile(
            dependencies = Dependencies(aggregating = false),
            packageName = classDeclaration.packageName.asString(),
            fileName = classDeclaration.simpleName.asString() + "_Inject"
        ).use { out ->
            out.writer()
                .use {
                    fileSpec.writeTo(it)
                }
        }
    }

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
        properties.add(property)
    }
}