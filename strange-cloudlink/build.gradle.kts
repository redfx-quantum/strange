plugins {
    war
    application
}

repositories {
    mavenCentral()
}

val payara by configurations.creating

dependencies {
    providedCompile("javax:javaee-api:7.0")
    providedCompile("com.fasterxml.jackson.jaxrs:jackson-jaxrs-json-provider:2.9.5")

    payara("fish.payara.extras:payara-micro:4.1.2.181")
}

application {
    mainClassName = "fish.payara.micro.PayaraMicro"
}

val war = tasks.named<War>("war")

tasks.withType<JavaExec> {
    dependsOn(war)
    classpath(payara)
    setArgs(listOf("--deploy", war.get().archiveFile.get().asFile, "--nocluster"))
}
