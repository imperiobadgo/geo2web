uniform mat4 inverseCameraWorld;
uniform vec2 screenSize;
uniform float fov;

//varying vec2 texCoords;
varying vec2 vUv;

float projectLineWithPlaneGetLineParam(vec3 lineOrigin, vec3 lineDirection, vec3 planeOrigin, vec3 planeNormal) {
    float rechteSeiteEbene = dot(planeOrigin, planeNormal);
    float linkeSeiteProdukt = dot(planeNormal, lineOrigin);
    float vorLambda = dot(planeNormal, lineDirection);
    return (rechteSeiteEbene - linkeSeiteProdukt) / vorLambda;
}

float projectPointOnLine(vec3 lineOrigin, vec3 lineDirection, vec3 position) {
    vec3 v = position - lineOrigin;
    float lengthSquared = lineDirection.x * lineDirection.x + lineDirection.y * lineDirection.y;
    return dot(lineDirection, v) / lengthSquared;
}

void main() {
    vec3 origin = vec3(0.0, 0.0, 0.0);
    vec3 xAxis = vec3(1.0, 0.0, 0.0);
    vec3 yAxis = vec3(0.0, 0.0, 1.0);
    vec3 zAxis = vec3(0.0, -1.0, 0.0);

    //    gl_FragColor = vec4(vUv.x, vUv.y, -1.0, 0.5);

    float pixelScreenX = 2.0 * vUv.x - 1.0;
    float pixelScreenY = 2.0 * vUv.y - 1.0;

    float imageAspectRatio = screenSize.x / screenSize.y;

    float pixelCameraX = (2.0 * pixelScreenX - 1.0) * imageAspectRatio * tan(fov / 2.0);
    float pixelCameraY = (1.0 - 2.0 * pixelScreenY) * tan(fov / 2.0);

    //is automatically the ray because the camera is at the origin in the camera coordinate system.
    vec4 cameraSpaceRay = vec4(pixelScreenX, pixelScreenY, -1.0, 1.0);

    //    gl_FragColor = vec4(cameraSpaceRay.x, cameraSpaceRay.y, cameraSpaceRay.z, 0.5);

    vec4 worldSpaceRay4 = inverseCameraWorld * cameraSpaceRay;
    //    gl_FragColor = vec4(worldSpaceRay4.x, worldSpaceRay4.y, worldSpaceRay4.z, 0.5);
    vec3 worldSpaceRay = worldSpaceRay4.xyz;

    float intersectionParam = projectLineWithPlaneGetLineParam(cameraPosition, worldSpaceRay, origin, zAxis);

    vec3 intersection = cameraPosition + worldSpaceRay * intersectionParam;

    float xPos = projectPointOnLine(origin, xAxis, intersection);
    float yPos = projectPointOnLine(origin, yAxis, intersection);

    gl_FragColor = vec4(xPos, yPos, 0.0, 0.5);

    //
    ////    float xPos = gl_FragCoord.x;
    ////    float yPos = gl_FragCoord.y;
    //
    //
    //    float vpw = 50.0;
    //    float vph = 50.0;
    //    vec2 offset = vec2(0.0, 0.0);
    //    vec2 pitch = vec2(50.0, 50.0);
    //
    //    float lX = xPos / vpw;
    //    float lY = yPos / vph;
    //
    //    float scaleFactor = 10000.0;
    //
    //    float offX = (scaleFactor * offset[0]) + xPos;
    //    float offY = (scaleFactor * offset[1]) + (1.0 - yPos);
    //
    //    if (int(mod(offX, pitch[0])) == 0 ||
    //    int(mod(offY, pitch[1])) == 0) {
    //        gl_FragColor = vec4(0.0, 0.0, 0.0, 0.5);
    //    } else {
    //        gl_FragColor = vec4(1.0, 1.0, 1.0, 0.0);
    //    }
}
