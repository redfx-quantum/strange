plugins {
    application
    id("org.javamodularity.moduleplugin") version "1.2.1"
    id("gluonhq.publishing")
}

group = "com.gluonhq"
version = "0.0.11-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        setUrl("https://nexus.gluonhq.com/nexus/content/repositories/releases/")
    }
}

application {
    mainClassName = "com.gluonhq.strange.demo.Demo"
}


tasks {
    test {
        useJUnitPlatform()
        testLogging {
            //events 'PASSED', 'FAILED', 'SKIPPED'
        }
    }

    javadoc {
        isFailOnError = false
    }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}

java {
    withSourcesJar()
    withJavadocJar()
}