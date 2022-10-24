plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven {url = uri("https://jitpack.io")} //was maven {url "https://jitpack.io"}
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    //implementation("info.picocli:picocli:4.6.3")

    //compileOnly("info.picocli:picocli:4.6.3")
    //annotationProcessor("info.picocli:picocli-codegen:4.6.3")

    implementation("commons-cli:commons-cli:1.5.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}