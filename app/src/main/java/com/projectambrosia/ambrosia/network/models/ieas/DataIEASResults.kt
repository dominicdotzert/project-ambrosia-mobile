package com.projectambrosia.ambrosia.network.models.ieas

data class DataIEASResults(
    val ieasResults: IntArray,
    val timestamp: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataIEASResults

        if (!ieasResults.contentEquals(other.ieasResults)) return false
        if (timestamp != other.timestamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ieasResults.contentHashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }
}