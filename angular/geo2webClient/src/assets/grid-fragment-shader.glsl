precision highp float;
precision highp int;

layout (location = 0) out vec4 pc_FragColor;
layout (location = 1) out vec4 pc_test;

in vec2 vUv;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform vec2 screenSize;

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
    mat4 position = modelViewMatrix;
    vec3 xAxis = position[0].xyz;
    vec3 yAxis = position[1].xyz;
    vec3 zAxis = position[2].xyz;
    vec3 origin = position[3].xyz;
//    vec3 origin = vec3(0.0,0.0,0.0);// position[3].xyz;


    //    pc_FragColor = vec4(xAxis, 0.5);
    //    pc_test = pc_FragColor;

    float imageAspectRatio = screenSize.x / screenSize.y;

    float pixelScreenX = (2.0 * vUv.x - 1.0) * imageAspectRatio;
    float pixelScreenY = (2.0 * vUv.y - 1.0);

    //is automatically the ray origin because the camera is at the origin in the camera coordinate system.
    vec4 cameraSpaceOrigin = vec4(pixelScreenX, pixelScreenY, -1.0, 1.0);

    vec4 worldSpaceOrigin4 = cameraSpaceOrigin;// * viewMatrix;
    vec3 worldSpaceOrigin = worldSpaceOrigin4.xyz;

    //direction is in ndc just (0,0,1)
    vec4 worldSpaceRay4 = vec4(0.0, 0.0, 1.0, 1.0);// * viewMatrix;
    vec3 worldSpaceRay = worldSpaceRay4.xyz;

    //    pc_FragColor = vec4(origin, 0.5);
    //    pc_test = pc_FragColor;
    float intersectionParam = projectLineWithPlaneGetLineParam(worldSpaceOrigin, worldSpaceRay, origin, yAxis);
    vec4 intersection = (worldSpaceOrigin4 + worldSpaceRay4 * intersectionParam);

    float xPos = projectPointOnLine(origin, xAxis, intersection.xyz);
    float yPos = projectPointOnLine(origin, zAxis, intersection.xyz);

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
}
