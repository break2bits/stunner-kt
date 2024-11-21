package com.hal.stunner

import com.hal.stunner.cli.CliArgumentParser
import com.hal.stunner.config.StunServerConfiguration
import com.hal.stunner.print.Logger
import kotlin.system.exitProcess

class Stunner(private val args: Array<String>) {

    private val logger = Logger()

    fun start() {
        val config = readConfig()
        logger.println("Starting stun servier on port ${config.port} using protocol ${config.protocol}")
        val server = StunServer(config)
        server.listen()
    }

    private fun readConfig(): StunServerConfiguration {
        try {
            val parser = CliArgumentParser(args)
            val port = parser.getArg<Int>("p", "port", 3478)
            val protocol = parser.getArg<String>("s", "scheme", "udp")
            return StunServerConfiguration(
                port = port,
                protocol = protocol
            )
        } catch (e: Exception) {
            logger.printErrln(e.message)
            exitProcess(1)
        }
    }
}