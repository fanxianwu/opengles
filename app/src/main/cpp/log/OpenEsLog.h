//
// Created by admin on 2021/1/26.
//

#ifndef OPENGLES_OPENESLOG_H
#define OPENGLES_OPENESLOG_H
#include<android/log.h>
#include <sys/time.h>
#define TAG "MYOPENES"

#define DEBUG
#ifdef DEBUG

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__)
#else

#endif

#define FUN_BEGIN_TIME(FUN) {\
    LOGE("%s:%s fun start",__FILE__,FUN); \
    long long t0 = GetSysCurrentTime();

#define FUN_END_TIME(FUN) { \
    long long t1 = GetSysCurrentTime(); \
    LOGE("%s:%s fun end cos time:%ldms",__FILE__,FUN,(t1 - t0));

#endif //OPENGLES_OPENESLOG_H
