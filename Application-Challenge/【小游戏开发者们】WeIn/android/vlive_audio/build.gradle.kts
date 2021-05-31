plugins {
    id("com.android.library")
    id("kotlin-android")
}


android {
    compileSdkVersion(Dep.Build.COMPILE_SDK_VERSION)
    buildToolsVersion(Dep.Build.BUILD_TOOLS_VERSION)

    defaultConfig {
        minSdkVersion(Dep.Build.MIN_SDK_VERSION)
        targetSdkVersion(Dep.Build.TARGET_SDK_VERSION)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        externalNativeBuild {
            cmake {
                arguments += "-DANDROID_STL=c++_shared"
            }
        }
        ndk {
//            abiFilters.add("arm64-v8a")
            abiFilters.add("armeabi-v7a")
        }
        buildFeatures {
            prefab = true
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
    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
        }
    }
}


dependencies {

    implementation(kotlin("stdlib", Dep.Kt.KOTLIN_VERSION))
    implementation(Dep.OBOE)

}
