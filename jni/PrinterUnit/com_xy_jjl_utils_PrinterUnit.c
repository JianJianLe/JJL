#include "com_xy_jjl_utils_PrinterUnit.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <signal.h>

#include <fcntl.h>
#include <termios.h>
#include <errno.h>

#include <android/log.h>

#define PRINTER_DEVICE_NAME "/dev/ttyS0"

#define UART_RW_LENGTH 255

int p_fd;

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jint JNICALL Java_com_xy_jjl_utils_PrinterUnit_jPrinterInit(
		JNIEnv *env, jobject thiz) {
	p_fd = open(PRINTER_DEVICE_NAME, O_RDWR | O_NOCTTY);

	struct termios options;

	tcgetattr(p_fd, &options);

	options.c_cflag |= (CLOCAL | CREAD);
	options.c_cflag &= ~CSIZE;
	options.c_cflag &= ~CRTSCTS;
	options.c_cflag |= CS8;
	options.c_cflag &= ~CSTOPB;

	options.c_iflag &= ~(BRKINT | ICRNL | INPCK | ISTRIP | IXON);
	options.c_oflag &= ~OPOST;
	options.c_cflag |= CLOCAL | CREAD;
	options.c_lflag &= ~(ICANON | ECHO | ECHOE | ISIG);

	//投币机必须设置为偶校验
	//options.c_cflag |= PARENB;
	//options.c_cflag &= ~PARODD;

	cfsetospeed(&options, B9600);
	cfsetispeed(&options, B9600);

	tcflush(p_fd, TCIFLUSH);
	tcsetattr(p_fd, TCSANOW, &options);

	return p_fd;
}

JNIEXPORT void JNICALL Java_com_xy_jjl_utils_PrinterUnit_jPrinterDataSend(
		JNIEnv *env, jobject thiz, jbyteArray buffer, jint data_len) {
	int len;

	unsigned char array[data_len];

	(*env)->GetByteArrayRegion(env, buffer, 0, data_len, array);

	len = write(p_fd, array, sizeof(array));

	__android_log_print(ANDROID_LOG_DEBUG, "JNI_data", "len = %d", len);
}

#ifdef __cplusplus
}
#endif
