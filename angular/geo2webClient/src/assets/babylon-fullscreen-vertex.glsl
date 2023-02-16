#version 300 es
precision highp float;

// Attributes
in vec3 position;
in vec2 uv;

// Varying
out vec2 fragmentPosition;

// Uniforms
uniform mat4 worldViewProjection;

void main() {
    fragmentPosition = position.xy;
    gl_Position = vec4(position, 1.0);
}
