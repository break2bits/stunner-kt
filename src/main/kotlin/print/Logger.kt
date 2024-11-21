package com.hal.stunner.print

import kotlin.io.println as kprintln

class Logger {
    fun println(string: String?){
        kprintln(string)
    }

    fun printErrln(string: String?) {
        System.err.println(string)
    }
}