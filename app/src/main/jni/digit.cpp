//
// Created by DKU on 2019-06-20.
//

#include <asm/unistd.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <string.h>
#include <jni.h>
#include <termios.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/mman.h>
#include <errno.h>

#include "com_example_card_DigitActivity.h"


static int swFd;
/*
 * Class:     com_example_card_DigitActivity
 * Method:    digitLed
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_example_card_DigitActivity_digitLed
        (JNIEnv *, jobject, jint life_num){

    int fd = open("/dev/fpga_led", O_RDWR);
    int i;
    unsigned char nullValue = 0x00;
    unsigned char values[] = { 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x21};
    unsigned short one = 1;
    unsigned short val = 0;
    //unsigned short val = number;

    for(i =0; i < life_num; i++){
        val = (val << 1) | 1;
    }

    write(fd, &val, sizeof(val));
    close(fd);
    return life_num;

}

/*
 * Class:     com_example_card_DigitActivity
 * Method:    digitBuzzer
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_example_card_DigitActivity_digitBuzzer
        (JNIEnv *, jobject, jint value){

    int fd, ret;
    int data = value;

    fd = open("/dev/fpga_piezo",O_WRONLY);

    if(fd < 0)
        return -errno;
    ret = write(fd, &data, 1);

    close(fd);
    if(ret == 1)
        return 0;
    return -1;

}

/*
 * Class:     com_example_card_DigitActivity
 * Method:    digitSegment
 * Signature: (I)Ljava/lang/String;
 */

int segdev, segret;
JNIEXPORT jstring JNICALL Java_com_example_card_DigitActivity_digitSegment
        (JNIEnv *, jobject, jint data){


    segdev = open("/dev/fpga_segment",O_RDWR | O_SYNC);

    if(segdev != -1) {
        for(int a = 0; a<80; a++) {
            segret = write(segdev, &data, 4);
        }
        close(segdev);
    } else {
        //__android_log_print(ANDROID_LOG_ERROR, "SegmentActivity", "Device Open ERROR!\n");
        exit(1);
    }
    return 0;
}

/*
 * Class:     com_example_card_DigitActivity
 * Method:    digitSwOpen
 * Signature: ()I
 */
JNIEXPORT jstring JNICALL Java_com_example_card_DigitActivity_digitIOSegment
        (JNIEnv *, jobject, jint data){

    segdev = open("/dev/fpga_segment",O_RDWR | O_SYNC);

    if(segdev != -1) {
        segret = ioctl(segdev, data, NULL, NULL);
        close(segdev);
    } else {
        //__android_log_print(ANDROID_LOG_ERROR, "SegmentActivity", "Device Open ERROR!\n");
        exit(1);
    }
    return 0;
}
JNIEXPORT jstring JNICALL Java_com_example_card_DigitActivity_digitSegmentClose
        (JNIEnv *, jobject, jint){

    close(segdev);
    return 0;
}
JNIEXPORT jint JNICALL Java_com_example_card_DigitActivity_digitSwOpen
        (JNIEnv *, jobject){

    int ret;
    swFd = open("/dev/fpga_dipsw",O_RDONLY);
    if(swFd <= 0) return -errno;

    return swFd;
}

/*
 * Class:     com_example_card_DigitActivity
 * Method:    digitSwClose
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_example_card_DigitActivity_digitSwClose
        (JNIEnv *, jobject){

    if(swFd > 0) {
        close(swFd);
    }

    return 0;
}

/*
 * Class:     com_example_card_DigitActivity
 * Method:    digitSwValue
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_example_card_DigitActivity_digitSwValue
        (JNIEnv *, jobject){
    int ret;
    int data;

    if(swFd < 0) return -errno;

    ret = read(swFd, &data, 4);
    if(ret == 4) return data;

    return -1;

}

/*
 * Class:     com_example_card_DigitActivity
 * Method:    digitDotMatrix
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_example_card_DigitActivity_digitDotMatrix
        (JNIEnv *env, jobject, jstring inputData){
    const char *buf;
    int dev, ret, len;
    char str[100];

    //buf = (env)->NewStringUTF(inputData);
    buf = env->GetStringUTFChars(inputData, 0);
    len = env->GetStringLength(inputData);

    // dev = open("/dev/fpga_DotMatrix", O_RDWR|O_SYNC);
    dev = open("/dev/fpga_dotmatrix", O_RDWR | O_SYNC);

    if(dev!=-1){

        ret = write(dev, buf, len);
        close(dev);
        return env->NewStringUTF(buf);
    }
    else{
        close(dev);
        exit(0);
        return env->NewStringUTF("fail");
    }
}