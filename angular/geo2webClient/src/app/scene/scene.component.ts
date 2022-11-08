import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {WebGLService} from "./services/web-gl.service";
import {interval} from 'rxjs';
import * as matrix from 'gl-matrix';
import {degreesPerRad} from "./lib/math-helpers";

@Component({
  selector: 'app-scene',
  templateUrl: './scene.component.html',
  styleUrls: ['./scene.component.scss']
})
export class SceneComponent implements OnInit, AfterViewInit {
  @ViewChild('sceneCanvas') private canvas: ElementRef<HTMLCanvasElement> | undefined;

  /**
   * The interval of refresh rate for drawing our scene during one second of elapsed time (1000ms).
   */
  private _60fpsInterval = 16.666666666666666667;
  private gl: WebGLRenderingContext | undefined | null

  private cubeRotation = 0;
  private deltaTime = 0;
  private initialPosition: any;
  private initialCameraPosition: any;

  constructor(private webglService: WebGLService) {
  }

  ngAfterViewInit(): void {
    if (!this.canvas) {
      alert("canvas not supplied! cannot bind WebGL context!");
      return;
    }
    this.gl = this.webglService.initializeWebGLContext(this.canvas.nativeElement);

    this.canvas.nativeElement.addEventListener("keydown", this.onKeyDown);
    this.canvas.nativeElement.addEventListener("pointerdown", this.onPointerDown);
    this.canvas.nativeElement.addEventListener("wheel", this.onWheel);

    // Set up to draw the scene periodically via deltaTime.
    const milliseconds = 0.001;
    this.deltaTime = this._60fpsInterval * milliseconds;

    // Set up to draw the scene periodically.
    const drawSceneInterval = interval(this._60fpsInterval);
    drawSceneInterval.subscribe(() => {
      this.drawSceneCube();
      this.deltaTime = this.deltaTime + (this._60fpsInterval * milliseconds);
    });
  }

  ngOnInit(): void {
  }

  /**
   * Draws the scene
   */
  private drawSceneSquare() {
    // prepare the scene and update the viewport
    this.webglService.formatScene();

    if (!this.gl) {
      return;
    }

    // draw the scene
    const offset = 0;
    // 2 triangles, 3 vertices, 6 faces
    const vertexCount = 4;

    // translate and rotate the model-view matrix to display the cube
    const mvm = this.webglService.getModelViewMatrix();
    matrix.mat4.translate(
      mvm,                    // destination matrix
      mvm,                    // matrix to translate
      [-0.0, 0.0, -1.0]       // amount to translate
    );

    this.webglService.prepareScene(2);

    this.gl.drawArrays(
      this.gl.TRIANGLE_STRIP,
      offset,
      vertexCount
    );

    // rotate the cube
    this.cubeRotation = this.deltaTime;

  }

  /**
   * Draws the scene
   */
  private drawSceneCube() {
    // prepare the scene and update the viewport
    this.webglService.formatScene();

    if (!this.gl) {
      return;
    }

    // draw the scene
    const offset = 0;
    // 2 triangles, 3 vertices, 6 faces
    const vertexCount = 2 * 3 * 6;

    // translate and rotate the model-view matrix to display the cube
    const mvm = this.webglService.getModelViewMatrix();
    matrix.mat4.translate(
      mvm,                    // destination matrix
      mvm,                    // matrix to translate
      [-0.0, 0.0, -6.0]       // amount to translate
    );
    matrix.mat4.rotate(
      mvm,                    // destination matrix
      mvm,                    // matrix to rotate
      this.cubeRotation,      // amount to rotate in radians
      [1, 1, 1]               // rotate around X, Y, Z axis
    );

    this.webglService.prepareScene(3);

    this.gl.drawArrays(
      this.gl.TRIANGLES,
      offset,
      vertexCount
    );

    // rotate the cube
    this.cubeRotation = this.deltaTime;

  }

  //Events
  onKeyDown(e: any) {
    switch (e.code) {
      case "KeyA": {
        console.log("Pressed key a");
        //this.cameras.default.panBy({ x: 0.1 });
        break;
      }
      // case "KeyD": {
      //   this.cameras.default.panBy({ x: -0.1 });
      //   break;
      // }
      // case "KeyW": {
      //   this.cameras.default.panBy({ z: 0.1 });
      //   break;
      // }
      // case "KeyS": {
      //   this.cameras.default.panBy({ z: -0.1 });
      //   break;
      // }
      // case "NumpadAdd": {
      //   this.cameras.default.zoomBy(2);
      //   break;
      // }
      // case "NumpadSubtract": {
      //   this.cameras.default.zoomBy(0.5);
      //   break;
      // }
    }
  }

  private onPointerDown = (e: any) => {
    console.log("clicked!");
    this.initialPosition = [e.offsetX, e.offsetY];
    this.initialCameraPosition = this.webglService.getCamera().getPosition();
    this.canvas?.nativeElement.setPointerCapture(e.pointerId);
    this.canvas?.nativeElement.addEventListener("pointermove", this.onPointerMove);
    this.canvas?.nativeElement.addEventListener("pointerup", this.onPointerUp);
  }

  private onPointerUp = (e: any) => {
    this.canvas?.nativeElement.removeEventListener("pointermove", this.onPointerMove);
    this.canvas?.nativeElement.removeEventListener("pointerup", this.onPointerUp);
    this.canvas?.nativeElement.releasePointerCapture(e.pointerId);
  }

  private onPointerMove = (e: any) => {
    if (!this.gl) {
      return;
    }
    const pointerDelta = [
      e.offsetX - this.initialPosition[0],
      e.offsetY - this.initialPosition[1]
    ];

    const radsPerWidth = (180 / degreesPerRad) / this.gl.canvas.width;
    const xRads = pointerDelta[0] * radsPerWidth;
    const yRads = pointerDelta[1] * radsPerWidth * (this.gl.canvas.height / this.gl.canvas.width);

    this.webglService.getCamera().setPosition(this.initialCameraPosition);
    this.webglService.getCamera().orbitBy({ long: xRads, lat: yRads });
  }

  private onWheel = (e: any) => {
    const delta = e.deltaY / 1000;
    this.webglService.getCamera().orbitBy({ radius: delta });
  }

}
