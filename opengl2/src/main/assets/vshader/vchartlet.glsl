attribute vec4 vPosition;
attribute vec2 vCoordinate;
varying vec2 mCoordinate;
uniform mat4 mvpProject;

void main() {
    gl_Position = mvpProject*vPosition;
    mCoordinate = vCoordinate;
}
