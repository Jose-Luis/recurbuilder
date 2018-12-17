package info

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import java.io.File

class StatusCache(path: String) {

    private val cacheFile: File = File(path).absoluteFile
    private val statuses: JsonObject

    init {
        if (cacheFile.createNewFile()) {
            cacheFile.writeText("{}")
        }
        statuses = Parser().parse(path) as JsonObject
    }

    fun isChange(nodename: String, newStatus: String): Boolean = statuses.getOrDefault(nodename, "") != newStatus

    fun updateCache(nodename: String, newStatus: String) {
        statuses.put(nodename, newStatus)
    }

    fun persist() {
        cacheFile.writeText(Klaxon().toJsonString(statuses))
    }
}
