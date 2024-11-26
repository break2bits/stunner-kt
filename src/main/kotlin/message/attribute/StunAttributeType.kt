package com.hal.stunner.message.attribute

enum class StunAttributeType(val value: Int) {
    RESERVED(0x0000),
    MAPPED_ADDRESS(0x0001),
    RESERVED_WAS_RESPONSE_ADDRESS(0x0002),//: Reserved; was RESPONSE-ADDRESS prior to [RFC5389]
    RESERVED_WAS_CHANGE_REQUEST(0x0003),// : Reserved; was CHANGE-REQUEST prior to [RFC5389]
    RESERVED_WAS_SOURCE_ADDRESS(0x0004), // : Reserved; was SOURCE-ADDRESS prior to [RFC5389]
    RESERVED_WAS_CHANGE_ADDRESS(0x0005), // : Reserved; was CHANGED-ADDRESS prior to [RFC5389]
    USERNAME(0x0006),
    RESERVED_WAS_PASSWORD(0x0007), // : Reserved; was PASSWORD prior to [RFC5389]
    MESSAGE_INTEGRITY(0x0008), // : MESSAGE-INTEGRITY
    ERROR_CODE(0x0009), // : ERROR-CODE
    UNKNOWN_ATTRIBUTES(0x000A), // : UNKNOWN-ATTRIBUTES
    RESERVED_WAS_REFLECTED_FROM(0x000B), // : Reserved; was REFLECTED-FROM prior to [RFC5389]
    REALM(0x0014), // : REALM
    NONCE(0x0015), // : NONCE
    XOR_MAPPED_ADDRESS(0x0020), // : XOR-MAPPED-ADDRESS

    // Comprehension-optional range (0x8000-0xFFFF)
    SOFTWARE(0x8022), // : SOFTWARE
    ALTERNATE_SERVER(0x8023), // : ALTERNATE-SERVER
    FINGERPRINT(0x8028); // : FINGERPRINT
    companion object {
        fun fromValue(value: Int): StunAttributeType? {
            return entries.find { it.value == value }
        }
    }
}