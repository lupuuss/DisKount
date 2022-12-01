package com.github.lupuuss.diskount

import com.github.lupuuss.diskount.domain.Deal

data class Entities(
    val deals: Map<Deal.Id, Deal> = emptyMap()
)