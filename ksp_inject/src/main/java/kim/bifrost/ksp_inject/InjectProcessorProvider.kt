package kim.bifrost.ksp_inject

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

/**
 * kim.bifrost.ksp_inject.InjectProcessorProvider
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/23 21:10
 */
class InjectProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return InjectProcessor(environment)
    }
}