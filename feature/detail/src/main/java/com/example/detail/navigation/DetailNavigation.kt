package com.example.detail.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.example.detail.DetailScreen
import com.example.model.MovieWithGenreItem
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

@Serializable
data class DetailRoute(private val movieWithGenreItem: MovieWithGenreItem)

fun NavController.navigateToDetail(navOptions: NavOptions? = null, movieWithGenreItem: MovieWithGenreItem) = navigate(route = DetailRoute(movieWithGenreItem), navOptions)

fun NavGraphBuilder.detailScreen() {
    composable<DetailRoute>(
        typeMap = mapOf(typeOf<MovieWithGenreItem>() to MovieWithGenreItemNavType),
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "https://example.com/detail/{movieWithGenreItem}"
            },
        ),
    ) {
        DetailScreen()
    }
}

val MovieWithGenreItemNavType = object : NavType<MovieWithGenreItem>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): MovieWithGenreItem? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): MovieWithGenreItem {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun serializeAsValue(value: MovieWithGenreItem): String {
        return Uri.encode(Json.encodeToString(value))
    }

    override fun put(bundle: Bundle, key: String, value: MovieWithGenreItem) {
        bundle.putString(key, Json.encodeToString(value))
    }
}
