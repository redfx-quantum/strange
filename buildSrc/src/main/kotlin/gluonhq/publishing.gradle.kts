package gluonhq

plugins {
    `maven-publish`
    signing
}

publishing {
    repositories {
        maven {
            setUrl(getStringProperty("repositoryUrl") { "https://oss.sonatype.org/content/repositories/snapshots/" })
            credentials {
                username = getStringPropertyDefaultAsProperty("repositoryUsername", "sonatypeUsername")
                password = getStringPropertyDefaultAsProperty("repositoryPassword", "sonatypePassword")
            }
        }
        maven {
            setName("buildLocal")
            setUrl("${buildDir}/repo")
        }
    }

    publications {
        create<MavenPublication>("main") {
            from(components["java"])

            pom {
                name.set("Strange")
                description.set("Java API for Quantum Computing")
                url.set("https://github.com/gluonhq/strange")
                scm {
                    connection.set("scm:git:https://github.com/gluonhq/strange.git")
                    developerConnection.set("scm:git:ssh://git@github.com:gluonhq/strange.git")
                    url.set("https://github.com/gluonhq/strange")
                }
                developers {
                    developer {
                        name.set("Johan Vos")
                        email.set("johan.vos@gluonhq.com")
                        roles.set(listOf("author", "developer"))
                    }
                }
                licenses {
                    license {
                        name.set("The 3-Clause BSD License")
                        url.set("http://www.opensource.org/licenses/bsd-license.php")
                        distribution.set("repo")
                    }
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["main"])
    setRequired { !(version as String).endsWith("SNAPSHOT") }
}

fun getStringProperty(name: String, defaultValue: () -> String) = if (project.hasProperty(name)) {
    nonNullProperty(name).ifEmpty(defaultValue)
} else {
    defaultValue()
}

fun getStringPropertyDefaultAsProperty(name: String, defaultValue: String) =
        getStringProperty(name) {
            val prop: String? = System.getenv(name)
            if (prop == null) {
                defaultValue
            } else {
                prop.ifEmpty { defaultValue }
            }
        }

fun nonNullProperty(name: String) = project.property(name) as String

fun String.ifEmpty(value: () -> String) = trim().run {
    if (isEmpty()) {
        value()
    } else {
        this
    }
}