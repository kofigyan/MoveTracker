package com.kofigyan.movetracker.util

import com.kofigyan.movetracker.model.Location

object StaticMapUrlBuilder {

    fun buildStaticMapUrl(
        width: Int = DEFAULT_WIDTH_STATIC_MAP,
        height: Int = DEFAULT_HEIGHT_STATIC_MAP,
        locations: List<Location>,
        pathWeight: Int = DEFAULT_PATH_WEIGHT_STATIC_MAP,
        pathColor: String = DEFAULT_PATH_COLOR
    ): String {

        val url = StringBuilder()
        url.append(BASE_STATIC_MAP_URL)
        url.append(String.format("&size=%dx%d", width, height))

        url.append(String.format("&path="))
        url.append(String.format("color:%s", pathColor))
        url.append(String.format("|weight:%d", pathWeight))

        // TODO  Update to enc: prefix using PolyUtil.encode of List<LatLng> path
        for (location in locations) {
            url.append(String.format("|%8.5f,%8.5f", location.latitude, location.longitude))
        }

        // Add your Map API key to URL
        url.append(String.format("&key=%s", API_KEY))
        return url.toString()
    }

}