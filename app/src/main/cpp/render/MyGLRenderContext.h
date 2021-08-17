//
// Created by admin on 2021/1/27.
//

#ifndef OPENGLES_MYGLRENDERCONTEXT_H
#define OPENGLES_MYGLRENDERCONTEXT_H
#include <GLES3/gl3.h>
#include <stdlib.h>
#include <thread>
using namespace std;
class MyGLRenderContext{
public:
    static MyGLRenderContext* getInstance();
    static void DestroyInstance();
    void onSurfaceCreate();
    void onSurfaceChange(int width,int height);
    void onDrawFrame();

    static void threadProcess(void *);

private:
    static MyGLRenderContext* m_glRenderContext;
    int m_width;
    int m_height;
    thread* m_thread = nullptr;
};

#endif //OPENGLES_MYGLRENDERCONTEXT_H
