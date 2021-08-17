//
// Created by admin on 2021/1/27.
//

#include "MyGLRenderContext.h"
#include "OpenEsLog.h"

MyGLRenderContext *MyGLRenderContext::m_glRenderContext = nullptr;

void thread_process(int num){
    LOGI("start run thread and num is %d",num);
}

void MyGLRenderContext::threadProcess(void *) {
    LOGI("start run thread and");
}

MyGLRenderContext * MyGLRenderContext::getInstance() {
    if(m_glRenderContext == nullptr){
        m_glRenderContext = new MyGLRenderContext();
    }
    return m_glRenderContext;
}

void MyGLRenderContext::DestroyInstance() {
    if(m_glRenderContext != nullptr){
        delete m_glRenderContext;
    }
}

void MyGLRenderContext::onSurfaceCreate() {
    LOGI("native onSurfaceCreate");
    glClearColor(1.0,1.0,1.0,1.0);
    m_thread = new thread(thread_process,10);

}

void MyGLRenderContext::onSurfaceChange(int width, int height) {

}

void MyGLRenderContext::onDrawFrame() {

}



