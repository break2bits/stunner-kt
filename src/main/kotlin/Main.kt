package com.hal.stunner

import com.hal.stunner.cli.CliArgumentParser

fun main(args: Array<String>) {
    println("Ran stunner")

    val parser = CliArgumentParser(args)
    val port = parser.getArg<Int>("p", "port")
    println("port: $port")
}