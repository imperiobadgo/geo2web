#version 300 es
precision highp float;

layout (location = 0) out vec4 pc_FragColor;
layout (location = 1) out vec4 pc_test;

in vec2 fragmentPosition;

// Uniforms
uniform mat4 worldViewProjection;

void main() {
    pc_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
    pc_test = vec4(0.0, 0.0, 1.0, 1.0);
}
