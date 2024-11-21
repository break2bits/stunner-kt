package com.hal.stunner

import com.hal.stunner.cli.CliArgumentParser
import com.hal.stunner.config.StunServerConfiguration

class Stunner(private val args: Array<String>) {

    fun start() {
        val parser = CliArgumentParser(args)
        val port = parser.getArg<Int>("p", "port", 3478)
        val protocol = parser.getArg<String>("s", "scheme", "udp")
        val config = StunServerConfiguration(
            port = port,
            protocol = protocol
        )
        println("Starting stun servier on port ${config.port} using protocol ${protocol}")
        val server = StunServer(config)
        server.listen()
    }
}