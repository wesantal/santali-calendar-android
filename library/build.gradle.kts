plugins {
    alias(libs.plugins.android.library)
    `maven-publish`
    signing
}

android {
    namespace = "org.wesantal.santalicalendar"
    compileSdk = 35

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
                "proguard-rules.pro"
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
    implementation("androidx.annotation:annotation:1.7.1")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "org.wesantal"
                artifactId = "santali-calendar"
                version = "1.0.0"

                pom {
                    name.set("Santali Calendar")
                    description.set("An Ol Chiki (Santali) lunar calendar library for Android, providing accurate moon phase calculations, festival dates, and month/day conversions based on the Metonic cycle.")
                    url.set("https://github.com/wesantal/santali-calendar-android")

                    licenses {
                        license {
                            name.set("The Apache Software License, Version 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
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
                        connection.set("scm:git:git://github.com/wesantal/santali-calendar-android.git")
                        developerConnection.set("scm:git:ssh://github.com/wesantal/santali-calendar-android.git")
                        url.set("https://github.com/wesantal/santali-calendar-android")
                    }
                }
            }
        }

        repositories {
            maven {
                name = "MavenCentral"
                val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
                credentials {
                    username = findProperty("mavenCentralUsername") as? String ?: ""
                    password = findProperty("mavenCentralPassword") as? String ?: ""
                }
            }
            maven {
                name = "Local"
                url = uri(rootProject.layout.buildDirectory.dir("maven-repo"))
            }
        }
    }

    signing {
        val signingKey = findProperty("signingInMemoryKey") as? String
        val signingPassword = findProperty("signingInMemoryKeyPassword") as? String
        val mavenCentralUser = findProperty("mavenCentralUsername") as? String
        if (signingKey != null && signingPassword != null && mavenCentralUser != null) {
            useInMemoryPgpKeys(signingKey, signingPassword)
            sign(publishing.publications["release"])
        }
    }
}
