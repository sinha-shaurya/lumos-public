package com.example.lumos.network.dataclasses.practice

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class AnsweredQuestion(
    @Json(name="question") val question: Question,
    @Json(name="answer") val answerSubmit:String,
    @Json(name="points") val points:Int?
):Parcelable
