plugins {
    val kotlinVersion = "1.7.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.13.3"

}

dependencies {
    implementation("io.github.landuo:l4d2-query:0.0.6")
    implementation("com.github.oshi:oshi-core:5.6.1")
    implementation("cn.hutool:hutool-all:5.8.11")
}

group = "io.github.landuo"
version = "0.1.0"

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}
