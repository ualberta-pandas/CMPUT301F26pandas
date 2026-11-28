
package com.example.campsiteapp.model

data class Quad<out S, out L, out D, out GS> (

    val first:S,
    val second:L,
    val third:D,
    val fourth:GS

){
    override fun toString(): String = "($first, $second,$third,$fourth)"
}