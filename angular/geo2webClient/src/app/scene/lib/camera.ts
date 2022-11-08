//source: https://github.com/ndesmic/geogl/blob/v8/js/entity/camera.js

import {getOrthoMatrix, getProjectionMatrix, getLookAtMatrix, UP, subtractVector} from './vector';
import {cartesianToLatLng, latLngToCartesian, clamp} from "./math-helpers";

export class Camera {
  private position = [0, 0, -1];
  private target = [0, 0, 0];
  private screenWidth: number;
  private screenHeight;
  private near;
  private far;
  private left;
  private right;
  private top;
  private bottom;
  private fieldOfView;
  private isPerspective;

  constructor(camera: any) {
    this.position = camera.position;
    this.screenWidth = camera.screenWidth;
    this.screenHeight = camera.screenHeight;
    this.left = camera.left;
    this.right = camera.right;
    this.top = camera.top;
    this.bottom = camera.bottom;
    this.near = camera.near;
    this.far = camera.far;
    this.fieldOfView = camera.fieldOfView;
    this.isPerspective = camera.isPerspective;

    if (this.isPerspective && (this.screenWidth === undefined || this.screenHeight === undefined || this.near === undefined || this.far === undefined || this.fieldOfView === undefined)) {
      throw new Error(`Missing required value for perspective projection`);
    }
    if (!this.isPerspective && (this.left === undefined || this.right === undefined || this.near === undefined || this.far === undefined || this.top === undefined || this.bottom === undefined)) {
      throw new Error(`Missing required value for ortho projection`);
    }
  }

  setSize(screenWidth: number, screenHeight: number) {
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
  }

  setClippingPlanes(near: number, far: number){
    this.near = near;
    this.far = far;
  }

  setDimensions(left: number, right: number, top: number, bottom: number){
    this.left = left;
    this.right = right;
    this.top = top;
    this.bottom = bottom;
  }

  setFieldOfView(fov: number){
    this.fieldOfView = fov;
  }

  setIsPerspective(isPerspective: number){
    this.isPerspective = isPerspective;
  }

  moveTo(x: number, y: number, z: number) {
    this.position = [x, y, z];
  }

  moveBy({x = 0, y = 0, z = 0}) {
    this.position[0] += x;
    this.position[1] += y;
    this.position[2] += z;
  }

  panBy({x = 0, y = 0, z = 0}) {
    this.position[0] += x;
    this.target[0] += x;
    this.position[1] += y;
    this.target[1] += y;
    this.position[2] += z;
    this.target[2] += z;
  }

  orbitBy({lat = 0, long = 0, radius = 0}) {
    const [r, currentLat, currentLng] = this.getOrbit();
    const newLat = clamp(currentLat + lat, -Math.PI / 2, Math.PI / 2);
    const newRadius = Math.max(0.1, r + radius);
    this.position = latLngToCartesian([newRadius, newLat, currentLng - long]);
  }

  zoomBy(value: number) {
    const [r, currentLat, currentLng] = this.getOrbit();
    const newRadius = Math.max(0.1, r / value);
    this.position = latLngToCartesian([newRadius, currentLat, currentLng]);
  }

  lookAt(x: number, y: number, z: number) {
    this.target = [x, y, z];
  }

  getOrbit() {
    const targetDelta = subtractVector(this.position, this.target);
    return cartesianToLatLng(targetDelta);
  }

  getViewMatrix() {
    return getLookAtMatrix(this.position, this.target, UP).flat();
  }

  getProjectionMatrix() {
    return this.isPerspective
      ? getProjectionMatrix(this.screenHeight, this.screenWidth, this.fieldOfView, this.near, this.far).flat()
      : getOrthoMatrix(this.left, this.right, this.bottom, this.top, this.near, this.far).flat();
  }

  getFieldOfView() {
    return this.fieldOfView;
  }

  getPosition() {
    return this.position;
  }

  setPosition(position: any) {
    this.position = position;
  }
}
