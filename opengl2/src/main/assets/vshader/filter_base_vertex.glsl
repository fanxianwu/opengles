attribute vec4 vPosition;
attribute vec2 vCoord;
varying vec2 aCoord;
uniform mat4 vMatrix;

void main() {
   // vec4 tPositon = vec4(vPosition,0,0);
    gl_Position = vMatrix*vPosition;
    aCoord = vCoord;
}
