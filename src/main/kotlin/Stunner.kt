package com.hal.stunner

import com.hal.stunner.cli.CliArgumentParser
import com.hal.stunner.config.StunServerConfiguration
import com.hal.stunner.handler.FingerprintAttributeValueValidator
import com.hal.stunner.handler.FingerprintCalculator
import com.hal.stunner.handler.StunHandler
import com.hal.stunner.handler.StunMessageBuilderFactory
import com.hal.stunner.message.StunMessageParser
import com.hal.stunner.message.StunMessageSerializer
import com.hal.stunner.message.attribute.StunAttributeListParser
import com.hal.stunner.message.attribute.StunAttributeParser
import com.hal.stunner.message.attribute.value.FingerprintStunAttributeValueParser
import com.hal.stunner.message.attribute.value.MappedAddressStunAttributeValueParser
import com.hal.stunner.message.attribute.value.StunAttributeValueParserFactory
import com.hal.stunner.message.header.StunHeaderParser
import com.hal.stunner.print.Logger
import kotlin.system.exitProcess

class Stunner(private val args: Array<String>) {
    private val logger = Logger()

    fun start() {
        val config = readConfig()
        logger.println("Starting stun server on port ${config.port} using protocol ${config.protocol}")

        // Inject all dependencies the old-fashioned way
        val server = buildServer(config)
        server.listen()
    }

    private fun buildServer(config: StunServerConfiguration): StunServer {
        val fingerprintCalculator = FingerprintCalculator()
        return StunServer(
            config = config,
            handler = StunHandler(
                fingerprintAttributeValueValidator = FingerprintAttributeValueValidator(
                    fingerprintCalculator = fingerprintCalculator
                ),
                stunMessageBuilderFactory = StunMessageBuilderFactory(
                    stunMessageSerializer = StunMessageSerializer(),
                    fingerprintCalculator = fingerprintCalculator
                )
            ),
            parser = StunMessageParser(
                headerParser = StunHeaderParser(),
                attributeListParser = StunAttributeListParser(
                    attributeParser = StunAttributeParser(
                        attributeValueParserFactory = StunAttributeValueParserFactory(
                            mappedAddressStunAttributeValueParser = MappedAddressStunAttributeValueParser(),
                            fingerprintStunAttributeValueParser = FingerprintStunAttributeValueParser()
                        ),
                    ),
                ),
            ),
            serializer = StunMessageSerializer()
        )
    }

    private fun readConfig(): StunServerConfiguration {
        try {
            val parser = CliArgumentParser(args)
            val port = parser.getArg<Int>("p", "port", 3478)
            val protocol = parser.getArg<String>("s", "scheme", "udp")
            return StunServerConfiguration(
                port = port,
                protocol = protocol,
            )
        } catch (e: Exception) {
            logger.printErrln(e.message)
            exitProcess(1)
        }
    }
}
