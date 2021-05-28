object Dep {

    var filamentPath = ""

    object Build {
        const val APPLICATION_ID = "com.hustunique.vlive"
        const val MIN_SDK_VERSION = 29
        const val TARGET_SDK_VERSION = 30
        const val COMPILE_SDK_VERSION = 30
        const val BUILD_TOOLS_VERSION = "30.0.3"
        const val ANDROID_TOOLS = "com.android.tools.build:gradle:4.1.3"
    }

    object Kt {
        const val KOTLIN_VERSION = "1.4.31"
        const val COROUTINE = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2"
    }

    object Test {
        const val JUNIT = "junit:junit:4.+"
        const val EXT_JUNIT = "androidx.test.ext:junit:1.1.2"
        const val ESPRESSO = "androidx.test.espresso:espresso-core:3.3.0"
    }

    object AndroidX {
        const val CORE = "androidx.core:core:1.5.0"
        const val CORE_KTX = "androidx.core:core-ktx:1.5.0"
        const val APPCOMPAT = "androidx.appcompat:appcompat:1.2.0"
        const val MATERIAL = "com.google.android.material:material:1.3.0"
        const val CONSTRAINTLAYOUT = "androidx.constraintlayout:constraintlayout:2.0.4"
        const val DATA_STORE = "androidx.datastore:datastore-preferences:1.0.0-beta01"

        private const val NAVIGATION_VERSION = "2.3.5"
        const val NAVIGATION_FRAGMENT =
            "androidx.navigation:navigation-fragment-ktx:$NAVIGATION_VERSION"
        const val NAVIGATION_UI = "androidx.navigation:navigation-ui-ktx:$NAVIGATION_VERSION"
        const val NAVIGATION_DYNAMIC_FEATURE =
            "androidx.navigation:navigation-dynamic-features-fragment:$NAVIGATION_VERSION"
        const val NAVIGATION_SAFE_ARGS =
            "androidx.navigation:navigation-safe-args-gradle-plugin:$NAVIGATION_VERSION"
        const val VIEW_PAGER = "androidx.viewpager2:viewpager2:1.0.0"

        object Lifecycle {
            private const val VERSION = "2.3.1"
            const val VM_KTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:$VERSION"
            const val LIVEDATA = "androidx.lifecycle:lifecycle-livedata:$VERSION"
            const val LD_KTX = "androidx.lifecycle:lifecycle-livedata-ktx:$VERSION"
            const val COMMON_KTX = "androidx.lifecycle:lifecycle-common-java8:$VERSION"
            const val RTM_KTX = "androidx.lifecycle:lifecycle-runtime-ktx:$VERSION"
        }
    }

    object MLKit {
        const val ML_KIT_FACE = "com.google.mlkit:face-detection:16.0.6"
    }

    object CameraX {
        const val CAMERA2 = "androidx.camera:camera-camera2:1.0.0-beta07"
        const val LIFE_CYCLE = "androidx.camera:camera-lifecycle:1.0.0-beta07"
        const val CAMERA_VIEW = "androidx.camera:camera-view:1.0.0-alpha14"
    }

    object Filament {
        private const val BASE_VAR = "1.9.21"

        const val FILAMENT = "com.google.android.filament:filament-android:${BASE_VAR}"
        const val GLTFIO = "com.google.android.filament:gltfio-android:${BASE_VAR}"
        const val UTILS = "com.google.android.filament:filament-utils-android:${BASE_VAR}"
        const val FILAMAT = "com.google.android.filament:filamat-android:${BASE_VAR}"
    }

    object Agora {
        const val AGORA = "com.github.agorabuilder:native-full-sdk:3.4.1"
        const val RTM = "com.github.agorabuilder:rtm-sdk:1.4.5"
    }

    object ARCore {
        const val CORE = "com.google.ar:core:1.23.0"
    }

    const val OBOE = "com.google.oboe:oboe:1.5.0"

    private const val RETROFIT_VER = "2.9.0"
    const val RETROFIT = "com.squareup.retrofit2:retrofit:${RETROFIT_VER}"
    const val RETROFIT_MOSHI = "com.squareup.retrofit2:converter-moshi:${RETROFIT_VER}"

    private const val OKHTTP_VER = "4.9.0"
    const val OKHTTP = "com.squareup.okhttp3:okhttp:${OKHTTP_VER}"
    const val OKHTTP_LOGGER = "com.squareup.okhttp3:logging-interceptor:${OKHTTP_VER}"

    private const val MOSHI_VER = "1.10.0"
    const val MOSHI = "com.squareup.moshi:moshi:${MOSHI_VER}"
    const val MOSHI_COMPILER = "com.squareup.moshi:moshi-kotlin-codegen:${MOSHI_VER}"
    const val RECYCLERVIEW_ADAPTER_HELPER = "com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.6"
    const val RECYCLERVIEW_ANIMATION = "jp.wasabeef:recyclerview-animators:4.0.2"
    const val INDICATOR = "com.tbuonomo:dotsindicator:4.2"
}