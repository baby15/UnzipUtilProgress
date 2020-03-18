LOCAL_PROJECT_PATH := $(call my-dir)
APP_PLATFORM := android-19

APP_CFLAGS += -O3 

APP_ABI := armeabi-v7a arm64-v8a 

APP_PIE := true

APP_OPTIM := release

NDK_TOOLCHAIN_VERSION := clang
