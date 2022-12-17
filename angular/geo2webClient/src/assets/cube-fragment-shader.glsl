precision highp float;
precision highp int;

layout (location = 0) out vec4 pc_FragColor;
layout (location = 1) out vec4 pc_test;

in vec3 vUv;

uniform vec3 colorA;
uniform vec3 colorB;

void main() {
    pc_FragColor = vec4(mix(colorA, colorB, vUv.y), 1.0);
    pc_test = pc_FragColor;
}
