package com.sample.roomandpaginlibrary.net

import com.google.gson.annotations.SerializedName

data class LingvoSearchResult(@SerializedName("Items") val items: List<LingvoItem>,
                              @SerializedName("HasNextPage") val hasNextPage: Boolean,
                              @SerializedName("TotalCount") val totalCount: Int,
                              var page: Int)

data class LingvoItem(@SerializedName("Title") val title: String,
                      @SerializedName("Dictionary") val dictionary: String,
                      @SerializedName("ArticleId") val articleId: String,
                      @SerializedName("Body") val body: List<LingvoBody>) {
    val id: String
        get() = articleId + dictionary
}

data class LingvoBody(@SerializedName("Text") val text: Int,
                      @SerializedName("Node") val node: String,
                      @SerializedName("Type") val type: Int)