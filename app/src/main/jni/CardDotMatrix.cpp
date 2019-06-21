//
// Created by DKU on 2019-06-21.
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

#include "com_example_card_GameActivity.h"
JNIEXPORT jstring JNICALL Java_com_example_card_GameActivity_CardDotMatrix
        (JNIEnv* env, jobject thiz, jstring inputData){

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