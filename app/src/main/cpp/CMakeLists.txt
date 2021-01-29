cmake_minimum_required(VERSION 3.4.1)


include_directories(
        log
        render
)

file(GLOB sourceFiles ${CMAKE_SOURCE_DIR}/*.cpp
        ${CMAKE_SOURCE_DIR}/render/*.cpp
        ${CMAKE_SOURCE_DIR}/log/*.cpp)

message("sourceFiles:"${sourceFiles})
add_library(
        native-render
        SHARED
        ${sourceFiles}
        )


find_library(
        my-log
        log
)

target_link_libraries(
        native-render
        ${my-log}
        EGL

)