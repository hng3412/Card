//
// Created by DKU on 2019-06-14.
//

//
// Created by DKU on 2019-06-14.
//


#include <string.h>
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <termios.h>
#include <sys/mman.h>
#include <errno.h>


static int fd;

#include "com_example_card_MainActivity.h"


JNIEXPORT jint JNICALL Java_com_example_card_MainActivity_swOpen
        (JNIEnv *, jobject){

    int ret;
    fd = open("/dev/fpga_dipsw",O_RDONLY);
    if(fd <= 0) return -errno;

    return fd;
}


JNIEXPORT jint JNICALL Java_com_example_card_MainActivity_swClose
        (JNIEnv *, jobject){

    if(fd > 0) {
        close(fd);
    }

    return 0;
}


JNIEXPORT jint JNICALL Java_com_example_card_MainActivity_swGetvalue
        (JNIEnv *env, jobject thiz){
    int ret;
    int data;

    if(fd < 0) return -errno;

    ret = read(fd, &data, 4);
    if(ret == 4) return data;

    return -1;
}

