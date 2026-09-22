plugins {
    alias(libs.plugins.android.library)
    `maven-publish`
    signing
}

android {
    namespace = "org.wesantal.santalicalendar"
    compileSdk = 37

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.annotation)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "org.wesantal"
            artifactId = "santali-calendar"
            version = "1.0.0"

            afterEvaluate {
                from(components["release"])
            }

            pom {
                name.set("Santali Calendar")
                description.set(
                    "An Ol Chiki (Santali) lunar calendar library for Android, " +
                            "providing moon phase calculations, festival dates, " +
                            "and Santali calendar month and day calculations."
                )
                url.set(
                    "https://github.com/wesantal/santali-calendar-android"
                )

                licenses {
                    license {
                        name.set(
                            "The Apache Software License, Version 2.0"
                        )
                        url.set(
                            "https://www.apache.org/licenses/LICENSE-2.0.txt"
                        )
                        distribution.set("repo")
                    }
                }

                developers {
                    developer {
                        id.set("wesantal")
                        name.set("WeSantal")
                        url.set("https://github.com/wesantal")
                    }
                }

                scm {
                    connection.set(
                        "scm:git:https://github.com/wesantal/" +
                                "santali-calendar-android.git"
                    )
                    developerConnection.set(
                        "scm:git:ssh://git@github.com/wesantal/" +
                                "santali-calendar-android.git"
                    )
                    url.set(
                        "https://github.com/wesantal/" +
                                "santali-calendar-android"
                    )
                }
            }
        }
    }

    repositories {
        /*
         * Sonatype Central Portal OSSRH compatibility API.
         *
         * DO NOT use:
         * https://s01.oss.sonatype.org/...
         */
        maven {
            name = "MavenCentral"

            url = uri(
                "https://ossrh-staging-api.central.sonatype.com/" +
                        "service/local/staging/deploy/maven2/"
            )

            credentials {
                username =
                    providers.gradleProperty("mavenCentralUsername")
                        .orNull

                password =
                    providers.gradleProperty("mavenCentralPassword")
                        .orNull
            }
        }

        /*
         * Local repository for testing.
         */
        maven {
            name = "Local"

            url = uri(
                rootProject.layout.buildDirectory
                    .dir("maven-repo")
            )
        }
    }
}

signing {
    val signingKey =
        providers.gradleProperty("signingInMemoryKey").orNull

    val signingPassword =
        providers.gradleProperty(
            "signingInMemoryKeyPassword"
        ).orNull

    if (
        signingKey != null &&
        signingPassword != null
    ) {
        useInMemoryPgpKeys(
            signingKey,
            signingPassword,
        )

        sign(
            publishing.publications["release"]
        )
    }
}