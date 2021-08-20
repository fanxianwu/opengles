precision mediump float;
varying vec2 aCoord;
uniform sampler2D vTexture;

void main() {
    vec4 color = texture2D(vTexture,aCoord);
    float r = color.r;
    vec4 grayColor = vec4(r,r,r,color.a);
    gl_FragColor = grayColor;
}
