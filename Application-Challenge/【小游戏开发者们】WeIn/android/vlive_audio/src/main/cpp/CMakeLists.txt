cmake_minimum_required(VERSION 3.4.1)

add_library(
        vlive_audio
        SHARED
        audio_render.cpp
#        audio_player.cpp
)

# Your-external "mylib", add GLOBAL if the imported library is located in directories above the current.
add_library(ra SHARED IMPORTED)
# You can define two import-locations: one for debug and one for release.
set_target_properties(ra PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/lib/${CMAKE_ANDROID_ARCH_ABI}/libResonanceAudioShared.so)

#find_package(oboe REQUIRED CONFIG)

include_directories(include)

target_link_libraries(
        vlive_audio
        log
#        oboe::oboe
        ra
)