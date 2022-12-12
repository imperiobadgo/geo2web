precision highp float;
precision highp int;

layout (location = 0) out vec4 pc_FragColor;

in vec2 vUv;

uniform sampler2D tDiffuse;
uniform sampler2D tTest;

void main() {

    vec3 diffuse = texture(tDiffuse, vUv).rgb;
    vec3 test = texture(tTest, vUv).rgb;

    pc_FragColor.rgb = mix(diffuse, test, step(0.5, vUv.x));
    pc_FragColor.a = 1.0;

}
