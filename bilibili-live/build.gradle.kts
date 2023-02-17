plugins {
    val kotlinVersion = "1.7.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.14.0"
}

group = "top.accidia.bilibili-live"
version = "0.1.0"

dependencies {
    implementation("cn.hutool:hutool-all:5.8.11")
}

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}
