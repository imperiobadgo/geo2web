precision highp float;
precision highp int;

layout(location = 0) out vec4 pc_FragColor;
layout (location = 1) out vec4 pc_test;

in vec2 vUv;

uniform mat4 inverseCameraWorld;
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
    vec3 origin = vec3(0.0, 0.0, 0.0);
    vec3 xAxis = vec3(0.01, 0.0, 0.0);
    vec3 yAxis = vec3(0.0, 0.01, 0.0);
    vec3 zAxis = vec3(0.0, 0.0, 0.01);

    float imageAspectRatio = screenSize.x / screenSize.y;

    float pixelScreenX = (2.0 * vUv.x - 1.0) * imageAspectRatio;
    float pixelScreenY = (2.0 * vUv.y - 1.0);

    //is automatically the ray origin because the camera is at the origin in the camera coordinate system.
    vec4 cameraSpaceOrigin = vec4(pixelScreenX, pixelScreenY, -1.0, 1.0);

    vec4 worldSpaceOrigin4 = cameraSpaceOrigin * inverseCameraWorld;
    vec3 worldSpaceOrigin = worldSpaceOrigin4.xyz;

    //direction is in ndc just (0,0,1)
    vec4 worldSpaceRay4 = vec4(0.0, 0.0, 1.0, 1.0) * inverseCameraWorld;
    vec3 worldSpaceRay = worldSpaceRay4.xyz;

    float intersectionParam = projectLineWithPlaneGetLineParam(worldSpaceOrigin, worldSpaceRay, origin, zAxis);
    vec3 intersection = worldSpaceOrigin + worldSpaceRay * intersectionParam;

    float xPos = projectPointOnLine(origin, xAxis, intersection);
    float yPos = projectPointOnLine(origin, yAxis, intersection);

    float xResult = sin(xPos * 0.2) * 20.0 + 20.0;
    if (floor(xResult) == floor(yPos))
    {
        pc_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
        pc_test = vec4(0.0, 1.0, 0.0, 1.0);
    }
    else
    {
        pc_FragColor = vec4(0.0, 0.0, 0.0, 0.0);// vColor;
        pc_test = vec4(0.0, 0.0, 0.0, 0.0);
    }
}
