package com.uvarov.stylish.core.model

enum class ProductImportance {
    NORMAL,
    HIGH;

    companion object {
        fun fromString(value: String?): ProductImportance = when (value?.lowercase()) {
            "high", "featured", "important" -> HIGH
            else -> NORMAL
        }
    }
}
