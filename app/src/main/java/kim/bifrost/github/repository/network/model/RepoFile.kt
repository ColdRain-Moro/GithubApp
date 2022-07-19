package kim.bifrost.github.repository.network.model


import com.google.gson.annotations.SerializedName

data class RepoFile(
    // 拿readme的时候才有这个字段
    @SerializedName("content")
    val content: String? = null,
    @SerializedName("download_url")
    val downloadUrl: String,
    // 拿readme的时候才有这个字段
    @SerializedName("encoding")
    val encoding: String? = null,
    @SerializedName("git_url")
    val gitUrl: String,
    @SerializedName("html_url")
    val htmlUrl: String,
    @SerializedName("_links")
    val links: Links,
    @SerializedName("name")
    val name: String,
    @SerializedName("path")
    val path: String,
    @SerializedName("sha")
    val sha: String,
    @SerializedName("size")
    val size: Int,
    @SerializedName("type")
    val type: Type,
    @SerializedName("url")
    val url: String
) {
    enum class Type {
        @SerializedName("file")
        FILE,
        @SerializedName("dir")
        DIRECTORY
    }

    data class Links(
        @SerializedName("git")
        val git: String,
        @SerializedName("html")
        val html: String,
        @SerializedName("self")
        val self: String
    )
}