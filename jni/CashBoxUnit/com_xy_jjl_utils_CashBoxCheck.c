#include "com_xy_jjl_utils_CashBoxCheck.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <signal.h>

#include <fcntl.h>
#include <termios.h>
#include <errno.h>
#include <time.h>

#include <android/log.h>

#define CashBoxDeviceName "/dev/boxgpio"

int device_fd;

#ifdef __cplusplus
extern "C" {
#endif


JNIEXPORT jint JNICALL Java_com_xy_jjl_utils_CashBoxCheck_jCashBoxDeviceInit
  (JNIEnv *env, jobject thiz) {

	device_fd = open(CashBoxDeviceName, O_RDWR | O_NDELAY);

	return device_fd;
}


JNIEXPORT jint JNICALL Java_com_xy_jjl_utils_CashBoxCheck_jCashBoxStatusGet
  (JNIEnv *env, jobject thiz) {

	unsigned char box_flag[1];

	read(device_fd, box_flag, sizeof(box_flag));

	__android_log_print(ANDROID_LOG_DEBUG, "JNI_data", "box_flag = %02x", box_flag[0]);

	if(box_flag[0] == 1) {
		return 1;
	}

	return 0;

}

#ifdef __cplusplus
}
#endif
