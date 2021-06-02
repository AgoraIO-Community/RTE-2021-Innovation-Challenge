# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

project(native-lib)

#link agora so
#set(agora-lib-so ${PROJECT_SOURCE_DIR}/../jniLibs/${CMAKE_ANDROID_ARCH_ABI}/libagora-rtc-sdk-jni.so)

#link_libraries(${agora-lib-so})


#link bytedance so
set(bytedance-lib-so ${PROJECT_SOURCE_DIR}/../jniLibs/${CMAKE_ANDROID_ARCH_ABI}/libeffect.so)

link_libraries(${bytedance-lib-so})

set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++14")


# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED
        plugin_source_code/ExtensionVideoProvider.cpp
        plugin_source_code/ExtensionAudioProvider.cpp
        plugin_source_code/ExtensionVideoFilter.cpp
        plugin_source_code/ExtensionAudioFilter.cpp
        plugin_source_code/EGLCore.cpp
        plugin_source_code/JniHelper.cpp
        plugin_source_code/VideoProcessor.cpp
        plugin_source_code/AudioProcessor.cpp
             # Provides a relative path to your source file(s).
        native-lib.cpp)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_include_directories(native-lib PRIVATE ${PROJECT_SOURCE_DIR})

target_link_libraries( # Specifies the target library.
                       native-lib
#                        ${agora-lib-so}
                        ${bytedance-lib-so}
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}
#                       ${GLESv2}
                        ${GLESv3}
                        EGL
                        android)