package org.kotlined.common

import org.kotlined.CCProcessor

interface ICCAppSettings {
    val processor: CCProcessor
    val corSettings: CCCorSettings
}