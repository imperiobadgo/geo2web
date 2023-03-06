#version 300 es
precision highp float;

// Attributes
in vec3 position;
in vec2 uv;

// Varying
out vec2 vUV;
out float vFragmentDepth;

// Uniforms
uniform mat4 worldViewProjection;
//uniform float logarithmicDepthConstant;

void main() {
    gl_Position = worldViewProjection * vec4(position, 1.0);

    vUV = uv;
    vFragmentDepth = 1.0 + gl_Position.w;
//    gl_Position.z = log2(max(0.000001, vFragmentDepth)) * 1.0;
}
