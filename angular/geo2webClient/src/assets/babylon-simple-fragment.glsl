#version 300 es
precision highp float;

layout (location = 0) out vec4 pc_FragColor;

in vec2 vUV;

void main() {
    pc_FragColor = vec4(1.0,0.0,0.1,1.0);
}
