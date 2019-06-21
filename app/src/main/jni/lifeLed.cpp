//
// Created by DKU on 2019-06-10.
//


/*
 * Class:     com_example_card_CardGame
 * Method:    lifeLed
 * Signature: (I)I
 */

#include <jni.h>
#include <string.h>
#include <asm/unistd.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <zconf.h>
#include <asm/ioctl.h>
#include <asm/unistd.h>
#include <zconf.h>
#include <jni.h>
#include <string.h>
#include <asm/unistd.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include "com_example_card_CardGame.h"


JNIEXPORT jint JNICALL Java_com_example_card_CardGame_lifeLed
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
