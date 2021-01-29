#include <jni.h>

//
// Created by admin on 2021/1/26.
//

#include "log/OpenEsLog.h"
#include "render/MyGLRenderContext.h"
#include "jni.h"
#ifdef __cplusplus
extern "C" {
#endif

const char *className = "com/example/opengles/MyNativeSurfaceRender";

void native_init(JNIEnv *env,jobject instance){
    LOGI("call native_init");
    MyGLRenderContext::getInstance();
}

void native_uninit(JNIEnv *env,jobject instance){
    LOGI("call native_uninit");
}

void onSurfaceCreate(JNIEnv *env,jobject instance){
    LOGI("call onSurfaceCreate");
}

void onSurfaceChange(JNIEnv *env,jobject instance,int width,int height){
    LOGI("call onSurfaceChange");
}

void onDrawFrame(JNIEnv *env,jobject instance){
    LOGI("call onDraw");
}

#ifdef __cplusplus
}
#endif

static JNINativeMethod nativeMethods[]{
        {"native_init","()V",(void*)native_init},
        {"native_uninit","()V",(void*)native_uninit},
        {"native_onSurfaceCreate","()V",(void*)onSurfaceCreate},
        {"native_onSurfaceChange","(II)V",(void*)onSurfaceChange},
        {"native_onDrawFrame","()V",(void*)onDrawFrame}
};


static int registerNativeMethod(JNIEnv* env,const char* className,const JNINativeMethod* gMethods,int numMethods){
    LOGI("registerNativeMethod:className:%s and numMethods:%d",className,numMethods);
    jclass jClassName = env->FindClass(className);
    if(jClassName != nullptr){
        LOGI("find class %s",className);
          return env->RegisterNatives(jClassName,gMethods,numMethods);
    }
    return JNI_ERR;
}

extern "C" jint JNI_OnLoad(JavaVM *vm,void *params){
    LOGI("----JNI_Onload----");
    JNIEnv *env;
    jint jniRet = JNI_ERR;
    if(vm->GetEnv((void **)&env,JNI_VERSION_1_6) != JNI_OK){
        LOGI("getEnvError");
        return jniRet;
    }

    jniRet = registerNativeMethod(env,className,nativeMethods,sizeof(nativeMethods)/sizeof(JNINativeMethod));
    if(jniRet != JNI_OK){
        LOGI("register EnvError");
        return JNI_ERR;
    }
    return JNI_VERSION_1_6;
}


