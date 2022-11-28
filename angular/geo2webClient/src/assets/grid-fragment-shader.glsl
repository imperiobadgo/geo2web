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

    //    float det = -dot(lineDirection, planeNormal);
    //    if (abs(det) < 0.01) {
    //        return -1.0;
    //    }
    //    float t = 1.0 / det * dot(planeNormal, lineOrigin - planeOrigin);
    //    return t;
    //    float denominator = dot((planeOrigin - lineOrigin), planeNormal);
    //    float nominator = dot(lineDirection, planeNormal);
    //    return denominator / nominator;
}

float IntersectPlane(vec3 rayOrigin, vec3 rayDirection, vec3 p0, vec3 normal)
{
    vec3 D = rayDirection;
    vec3 N = normal;
    float param = dot(p0 - rayOrigin, N) / dot(D, N);
    return param;
    //    vec3 X = rayOrigin + D * param;
    //
    //    return X;
}

float projectPointOnLine(vec3 lineOrigin, vec3 lineDirection, vec3 position) {
    vec3 v = position - lineOrigin;
    float lengthSquared = lineDirection.x * lineDirection.x + lineDirection.y * lineDirection.y;
    return dot(lineDirection, v) / lengthSquared;
}

float sdPlane(vec3 p, vec3 n, float h)
{
    // n must be normalized
    return dot(p, n) + h;
}

void main() {
    vec3 origin = vec3(0.0, 0.0, 0.0);
    vec3 xAxis = vec3(0.01, 0.0, 0.0);
    vec3 yAxis = vec3(0.0, 0.01, 0.0);
    vec3 zAxis = vec3(0.0, 0.0, 0.01);

    //    gl_FragColor = vec4(vUv.x, vUv.y, 0.0, 0.5);

    float pixelScreenX = 2.0 * vUv.x - 1.0;
    float pixelScreenY = 2.0 * vUv.y - 1.0;

    //    gl_FragColor = vec4(pixelScreenX, pixelScreenY, 0.0, 0.5);

    //is automatically the ray because the camera is at the origin in the camera coordinate system.
    vec4 cameraSpaceOrigin = vec4(pixelScreenX, pixelScreenY, -1.0, 1.0);

    //    gl_FragColor = vec4(cameraSpaceRay.x, cameraSpaceRay.y, cameraSpaceRay.z, 0.5);

    vec4 worldSpaceOrigin4 = cameraSpaceOrigin * inverseCameraWorld;
    //        gl_FragColor = vec4(worldSpaceOrigin4.x, worldSpaceOrigin4.y, worldSpaceOrigin4.z, 0.5);
    vec3 worldSpaceOrigin = worldSpaceOrigin4.xyz;
    //    gl_FragColor = vec4(worldSpaceOrigin.xyz, 0.8);
    //direction is in ndc just (0,0,1)
    vec4 worldSpaceRay4_intermediate = vec4(0.0, 0.0, 1.0, 0.0) * inverseCameraWorld;

    //        gl_FragColor = vec4(worldSpaceRay4_intermediate.xyz, 0.7);

    vec4 worldSpaceRay4 = worldSpaceRay4_intermediate / worldSpaceRay4_intermediate.w;


    vec3 worldSpaceRay = normalize(worldSpaceRay4.xyz);
    //    gl_FragColor = vec4(worldSpaceRay.xyz, 1.0);
    vec3 comb = worldSpaceRay4_intermediate.xyz + worldSpaceOrigin / 10.0;
    //        gl_FragColor = vec4(comb.x, comb.y, comb.z, 0.8);

    //    gl_FragColor = vec4(worldSpaceRay.x, 0.0, 0.0, 0.5);
    float intersectionParam = IntersectPlane(worldSpaceOrigin, worldSpaceRay4_intermediate.xyz, origin, zAxis);

    //    gl_FragColor = vec4(intersectionParam / 10.0, 0.0, 0.0, 0.8);

    vec3 intersection = worldSpaceOrigin + worldSpaceRay4_intermediate.xyz * intersectionParam;

    //        gl_FragColor = vec4(intersection.xyz / 10.0, 0.7);

    float xPos = projectPointOnLine(origin, xAxis, intersection);
    float yPos = projectPointOnLine(origin, yAxis, intersection);

    //        gl_FragColor = vec4(xPos / 10.0, yPos / 10.0, 0.0, 0.8);

    vec3 planePosition = origin + + xAxis * xPos + yAxis * yPos;// + xAxis * xPos;// + yAxis * yPos;
//        gl_FragColor = vec4(planePosition.xyz / 10.0, 0.5);
    //        gl_FragColor = vec4(intersectionParam, intersectionParam, intersectionParam, 0.5);


    //        float xPos = gl_FragCoord.x;
    //        float yPos = gl_FragCoord.y;


    float vpw = 50.0;
    float vph = 50.0;
    vec2 offset = vec2(0.0, 0.0);
    vec2 pitch = vec2(5.0, 5.0);

    float lX = xPos / vpw;
    float lY = yPos / vph;

    float scaleFactor = 10000.0;

    float offX = (scaleFactor * offset.x) + xPos;
    float offY = (scaleFactor * offset.y) + (1.0 - yPos);

//    if (int(mod(offX, pitch.x)) == 0 ||
//    int(mod(offY, pitch.y)) == 0) {
//        gl_FragColor = vec4(0.0, 0.0, 0.0, 0.5);
//    } else {
//        gl_FragColor = vec4(1.0, 1.0, 1.0, 0.0);
//    }


    float xResult = sin(xPos * 0.2) * 10.0 + 10.0;
    if (floor(xResult) == floor(yPos))
    {
        gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
    }
    else
    {
        gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);// vColor;
    }
}
