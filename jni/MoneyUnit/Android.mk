LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := money_unit
LOCAL_SRC_FILES := com_xy_jjl_utils_MoneyUnit.c
LOCAL_LDLIBS := -llog
include $(BUILD_SHARED_LIBRARY)