package org.kotlined.cc.app.ktor

import org.kotlined.cc.biz.CCProcessor
import org.kotlined.cc.app.common.ICCAppSettings
import org.kotlined.common.CCCorSettings

data class CCAppSettings(
    val appUrls: List<String> = emptyList(),
    override val corSettings: CCCorSettings = CCCorSettings(),
    override val processor: CCProcessor = CCProcessor(corSettings),
): ICCAppSettings

