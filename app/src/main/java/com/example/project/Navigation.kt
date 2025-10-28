package com.example.project

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Search : Screen("search")
    object Settings : Screen("settings")
}