package com.ide.shota.viewintentcatalog

import java.io.Serializable

class ViewIntent(
        val title: String,
        val description: String,
        val hasAppName: Boolean,
        val hasZoomLevel: Boolean
) : Serializable
