#include "com_xy_jjl_utils_MoneyUnit.h"
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

#define BA_DEVICE_NAME "/dev/ttyS3"
#define UART_RW_LENGTH 3

int BA_fd;
int BA_uart_wait_flag;
int read_flag = 0;
unsigned char receive_buffer[UART_RW_LENGTH];

int uartA_delay_time = 50000;

#ifdef __cplusplus
extern "C" {
#endif

void signal_handler_io(int status) {
	BA_uart_wait_flag = BA_UART_FALSE;
}

JNIEXPORT jint JNICALL Java_com_xy_jjl_utils_MoneyUnit_jBAdeviceInit
(JNIEnv *env, jobject thiz) {
	struct sigaction sa_action;

	BA_fd = open(BA_DEVICE_NAME, O_RDWR | O_NOCTTY | O_NDELAY);

	sa_action.sa_handler = signal_handler_io;
	sigemptyset(&sa_action.sa_mask);

	sa_action.sa_flags = 0;
	sa_action.sa_restorer = NULL;

	sigaction(SIGIO, &sa_action, NULL);

	fcntl(BA_fd, F_SETOWN, getpid());
	fcntl(BA_fd, F_SETFL, FASYNC);

	struct termios options;

	tcgetattr(BA_fd, &options);

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
	options.c_cflag |= PARENB;
	options.c_cflag &= ~PARODD;

	cfsetospeed(&options, B9600);
	cfsetispeed(&options, B9600);

	tcflush(BA_fd, TCIFLUSH);
	tcsetattr(BA_fd, TCSANOW, &options);

	return BA_fd;
}

void* read_data(void *arg) {

	unsigned char tmp_buffer = 0;

	int money_flag = 0;

	memset(receive_buffer, 0, UART_RW_LENGTH);

	while (1) {

		usleep(uartA_delay_time);

		if (BA_uart_wait_flag == BA_UART_FALSE) {
			read(BA_fd, &tmp_buffer, 1);

			if(tmp_buffer == 0x3e) {
				receive_buffer[0] = tmp_buffer;
				money_flag = 0;
				read_flag = 1;
			}

			if(tmp_buffer == 0x5e) {
				receive_buffer[0] = tmp_buffer;
				money_flag = 0;
				read_flag = 1;
			}

			if(tmp_buffer == 0x80) {
				receive_buffer[0] = tmp_buffer;
				continue;
			}

			if((tmp_buffer == 0x8f) && (receive_buffer[0] == 0x80)) {
				receive_buffer[1] = tmp_buffer;
				money_flag = 0;
				read_flag = 1;
			}

			if(tmp_buffer == 0x81) {
				receive_buffer[0] = tmp_buffer;
				continue;
			}

			if(tmp_buffer == 0x8f && (receive_buffer[0] == 0x81)) {
				receive_buffer[1] = tmp_buffer;
				continue;
			}

			if((tmp_buffer == 0x40) || (tmp_buffer == 0x41) || (tmp_buffer == 0x42) || (tmp_buffer == 0x43) || (tmp_buffer == 0x44)) {

				if(money_flag == 1) {
					continue;
				}

				receive_buffer[2] = tmp_buffer;
				read_flag = 1;
				money_flag = 1;
			}

			//__android_log_print(ANDROID_LOG_DEBUG, "JNI_data", "%02x, %02x, %02x, %02x",
			//receive_buffer[0], receive_buffer[1], receive_buffer[2], receive_buffer[3]);

			//read_flag = 1;
		}

	}

}

JNIEXPORT void JNICALL Java_com_xy_jjl_utils_MoneyUnit_jBAdeviceStartRunning
(JNIEnv *env, jobject thiz) {

	int errCode = 0;
	do {
		pthread_attr_t tAttr;
		errCode = pthread_attr_init(&tAttr);

		errCode = pthread_attr_setdetachstate(&tAttr, PTHREAD_CREATE_DETACHED);
		if (errCode != 0) {
			pthread_attr_destroy(&tAttr);
			break;
		}
		errCode = pthread_create(&uarta_pth_socket, &tAttr, read_data, NULL);

	}while (0);

}

JNIEXPORT jint JNICALL Java_com_xy_jjl_utils_MoneyUnit_jBAstatusFlagCheck
(JNIEnv *env, jobject thiz) {
	return read_flag;

}

JNIEXPORT void JNICALL Java_com_xy_jjl_utils_MoneyUnit_jBAstatusFlagClear
(JNIEnv *env, jobject thiz, jint flag) {
	read_flag = flag;

}

JNIEXPORT jbyteArray JNICALL Java_com_xy_jjl_utils_MoneyUnit_jBAdataReceive
(JNIEnv *env, jobject thiz) {
	jbyteArray array = (*env)->NewByteArray(env, UART_RW_LENGTH);

	(*env)->SetByteArrayRegion(env, array, 0, UART_RW_LENGTH, receive_buffer);

	//memset(receive_buffer, 0, sizeof(receive_buffer));

	return array;

}

JNIEXPORT void JNICALL Java_com_xy_jjl_utils_MoneyUnit_jBAdataSend
(JNIEnv *env, jobject thiz, jbyteArray buffer) {
	unsigned char array[UART_RW_LENGTH];

	(*env)->GetByteArrayRegion(env, buffer, 0, UART_RW_LENGTH, array);

	write(BA_fd, array, sizeof(array));

}

#ifdef __cplusplus
}
#endif
