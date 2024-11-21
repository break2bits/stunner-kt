package com.hal.stunner.cli

/*
need to support
- flag arguments
- int arguments
 */
class CliArgumentParser(val arguments: Array<String>) {
    val argumentMap = untypedParse(arguments)

    companion object {
        private fun untypedParse(arguments: Array<String>): MutableMap<String, String> {
            if (arguments.size == 0) {
                return mutableMapOf()
            }
            val flatMappedArguments = arguments.flatMap { it.split("=") }
            val argMap = mutableMapOf<String, String>()
            var name: String? = null
            flatMappedArguments.forEach {
                if (it.startsWith("-")) { // we found a key
                    if (name != null) { // set the last key as a flag argument
                        argMap[name!!] = "true"
                    }
                    name = getNameWithoutDashes(it)
                } else if (name != null) {
                    argMap[name!!] = it
                    name = null
                }
            }

            return argMap
        }

        private fun getNameWithoutDashes(name: String): String {
            return name
                .removePrefix("-")
                .removePrefix("-")
        }
    }

    inline fun <reified T> getArg(shortName: String, longName: String): T {
        if (T::class == Int::class) {
            val unparsedValue = readRawValue(shortName, longName)
            return Integer.parseInt(unparsedValue) as T
        }

        if (T::class == Boolean::class) {
            val unparsedValue = readRawValue(shortName, longName)
            val unparsedToLower = unparsedValue.lowercase()
            return (unparsedToLower == "true" || unparsedToLower == "t") as T
        }

        if (T::class == String::class) {
            return readRawValue(shortName, longName) as T
        }
        throw IllegalArgumentException("Unsupported argument of type ${T::class.simpleName}")
    }

    inline fun <reified T> getArg(shortName: String, longName: String, default: T): T {
        if (T::class == Int::class) {
            val unparsedValue = readRawValueOptional(shortName, longName)
            if (unparsedValue == null) {
                return default
            }
            return Integer.parseInt(unparsedValue) as T
        }

        if (T::class == Boolean::class) {
            val unparsedValue = readRawValueOptional(shortName, longName)
            if (unparsedValue == null) {
                return default
            }
            val unparsedToLower = unparsedValue.lowercase()
            return (unparsedToLower == "true" || unparsedToLower == "t") as T
        }

        if (T::class == String::class) {
            val rawValue = readRawValueOptional(shortName, longName)
            if (rawValue == null) {
                return default
            }
            return rawValue as T
        }

        throw IllegalArgumentException("Unsupported argument of type ${T::class.simpleName}")
    }

    fun readRawValue(shortName: String, longName: String): String {
        return argumentMap[shortName]
            ?: argumentMap[longName]
            ?: throw CliArgumentNotFoundException(shortName, longName)
    }

    fun readRawValueOptional(shortName: String, longName: String): String? {
        return argumentMap[shortName] ?: argumentMap[longName]
    }
}