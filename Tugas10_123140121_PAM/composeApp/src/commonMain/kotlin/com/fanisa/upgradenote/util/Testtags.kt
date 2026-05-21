package com.fanisa.upgradenote.util

/**
 * Konstanta Test Tags untuk UI Testing dengan Compose Test.
 * Digunakan di composable dengan Modifier.testTag() dan
 * di test dengan onNodeWithTag().
 */
object TestTags {
    // Notes List Screen
    const val NOTES_LIST        = "notes_list"
    const val NOTE_ITEM         = "note_item"
    const val EMPTY_STATE       = "empty_state"
    const val SEARCH_INPUT      = "search_input"
    const val FAB_ADD_NOTE      = "fab_add_note"

    // Add/Edit Screen
    const val TITLE_INPUT       = "title_input"
    const val CONTENT_INPUT     = "content_input"
    const val SAVE_BUTTON       = "save_button"
    const val DELETE_BUTTON     = "delete_button"

    // Nutrition Screen
    const val FOOD_INPUT        = "food_input"
    const val ANALYZE_BUTTON    = "analyze_button"
    const val RESULT_CARD       = "result_card"
    const val LOADING_INDICATOR = "loading_indicator"
    const val ERROR_CARD        = "error_card"
}