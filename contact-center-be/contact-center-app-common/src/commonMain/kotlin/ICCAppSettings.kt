package org.kotlined.cc.app.common

import org.kotlined.cc.biz.CCProcessor
import org.kotlined.common.CCCorSettings

interface ICCAppSettings {
    val processor: CCProcessor
    val corSettings: CCCorSettings
}