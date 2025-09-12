package org.kotlined

import org.kotlined.common.CCCorSettings
import org.kotlined.common.ICCAppSettings

data class CCAppSettings(
    val appUrls: List<String> = emptyList(),
    override val processor: CCProcessor,
    override val corSettings: CCCorSettings,
) : ICCAppSettings
