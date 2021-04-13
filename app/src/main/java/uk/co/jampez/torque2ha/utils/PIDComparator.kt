package uk.co.jampez.torque2ha.utils

import java.util.*

class PIDComparator : Comparator<PID> {
    override fun compare(pid1: PID, pid2: PID): Int {

        var n1: String? = ""
        var n2: String? = ""
        if (pid1.fullName != null) n1 = pid1.fullName
        if (pid2.fullName != null) n2 = pid2.fullName
        return n1!!.compareTo(n2!!, ignoreCase = true)
    }
}
