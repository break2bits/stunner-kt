package com.hal.stunner.cli

class CliArgumentNotFoundException(shortName: String, longName: String)
    : Exception("Required arg \"-${shortName}\", \"--${longName}\" not found")