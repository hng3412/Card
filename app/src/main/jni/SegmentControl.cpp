//
// Created by DKU on 2019-06-10.
//
/// /*
// * Class:     com_example_hardwaretest_SevenSegment
// * Method:    countdown
// * Signature: (I)I
// */


#include <string.h>
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <termios.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <asm/unistd.h>
#include <zconf.h>

#include "com_example_card_CardGame.h"
/*
 * Class:     com_example_card_CardGame
 * Method:    SegmentControl
 * Signature: (I)Ljava/lang/String;
 */


ssize_t write(int fd, const void *buf, size_t count){
    return syscall(__NR_write, fd, buf, count);
}

int dev, ret ;

JNIEXPORT jstring JNICALL Java_com_example_card_CardGame_SegmentControl
(JNIEnv* env,jobject thiz, jint data ){

    dev = open("/dev/fpga_segment",O_RDWR | O_SYNC);

    if(dev != -1) {
        for(int a = 0; a<80; a++) {
            ret = write(dev, &data, 4);
        }
        close(dev);
    } else {
        //__android_log_print(ANDROID_LOG_ERROR, "SegmentActivity", "Device Open ERROR!\n");
        exit(1);
    }
    return 0;
}


JNIEXPORT jstring JNICALL Java_com_example_card_CardGame_SegmentIOControl
(JNIEnv* env,jobject thiz, jint data ){

    dev = open("/dev/fpga_segment",O_RDWR | O_SYNC);

    if(dev != -1) {
        ret = ioctl(dev, data, NULL, NULL);
        close(dev);
    } else {
        //__android_log_print(ANDROID_LOG_ERROR, "SegmentActivity", "Device Open ERROR!\n");
        exit(1);
    }
    return 0;
}


JNIEXPORT jstring JNICALL Java_com_example_card_CardGame_SegmentClose
        (JNIEnv *, jobject, jint){
    close(dev);
    return 0;

}
