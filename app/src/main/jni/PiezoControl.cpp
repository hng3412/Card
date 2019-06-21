//
// Created by DKU on 2019-06-19.
//

#include <jni.h>
#include <string.h>
#include <asm/unistd.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <errno.h>
#include <zconf.h>

#include "com_example_card_CardGame.h"
/*
 * Class:     com_example_card_CardGame
 * Method:    PiezoControl
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_example_card_CardGame_PiezoControl
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