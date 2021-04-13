package com.example.lumos.utils

//utility class to convert timestamp to date
class DateTimeConverter(private val timestamp:String) {
    //"2020-04-16T17:45:00+05:30"
    private var separator:Int=0
    init{
        separator=timestamp.indexOf('T')
    }
    fun getYear()=timestamp.substring(0,4)

    fun getDate()=timestamp.substring(0,separator)

    fun getMonth()=when(timestamp.substring(timestamp.indexOf('-')+1,timestamp.lastIndexOf('-')).toInt()){
        1->"Jan"
        2->"Feb"
        3->"Mar"
        4->"Apr"
        5->"May"
        6->"Jun"
        7->"Jul"
        8->"Aug"
        9->"Sep"
        10->"Oct"
        11->"Nov"
        else->"Dec"
    }
    fun getDay()=timestamp.substring(timestamp.lastIndexOf('-')+1,separator)

    fun getTime()=timestamp.substring(separator+1,timestamp.indexOf('+'))
}