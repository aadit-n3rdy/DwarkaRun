plugins {
	java
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("com.google.guava:guava:32.1.1-jre")

    implementation(project(":engine"))
}

java {
//    toolchain {
//        languageVersion.set(JavaLanguageVersion.of(21))
//    }
}

application {
    mainClass.set("com.dwarakarun.dwaraka.Game")
    if (System.getProperty("os.name").lowercase().contains("mac")) {
        applicationDefaultJvmArgs = listOf("-XstartOnFirstThread")
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
