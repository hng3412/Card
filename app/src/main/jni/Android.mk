LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := jniExample
LOCAL_SRC_FILES := lifeLed.cpp SegmentControl.cpp for_switch.cpp PiezoControl.cpp digit.cpp CardDotMatrix.cpp
LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)