LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := filehash 
LOCAL_CPPFLAGS += -ffunction-sections -fdata-sections -fvisibility=hidden -O3
LOCAL_CFLAGS +=  -ffunction-sections -fdata-sections -fvisibility=hidden -O3
LOCAL_LDFLAGS += -Wl,--gc-sections 
LOCAL_SRC_FILES = main.c hash.c 
include $(BUILD_EXECUTABLE)

include $(CLEAR_VARS)
LOCAL_MODULE := hash 
LOCAL_SRC_FILES :=  hash.c 
LOCAL_LDLIBS += -llog
include $(BUILD_SHARED_LIBRARY)
