in vec3 position;
in vec3 normal;
in vec2 uv;

out vec2 fragmentPosition;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

void main() {
    fragmentPosition = position.xy;
    gl_Position = vec4(position, 1.0);
}
