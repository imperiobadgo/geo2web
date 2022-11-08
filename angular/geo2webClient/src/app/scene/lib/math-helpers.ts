//source: https://github.com/ndesmic/geogl/blob/v8/js/lib/math-helpers.js

export const TWO_PI = Math.PI * 2
export const QUARTER_TURN = Math.PI / 2;
export const degreesPerRad = 180 / Math.PI;

export function normalizeAngle(angle: number) {
  if (angle < 0) {
    return TWO_PI - (Math.abs(angle) % TWO_PI);
  }
  return angle % TWO_PI;
}

export function radToDegrees(rad: number) {
  return rad * 180 / Math.PI;
}

export function cartesianToLatLng([x, y, z]: any) {
  const radius = Math.sqrt(x ** 2 + y ** 2 + z ** 2);
  return [
    radius,
    (Math.PI / 2) - Math.acos(y / radius),
    normalizeAngle(Math.atan2(x, -z)),
  ];
}

export function latLngToCartesian([radius, lat, lng]: any) {
  lng = -lng + Math.PI / 2;
  return [
    radius * Math.cos(lat) * Math.cos(lng),
    radius * Math.sin(lat),
    radius * -Math.cos(lat) * Math.sin(lng),
  ];
}

export function clamp(value: number, low: number, high: number) {
  low = low !== undefined ? low : Number.MIN_SAFE_INTEGER;
  high = high !== undefined ? high : Number.MAX_SAFE_INTEGER;
  if (value < low) {
    value = low;
  }
  if (value > high) {
    value = high;
  }
  return value;
}

export function lerp(start: number, end: number, normalValue: number) {
  return start + (end - start) * normalValue;
}

export function inverseLerp(start: number, end: number, value: number) {
  return (value - start) / (end - start);
}

export function normalizeNumber(num: number, len: number) {
  num = parseFloat(num.toFixed(len));
  num = num === -0 ? 0 : num;
  return num;
}
