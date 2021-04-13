@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package uk.co.jampez.torque2ha.utils

import java.io.Serializable

class PID(pid: String?) : Serializable {
    var pid: String? = null
    var fullName: String? = null
    var varType: Class<*>? = null
    var shortName: String? = null
    var min = 0f
    var max = 0f
    var unit: String? = null
    var scale = 0f
    private var userPidValue: Float? = null
    var equation: String? = null
    var mode = 0
    var isUserPid = true
    var header = ""
    private var userPidValueUpdateTime: Long = 0
    var valueLastUpdatedTime: Long = 0
        private set
    private var tries = 0
    var isEditable = true

    override fun equals(other: Any?): Boolean {
        return fullName == (other as PID?)!!.fullName
    }

    override fun hashCode(): Int {
        return ("$pid|$fullName|$varType|$shortName|$min|$max|$unit|$scale|$equation|$header").hashCode()
    }

    override fun toString(): String {
        return fullName!!
    }

    fun getUserPidValue(triggersRequest: Boolean): Float? {
        if (triggersRequest) {
            userPidValueUpdateTime = System.currentTimeMillis()
        }
        return userPidValue
    }

    val isUserPidRequestRequired: Boolean
        get() = userPidValueUpdateTime + 3000 > System.currentTimeMillis()
    val isUserPidUpdatedRecently: Boolean
        get() = valueLastUpdatedTime + 5000 > System.currentTimeMillis()

    fun setUserPidValue(userPidValue: Float?) {
        this.userPidValue = userPidValue
        valueLastUpdatedTime = System.currentTimeMillis()
    }

    fun incrementTries() {}
    fun reset() {
        tries = 0
        userPidValue = null
        valueLastUpdatedTime = 0
    }

    val isUserPidSupported: Boolean
        get() = if (tries < 3) {
            true
        } else getUserPidValue(false) != null

    companion object {
        private const val GROUP_CUSTOM = 0x9000
    }

    init {
        this.pid = pid
    }
}
