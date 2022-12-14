in vec3 position;
in vec3 normal;
in vec2 uv;

out vec3 vNormal;
out vec2 vUv;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

void main() {
    vUv = uv;
    gl_Position = vec4(position, 1.0);
}
