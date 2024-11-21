package com.hal.stunner.cli

class ArgumentNotFoundException(shortName: String, longName: String)
    : Exception("Required arg \"${shortName}\", \"${longName}\" not found")