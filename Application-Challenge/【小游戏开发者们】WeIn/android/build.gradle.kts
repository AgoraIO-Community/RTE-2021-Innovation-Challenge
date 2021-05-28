// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlin_version by extra("1.5.0")
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(Dep.Build.ANDROID_TOOLS)
        classpath(kotlin("gradle-plugin", Dep.Kt.KOTLIN_VERSION))
        classpath(Dep.AndroidX.NAVIGATION_SAFE_ARGS)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { setUrl("https://www.jitpack.io") }
        maven { setUrl("https://maven.google.com/artifac") }
        maven { setUrl("https://mvnrepository.com/artifac") }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}