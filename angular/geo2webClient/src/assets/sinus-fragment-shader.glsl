precision highp float;
precision highp int;

layout (location = 0) out vec4 pc_FragColor;
layout (location = 1) out vec4 pc_test;

in vec2 fragmentPosition;

uniform mat4 clip2camera;
uniform mat4 camera2world;


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

float f(float x)
{
    return sin(x) * 5.0;
}

float f_dx(float x)
{
    float epsilon = 0.00001;
    float y0 = f(x - epsilon);
    float y1 = f(x + epsilon);
    return (y1 - y0) / 2.0 * epsilon;
}

float f_dx2(float x)
{
    float epsilon = 0.00001;
    float y0 = f_dx(x - epsilon);
    float y1 = f_dx(x + epsilon);
    return (y1 - y0) / 2.0 * epsilon;
}

float error(float x, vec2 planeIntersection)
{
    vec2 current = vec2(x, f(x));
    vec2 delta = current - planeIntersection;
    return dot(delta, delta); //squared length
}

float error_dx(float x, vec2 planeIntersection)
{
    vec2 current = vec2(x, f(x));
    vec2 current_dx = vec2(1, f_dx(x));
    vec2 delta = current - planeIntersection;
    vec2 delta_dx = current_dx;
    return 2.0 * dot(delta, delta_dx);
}

float error_dx2(float x, vec2 planeIntersection)
{
    vec2 current = vec2(x, f(x));
    vec2 current_dx = vec2(1, f_dx(x));
    vec2 delta = current - planeIntersection;
    vec2 delta_dx = current_dx;
    vec2 current_dx2 = vec2(0, f_dx2(x));
    vec2 delta_dx2 = current_dx2;
    return 2.0 * dot(delta, delta_dx2) + 2.0 * dot(delta_dx, delta_dx);
}

float newton(float x, vec2 planeIntersection)
{
    for (int i = 0; i < 5; i++) {
        x -= error_dx(x, planeIntersection) / error_dx2(x, planeIntersection);
    }
    return x;
}

void main() {
    vec3 origin = vec3(0.0, 0.0, 0.0);
    vec3 xAxis = vec3(1.0, 0.0, 0.0);
    vec3 yAxis = vec3(0.0, 1.0, 0.0);
    vec3 zAxis = vec3(0.0, 0.0, 1.0);

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


    float xResult = f(xPos);// newton(xPos, vec2(xPos, yPos));

    float yDistance = xResult - yPos;

    float yDistanceFWidth = fwidth(yDistance);
    float fWidthMax = 1.0;
    if (yDistanceFWidth > fWidthMax)
    {
        yDistanceFWidth = fWidthMax;
    }
    float lineSize = 1.0;
    float smoothBla = 0.5;

    float lineAlpha = smoothstep(lineSize + smoothBla, lineSize - smoothBla, abs(yDistance) / yDistanceFWidth);

    if (lineAlpha != 0.0)
    {
        pc_FragColor = vec4(1.0, 0.0, 0.0, lineAlpha);
        pc_test = vec4(0.0, 1.0, 0.0, lineAlpha);
    }
    else
    {
        //Draw nothing
        pc_FragColor = vec4(0.0, 0.0, 0.0, 0.0);
        pc_test = vec4(0.0, 0.0, 0.0, 0.0);
    }
}
