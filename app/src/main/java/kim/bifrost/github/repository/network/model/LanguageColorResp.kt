package kim.bifrost.github.repository.network.model

/**
 * kim.bifrost.github.repository.network.model.LanguageColorResp
 * GitHubApp
 *
 * @author 寒雨
 * @since 2022/7/16 21:14
 */
data class LanguageColorResp(
    val status: Int,
    val message: String,
    val data: LanguageColor?
)

data class LanguageColor(
    val lang: String,
    val hex: String
)