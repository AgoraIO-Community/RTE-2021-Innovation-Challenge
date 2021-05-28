import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

apply {
    FilamentToolsPlugin().apply(project)
}


tasks {
    val copyModel by registering(Copy::class) {
        from("resource/models/")
        into("src/main/assets/models/")
    }

    val copyShader by registering(Copy::class) {
        from("resource/shaders/")
        into("src/main/assets/shaders/")
    }

    preBuild.dependsOn(copyModel)
    preBuild.dependsOn(copyShader)

    named("clean") {
        doFirst {
            delete("src/main/assets")
        }
    }

}

extensions.configure<FilamentToolsPluginExtension> {
    cmgenArgs =
        "-q --format=ktx --size=256 --extract-blur=0.1 --deploy=src/main/assets/envs/default_env"
    iblInputFile.set(project.layout.projectDirectory.file("resource/env/lightroom_14b.hdr"))
    iblOutputDir.set(project.layout.projectDirectory.dir("src/main/assets/envs"))

    materialInputDir.set(project.layout.projectDirectory.dir("resource/materials"))
    materialOutputDir.set(project.layout.projectDirectory.dir("src/main/assets/materials"))
}


android {
    compileSdkVersion(Dep.Build.COMPILE_SDK_VERSION)
    buildToolsVersion(Dep.Build.BUILD_TOOLS_VERSION)

    defaultConfig {
        applicationId = Dep.Build.APPLICATION_ID
        minSdkVersion(Dep.Build.MIN_SDK_VERSION)
        targetSdkVersion(Dep.Build.TARGET_SDK_VERSION)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk {
            abiFilters.apply {
                add("armeabi-v7a")
//                add("arm64-v8a")
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        prefab = true
    }
    aaptOptions {
        noCompress("filamat")
    }
}

dependencies {
    implementation(project(":vlive_audio"))

    implementation(kotlin("stdlib", Dep.Kt.KOTLIN_VERSION))
    implementation(Dep.MLKit.ML_KIT_FACE)
    implementation(Dep.Kt.COROUTINE)
    implementation(Dep.AndroidX.CORE)
    implementation(Dep.AndroidX.CORE_KTX)
    implementation(Dep.AndroidX.APPCOMPAT)
    implementation(Dep.AndroidX.MATERIAL)
    implementation(Dep.AndroidX.CONSTRAINTLAYOUT)
    implementation(Dep.AndroidX.DATA_STORE)
    implementation(Dep.AndroidX.NAVIGATION_FRAGMENT)
    implementation(Dep.AndroidX.NAVIGATION_UI)
    implementation(Dep.AndroidX.NAVIGATION_DYNAMIC_FEATURE)
    implementation(Dep.AndroidX.VIEW_PAGER)

    implementation(Dep.CameraX.CAMERA2)
    implementation(Dep.CameraX.LIFE_CYCLE)
    implementation(Dep.CameraX.CAMERA_VIEW)

    implementation(Dep.Filament.FILAMENT)
    implementation(Dep.Filament.GLTFIO)
    implementation(Dep.Filament.UTILS)
    implementation(Dep.Filament.FILAMAT)

    implementation(Dep.Agora.AGORA)
    implementation(Dep.Agora.RTM)

    implementation(Dep.AndroidX.Lifecycle.COMMON_KTX)
    implementation(Dep.AndroidX.Lifecycle.LIVEDATA)
    implementation(Dep.AndroidX.Lifecycle.LD_KTX)
    implementation(Dep.AndroidX.Lifecycle.VM_KTX)
    implementation(Dep.AndroidX.Lifecycle.RTM_KTX)

    implementation(Dep.ARCore.CORE)

    implementation(Dep.OKHTTP)
    implementation(Dep.OKHTTP_LOGGER)
    implementation(Dep.RETROFIT)
    implementation(Dep.RETROFIT_MOSHI)
    implementation(Dep.MOSHI)
    implementation(Dep.RECYCLERVIEW_ADAPTER_HELPER)
    implementation(Dep.RECYCLERVIEW_ANIMATION)
    implementation(Dep.INDICATOR)

    kapt(Dep.MOSHI_COMPILER)
}