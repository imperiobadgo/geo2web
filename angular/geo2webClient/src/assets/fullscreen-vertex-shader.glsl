varying vec2 vUv;
varying mat4 cameraProjection;
uniform mat4 inverseCameraProjection;
void main() {
    vUv = uv;
    gl_Position = vec4(position, 1.0);
}
