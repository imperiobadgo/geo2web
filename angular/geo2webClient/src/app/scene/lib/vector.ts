//source: https://github.com/ndesmic/geogl/blob/v8/js/lib/vector.js

export function transpose(matrix: any) {
  const result = [];
  for (let row = 0; row < matrix.length; row++) {
    const newRow = [];
    for (let col = 0; col < matrix[row].length; col++) {
      newRow.push(matrix[col][row]);
    }
    result.push(newRow);
  }
  return result;
}

export function getRotationXMatrix(theta: any) {
  return [
    [1, 0, 0, 0],
    [0, Math.cos(theta), -Math.sin(theta), 0],
    [0, Math.sin(theta), Math.cos(theta), 0],
    [0, 0, 0, 1]
  ];
}

export function getRotationYMatrix(theta: any) {
  return [
    [Math.cos(theta), 0, Math.sin(theta), 0],
    [0, 1, 0, 0],
    [-Math.sin(theta), 0, Math.cos(theta), 0],
    [0, 0, 0, 1]
  ];
}

export function getRotationZMatrix(theta: any) {
  return [
    [Math.cos(theta), -Math.sin(theta), 0, 0],
    [Math.sin(theta), Math.cos(theta), 0, 0],
    [0, 0, 1, 0],
    [0, 0, 0, 1]
  ];
}

export function getTranslationMatrix(x: any, y: any, z: any) {
  return [
    [1, 0, 0, x],
    [0, 1, 0, y],
    [0, 0, 1, z],
    [0, 0, 0, 1]
  ];
}

export function getScaleMatrix(x: any, y: any, z: any) {
  return [
    [x, 0, 0, 0],
    [0, y, 0, 0],
    [0, 0, z, 0],
    [0, 0, 0, 1]
  ];
}

export function getIdentityMatrix() {
  return [
    [1, 0, 0, 0],
    [0, 1, 0, 0],
    [0, 0, 1, 0],
    [0, 0, 0, 1]
  ];
}

export function multiplyMatrixVector(vector: any, matrix: any) {
  //normalize 3 vectors
  if (vector.length === 3) {
    vector.push(1);
  }

  return [
    vector[0] * matrix[0][0] + vector[1] * matrix[0][1] + vector[2] * matrix[0][2] + vector[3] * matrix[0][3],
    vector[0] * matrix[1][0] + vector[1] * matrix[1][1] + vector[2] * matrix[1][2] + vector[3] * matrix[1][3],
    vector[0] * matrix[2][0] + vector[1] * matrix[2][1] + vector[2] * matrix[2][2] + vector[3] * matrix[2][3],
    vector[0] * matrix[3][0] + vector[1] * matrix[3][1] + vector[2] * matrix[3][2] + vector[3] * matrix[3][3]
  ];
}

export function getVectorMagnitude(vec: any) {
  let sum = 0;
  for (const el of vec) {
    sum += el ** 2;
  }
  return Math.sqrt(sum);
}

export function addVector(a: any, b: any) {
  return [
    a[0] + b[0],
    a[1] + b[1],
    a[2] + b[2]
  ];
}

export function subtractVector(a: any, b: any) {
  return a.map((x: any, i: any) => x - b[i]);
}

export function scaleVector(vec: any, s: any) {
  return vec.map((x: any) => x * s);
}

export function divideVector(vec: any, s: any) {
  return [
    vec[0] / s,
    vec[1] / s,
    vec[2] / s
  ];
}

export function normalizeVector(vec: any) {
  return divideVector(vec, getVectorMagnitude(vec));
}

export function crossVector(a: any, b: any) {
  return [
    a[1] * b[2] - a[2] * b[1],
    a[2] * b[0] - a[0] * b[2],
    a[0] * b[1] - a[1] * b[0]
  ];
}

/**
 *vectors must have same length!
 */
export function dotVector(a: any, b: any) {
  let result = 0;
  for (let i = 0; i < a.length; i++) {
    result += a[i] * b[i];
  }
  return result;
}

export function invertVector(vec: any) {
  return vec.map((x: any) => -x);
}

export function reflectVector(vec: any, normal: any) {
  return [
    vec[0] - 2 * dotVector(vec, normal) * normal[0],
    vec[1] - 2 * dotVector(vec, normal) * normal[1],
    vec[2] - 2 * dotVector(vec, normal) * normal[2],
  ];
}

export const UP = [0, 1, 0];
export const FORWARD = [0, 0, 1];
export const RIGHT = [1, 0, 0];

///
export function getVectorIntersectPlane(planePoint: any, planeNormal: any, lineStart: any, lineEnd: any) {
  planeNormal = normalizeVector(planeNormal);
  const planeDot = dotVector(planePoint, planeNormal);
  const startDot = dotVector(lineStart, planeNormal);
  const endDot = dotVector(lineEnd, planeNormal);
  const t = (planeDot - startDot) / (endDot - startDot);
  if (t === Infinity || t === -Infinity) {
    return null;
  }
  const line = subtractVector(lineEnd, lineStart);
  const deltaToIntersect = scaleVector(line, t);
  return addVector(lineStart, deltaToIntersect);
}

export function isPointInInsideSpace(point: any, planeNormal: any, planePoint: any) {
  planeNormal = normalizeVector(planeNormal);
  return dotVector(planeNormal, subtractVector(planePoint, point)) > 0;
}

//Matrices

export function getColumn(matrix: any, col: number) {
  const result = [];
  for (let row = 0; row < matrix.length; row++) {
    result.push(matrix[row][col]);
  }
  return result;
}

//A's rows must equal B's columns, no check is given
export function multiplyMatrix(a: any, b: any) {
  const result = [];
  for (let row = 0; row < a.length; row++) {
    const newRow = [];
    for (let col = 0; col < b[row].length; col++) {
      newRow.push(dotVector(a[row], getColumn(b, col)));
    }
    result.push(newRow);
  }

  return result;
}

export function getProjectionMatrix(screenHeight: number, screenWidth: number, fieldOfView: number, zNear: number, zFar: number) {
  const aspectRatio = screenHeight / screenWidth;
  const fieldOfViewRadians = fieldOfView * (Math.PI / 180);
  const fovRatio = 1 / Math.tan(fieldOfViewRadians / 2);

  return [
    [aspectRatio * fovRatio, 0, 0, 0],
    [0, fovRatio, 0, 0],
    [0, 0, -zFar / (zFar - zNear), -1],
    [0, 0, (-zFar * zNear) / (zFar - zNear), 0]
  ];
}

export function getOrthoMatrix(left: number, right: number, bottom: number, top: number, near: number, far: number) {
  return [
    [2 / (right - left), 0, 0, -(right + left) / (right - left)],
    [0, 2 / (top - bottom), 0, -(top + bottom) / (top - bottom)],
    [0, 0, -2 / (near - far), -(near + far) / (near - far)],
    [0, 0, 0, 1]
  ];
}

export function getPointAtMatrix(position: any, target: any, up: any) {
  const forward = normalizeVector(subtractVector(target, position));
  const newUp = normalizeVector(subtractVector(up, scaleVector(forward, dotVector(up, forward))));
  const right = crossVector(newUp, forward);

  return [
    [right[0], right[1], right[2], 0],
    [newUp[0], newUp[1], newUp[2], 0],
    [forward[0], forward[1], forward[2], 0],
    [position[0], position[1], position[2], 1]
  ];
}

export function getLookAtMatrix(position: any, target: any, up: any) {
  const forward = normalizeVector(subtractVector(target, position));
  const newUp = normalizeVector(subtractVector(up, scaleVector(forward, dotVector(up, forward))));
  const right = crossVector(newUp, forward);

  return [
    [right[0], newUp[0], forward[0], 0],
    [right[1], newUp[1], forward[1], 0],
    [right[2], newUp[2], forward[2], 0],
    [-dotVector(position, right), -dotVector(position, newUp), -dotVector(position, forward), 1]
  ];
}

export function getDeterminantSubmatrix(matrix: any, row: number, col: number) {
  const result = [];
  for (let i = 0; i < matrix.length; i++) {
    const newRow = [];
    if (i === row) continue;
    for (let j = 0; j < matrix[i].length; j++) {
      if (j === col) continue;
      newRow.push(matrix[i][j]);
    }
    result.push(newRow);
  }
  return result;
}

export function getDeterminant(matrix: any) {
  let result = 0;

  if (matrix.length === 2 && matrix[0].length === 2) return (matrix[0][0] * matrix[1][1]) - (matrix[0][1] * matrix[1][0]);

  for (let i = 0; i < matrix[0].length; i++) {
    if (i % 2 === 0) {
      result += matrix[0][i] * getDeterminant(getDeterminantSubmatrix(matrix, 0, i));
    } else {
      result -= matrix[0][i] * getDeterminant(getDeterminantSubmatrix(matrix, 0, i));
    }
  }

  return result;
}

export function getCofactor(matrix: any, row: number, col: number) {
  const determinant = getDeterminant(getDeterminantSubmatrix(matrix, row, col));
  return (row + col) % 2 === 1
    ? -determinant
    : determinant;
}

export function getCofactorMatrix(matrix: any) {
  const result = [];
  for (let row = 0; row < matrix.length; row++) {
    const newRow = [];
    for (let col = 0; col < matrix[row].length; col++) {
      newRow.push(getCofactor(matrix, row, col));
    }
    result.push(newRow);
  }
  return result;
}

export function getAdjugate(matrix: any) {
  return transpose(getCofactorMatrix(matrix));
}

export function scaleMatrix(matrix: any, s: number) {
  const result = [];
  for (let row = 0; row < matrix.length; row++) {
    const newRow = [];
    for (let col = 0; col < matrix[row].length; col++) {
      newRow.push(matrix[row][col] * s);
    }
    result.push(newRow);
  }
  return result;
}

export function getInverse(matrix: any) {
  return scaleMatrix(getAdjugate(matrix), 1 / getDeterminant(matrix));
}

export function trimMatrix(matrix: any, height: number, width: number) {
  const result = [];
  for (let row = 0; row < height; row++) {
    const newRow = [];
    for (let col = 0; col < width; col++) {
      newRow.push(matrix[row][col]);
    }
    result.push(newRow);
  }
  return result;
}

export function asMatrix(array: any, height: number, width: number) {
  const result = [];
  for (let row = 0; row < height; row++) {
    const newRow = [];
    for (let col = 0; col < width; col++) {
      newRow.push(array[row * width + col]);
    }
    result.push(newRow);
  }
  return result;
}
