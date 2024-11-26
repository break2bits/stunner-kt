package com.hal.stunner

import com.hal.stunner.cli.CliArgumentParser
import com.hal.stunner.config.StunServerConfiguration
import com.hal.stunner.message.StunMessageParser
import com.hal.stunner.message.attribute.StunAttributeListParser
import com.hal.stunner.message.attribute.StunAttributeParser
import com.hal.stunner.message.attribute.value.MappedAddressStunAttributeValueParser
import com.hal.stunner.message.attribute.value.StunAttributeValueParserFactory
import com.hal.stunner.message.header.StunHeaderParser
import com.hal.stunner.print.Logger
import kotlin.system.exitProcess

class Stunner(private val args: Array<String>) {

    private val logger = Logger()

    fun start() {
        val config = readConfig()
        logger.println("Starting stun servier on port ${config.port} using protocol ${config.protocol}")

        // Inject all dependencies
        val server = StunServer(
            config = config,
            handler = StunHandler(),
            parser = StunMessageParser(
                headerParser = StunHeaderParser(),
                attributeListParser = StunAttributeListParser(
                    attributeParser = StunAttributeParser(
                        attributeValueParserFactory = StunAttributeValueParserFactory(
                            mappedAddressStunAttributeValueParser = MappedAddressStunAttributeValueParser()
                        )
                    )
                )
            )
        )
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