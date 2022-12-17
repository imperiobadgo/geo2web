in vec3 position;
in vec3 normal;
in vec2 uv;

out vec3 vUv;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

void main(void) {
    vUv = position;

    vec4 modelViewPosition = modelViewMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * modelViewPosition;
}
