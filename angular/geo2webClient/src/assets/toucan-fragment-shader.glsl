precision highp float;
uniform vec2 screenResolution;

varying lowp vec4 vColor;
void main(void) {
    float x = gl_FragCoord.x;
    float y = gl_FragCoord.y;
    float xResult = sin(x * 0.02) * 150.0;// + sin(x * 0.05) * 15.0;
    if (floor(xResult + screenResolution.y / 2.0) == floor(y))
    {
        gl_FragColor = vec4(1.0,0.0,0.0,1.0);
    }
    else
    {
        gl_FragColor = vec4(0.0,0.0,0.0,1.0);// vColor;
    }
}
