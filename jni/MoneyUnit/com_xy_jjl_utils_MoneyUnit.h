/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
#include <pthread.h>
/* Header for class com_xy_jjl_utils_MoneyUnit */

#ifndef _Included_com_xy_jjl_utils_MoneyUnit
#define _Included_com_xy_jjl_utils_MoneyUnit

#define BA_UART_TRUE 1
#define BA_UART_FALSE 0

pthread_t uarta_pth_socket;


#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_xy_jjl_utils_MoneyUnit
 * Method:    jBAdeviceInit
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_xy_jjl_utils_MoneyUnit_jBAdeviceInit
  (JNIEnv *, jobject);

/*
 * Class:     com_xy_jjl_utils_MoneyUnit
 * Method:    jBAdeviceStartRunning
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_xy_jjl_utils_MoneyUnit_jBAdeviceStartRunning
  (JNIEnv *, jobject);

/*
 * Class:     com_xy_jjl_utils_MoneyUnit
 * Method:    jBAstatusFlagCheck
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_xy_jjl_utils_MoneyUnit_jBAstatusFlagCheck
  (JNIEnv *, jobject);

/*
 * Class:     com_xy_jjl_utils_MoneyUnit
 * Method:    jBAstatusFlagClear
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_xy_jjl_utils_MoneyUnit_jBAstatusFlagClear
  (JNIEnv *, jobject, jint);

/*
 * Class:     com_xy_jjl_utils_MoneyUnit
 * Method:    jBAdataReceive
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_xy_jjl_utils_MoneyUnit_jBAdataReceive
  (JNIEnv *, jobject);

/*
 * Class:     com_xy_jjl_utils_MoneyUnit
 * Method:    jBAdataSend
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_com_xy_jjl_utils_MoneyUnit_jBAdataSend
  (JNIEnv *, jobject, jbyteArray);

#ifdef __cplusplus
}
#endif
#endif
