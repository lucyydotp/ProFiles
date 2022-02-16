import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

description = "profiles-bukkit"

plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0"
    java
}

dependencies {
    implementation(project(":profiles-squirtgun"))
    implementation(project(":profiles-api"))
    compileOnly("org.spigotmc:spigot-api:1.17-R0.1-SNAPSHOT")

    compileOnly("me.lucyy:pronouns-api:1.1")
    compileOnly("me.clip:placeholderapi:2.10.9")
    compileOnly("com.discordsrv:discordsrv:1.23.0")

    implementation("me.lucyy:squirtgun-api:2.0.0-pre5")
    implementation("me.lucyy:squirtgun-commands:2.0.0-pre5")
    implementation("me.lucyy:squirtgun-platform-bukkit:2.0.0-pre5")
}

tasks {
    val shadowTask = named<ShadowJar>("shadowJar") {
        archiveClassifier.set("")

        minimize {
            exclude(project(":profiles-api"))
        }

        dependencies {
            exclude(dependency("org.checkerframework:.*:.*"))
            exclude(dependency("org.jetbrains:annotations:.*"))
            exclude(dependency("com.google..*:.*:.*"))
        }
        // slf4j is transitive from hikari
        relocate("org.slf4j", "me.lucyy.profiles.deps.slf4j")
        relocate("me.lucyy.squirtgun", "me.lucyy.profiles.deps.squirtgun")
        relocate("net.kyori", "me.lucyy.profiles.deps.kyori")
        relocate("org.bstats", "me.lucyy.profiles.deps.bstats")
        relocate("com.zaxxer.hikari", "me.lucyy.profiles.deps.hikari")
    }

    named("build") {
        dependsOn(shadowTask)
    }

    withType<ProcessResources> {
        filter<org.apache.tools.ant.filters.ReplaceTokens>("tokens" to mapOf("version" to project.version.toString()))
    }
}