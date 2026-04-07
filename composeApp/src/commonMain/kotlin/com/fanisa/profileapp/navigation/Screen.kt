package com.fanisa.profileapp.navigation

sealed class Screen(val route: String) {
    object Profile : Screen("profile_route")
    object EditProfile : Screen("edit_route")
}