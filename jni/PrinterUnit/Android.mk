LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := printer_unit
LOCAL_SRC_FILES := com_xy_jjl_utils_PrinterUnit.c
LOCAL_LDLIBS := -llog
include $(BUILD_SHARED_LIBRARY)