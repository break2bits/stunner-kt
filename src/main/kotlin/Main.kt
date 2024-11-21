package com.hal.stunner

import com.hal.stunner.cli.CliArgumentParser

fun main(args: Array<String>) {
    val parser = CliArgumentParser(args)
    val port = parser.getArg<Int>("p", "port", 3478)
    val protocol = parser.getArg<String>("s", "scheme", "udp")
    println("port: $port")
    println("protocol: $protocol")
}