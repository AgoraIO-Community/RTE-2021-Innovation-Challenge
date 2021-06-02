package io.agora.education.api.statistics

enum class AgoraError(var value: Int) {
    // No error.
    NONE(0),

    // The operation failed due to an internal error.
    INTERNAL_ERROR(-1),

    // An operation is valid, but currently unsupported.
    UNSUPPORTED_OPERATION(1),

    // A supplied parameter is valid, but currently unsupported.
    UNSUPPORTED_PARAMETER(2),

    // General error indicating that a supplied parameter is invalid.
    INVALID_PARAMETER(3),

    // Slightly more specific than INVALID_PARAMETER; a parameter's value was
    // outside the allowed range.
    INVALID_RANGE(4),

    // Slightly more specific than INVALID_PARAMETER; an error occurred while
    // parsing string input.
    SYNTAX_ERROR(5),

    // The object does not support this operation in its current state.
    INVALID_STATE(6),

    // An attempt was made to modify the object in an invalid way.
    INVALID_MODIFICATION(7),

    // An error occurred within an underlying network protocol.
    NETWORK_ERROR(8),

    // Some resource has been exhausted; file handles, hardware resources, ports,
    // etc.
    RESOURCE_EXHAUSTED(9),

    //
    SEQUENCE_NOT_EXISTS(20404101),

    ROOM_ALREADY_EXISTS(20409100),
}