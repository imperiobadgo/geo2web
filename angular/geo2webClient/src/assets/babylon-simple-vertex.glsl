#version 300 es
precision highp float;

// Attributes
in vec3 position;
in vec2 uv;

// Varying
out vec2 vUV;

// Uniforms
uniform mat4 worldViewProjection;

void main() {
    gl_Position = worldViewProjection * vec4(position, 1.0);

    vUV = uv;
}
