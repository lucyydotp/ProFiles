plugins {
	java
}

group = "me.lucyy"
version = "1.1.0-SNAPSHOT"

subprojects {
	group = rootProject.group
	version = rootProject.version

	apply<JavaPlugin>()

	java {
		sourceCompatibility = JavaVersion.VERSION_11
	}

	repositories {
		maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
		maven("https://oss.sonatype.org/content/groups/public/")
		maven("https://repo.lucyy.me/repository/public")
		maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
		maven("https://nexus.scarsz.me/content/groups/public/")
		maven("https://m2.dv8tion.net/releases")
		mavenCentral()
	}

	tasks.withType<JavaCompile> {
		options.encoding = "UTF-8"
	}
}