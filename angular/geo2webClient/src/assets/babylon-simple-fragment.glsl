#version 300 es
precision highp float;

layout (location = 0) out vec4 pc_FragColor;
layout (location = 1) out vec4 pc_Id;

in vec2 vUV;
in float vFragmentDepth;

//uniform float logarithmicDepthConstant;

void main() {

    pc_FragColor = vec4(1.0,0.0,0.1,1.0);
    pc_Id = vec4(0.0);
    float logarithmicDepthConstant = 0.1;
    gl_FragDepth = log2(vFragmentDepth) * 1.0 * logarithmicDepthConstant;
}
