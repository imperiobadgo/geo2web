attribute vec4 aVertexPosition;
attribute vec4 aVertexColor;
uniform mat4 uModelViewMatrix;
uniform mat4 uProjectionMatrix;
uniform mat4 uViewMatrix;
varying lowp vec4 vColor;
void main(void) {
    gl_Position = uProjectionMatrix * uViewMatrix * uModelViewMatrix * aVertexPosition;
    vColor = aVertexColor;
}
