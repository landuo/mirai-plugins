package io.github.landuo.rcon.plugin

import kotlinx.serialization.Serializable

/**
 * @author accidia
 */
@Serializable
class Server constructor() {

    var name: String? = ""
    var ip: String? = ""
    var port: Int? = 27015
    var password: String? = ""

    /**
     * 当通过控制台执行时为 0.
     */
    var onwer: Long = 0

    constructor(name: String, ip: String, port: Int, password: String, onwer: Long) : this() {
        this.name = name
        this.ip = ip
        this.password = password
        this.port = port
        this.onwer = onwer
    }
}