#version 300 es
precision highp float;
precision highp int;

layout (location = 0) out vec4 pc_FragColor;
layout (location = 1) out vec4 pc_test;

in vec2 fragmentPosition;

//uniforms
uniform mat4 clip2camera;
uniform mat4 camera2world;
//uniform mat4 modelViewMatrix;


float projectLineWithPlaneGetLineParam(vec3 lineOrigin, vec3 lineDirection, vec3 planeOrigin, vec3 planeNormal) {
    float rechteSeiteEbene = dot(planeOrigin, planeNormal);
    float linkeSeiteProdukt = dot(planeNormal, lineOrigin);
    float vorLambda = dot(planeNormal, lineDirection);
    return (rechteSeiteEbene - linkeSeiteProdukt) / vorLambda;
}

float projectPointOnLine(vec3 lineOrigin, vec3 lineDirection, vec3 position) {
    vec3 v = position - lineOrigin;
    float lengthSquared = lineDirection.x * lineDirection.x + lineDirection.y * lineDirection.y + lineDirection.z * lineDirection.z;
    return dot(lineDirection, v) / lengthSquared;
}

void main() {
    mat4 position = mat4(1.0);// modelViewMatrix;
    vec3 xAxis = position[0].xyz;
    vec3 yAxis = position[1].xyz;
    vec3 zAxis = position[2].xyz;
    vec3 origin = position[3].xyz;

    vec4 worldPosition0 = camera2world * clip2camera * vec4(fragmentPosition.xy, -1.0, 1.0);
    vec4 worldPosition1 = camera2world * clip2camera * vec4(fragmentPosition.xy, 1.0, 1.0);

    vec3 cameraPos0 = worldPosition0.xyz / worldPosition0.w;
    vec3 cameraPos1 = worldPosition1.xyz / worldPosition1.w;

    vec3 cameraRay = cameraPos1 - cameraPos0;

    float intersectionParam = projectLineWithPlaneGetLineParam(cameraPos0, cameraRay, origin, zAxis);
    if (intersectionParam < 0.0)
    {
        //prevent intersection of the backward camera ray...
        pc_FragColor = vec4(0.0, 0.0, 0.0, 0.0);
        pc_test = vec4(0.0, 0.0, 0.0, 0.0);
        return;
    }

    vec3 intersection = cameraPos0 + cameraRay * intersectionParam;

    float xPos = projectPointOnLine(origin, xAxis, intersection);
    float yPos = projectPointOnLine(origin, yAxis, intersection);

    //        float xPos = gl_FragCoord.x;
    //        float yPos = gl_FragCoord.y;


    vec2 pitch = vec2(30.0, 30.0);
    float size = 80.0;

    //    if (int(xPos * size) == 0 ||
    //    int(yPos * size) == 0) {
    //        pc_FragColor = vec4(0.0, 0.0, 0.0, 0.5);
    //        pc_test = vec4(1.0, 0.0, 0.0, 0.5);
    //    } else {
    //        pc_FragColor = vec4(1.0, 1.0, 1.0, 0.0);
    //        pc_test = vec4(1.0, 1.0, 1.0, 0.0);
    //    }

    if (int(mod(xPos * size, pitch.x)) == 0 ||
    int(mod(yPos * size, pitch.y)) == 0) {
        pc_FragColor = vec4(0.0, 0.0, 0.0, 0.5);
        pc_test = vec4(1.0, 0.0, 0.0, 0.5);
    } else {
        pc_FragColor = vec4(1.0, 1.0, 1.0, 0.0);
        pc_test = vec4(1.0, 1.0, 1.0, 0.0);
    }

    //    float xResult = sin(xPos * 0.2) * 10.0 + 10.0;
    //    if (floor(xResult) == floor(yPos))
    //    {
    //        gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
    //    }
    //    else
    //    {
    //        gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);// vColor;
    //    }
    gl_FragDepth = 0.0;
}

