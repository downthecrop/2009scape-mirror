package org.rs09.client.console

data class AutocompletionHints(
        val base: String,
        val completions: List<String>,
        val totalSize: Int
)