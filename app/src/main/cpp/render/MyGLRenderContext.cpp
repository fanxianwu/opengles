//
// Created by admin on 2021/1/27.
//

#include "MyGLRenderContext.h"

MyGLRenderContext *MyGLRenderContext::m_glRenderContext = nullptr;

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

}

void MyGLRenderContext::onSurfaceChange(int width, int height) {

}

void MyGLRenderContext::onDrawFrame() {

}



