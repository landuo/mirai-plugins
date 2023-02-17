package io.github.landuo

import kotlinx.serialization.Serializable

/**
 * @author accidia
 */
@Serializable
open class SpConfig constructor() {
    /**
     * 外部图片存放目录(绝对路径), 如: /home/accidia/pic
     */
    var picDir: String? = "."

    /**
     * 监控信息发送给谁
     */
    var sendTo: Long? = 0L

    constructor(picDir: String, sendTo: Long) : this() {
        this.picDir = picDir
        this.sendTo = sendTo
    }

}