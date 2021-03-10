package com.example.lumos.network.dataclasses.practice

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class MentorDetails(
    @Json(name = "name") val name: String,
    @Json(name = "company") val company: String,
    @Json(name = "year") val year: Int,
    @Json(name = "brief_description") val mentorDescription: String
):Parcelable
