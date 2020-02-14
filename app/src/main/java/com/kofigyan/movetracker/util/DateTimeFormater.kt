package com.kofigyan.movetracker.util

import android.content.Context
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import android.text.format.DateFormat as AndroidDateFormat

fun applyDateTimeFormatter(context: Context): DateTimeFormatter {
    val dateF = AndroidDateFormat.getMediumDateFormat(context) as SimpleDateFormat
    val timeF = AndroidDateFormat.getTimeFormat(context) as SimpleDateFormat

    @Suppress("DEPRECATION")
    return DateTimeFormatter.ofPattern("${dateF.toPattern()} ${timeF.toPattern()}")
        .withLocale(context.resources.configuration.locale)
        .withZone(ZoneId.systemDefault()) }