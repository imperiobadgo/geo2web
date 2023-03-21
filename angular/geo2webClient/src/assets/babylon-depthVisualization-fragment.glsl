precision highp float;

// Samplers
varying vec2 vUV;
uniform sampler2D mainRenderTexture;
uniform sampler2D mainDepthTexture;

void main() {
    vec3 depth = vec3(texture(mainDepthTexture, vUV));
//    gl_FragColor = texture(mainRenderTexture, vUV);
    if (vUV.x < 0.5){
        gl_FragColor = texture(mainRenderTexture, vUV);
    }
    else{
        gl_FragColor = vec4(depth.x, depth.x, depth.x, 1.0);
    }

}
