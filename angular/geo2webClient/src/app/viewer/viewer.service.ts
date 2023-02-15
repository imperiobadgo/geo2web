import {Inject, Injectable, NgZone} from '@angular/core';
import {
  ArcRotateCamera,
  Color4,
  Engine,
  FreeCamera,
  HemisphericLight,
  Light,
  Mesh,
  MeshBuilder,
  Scene,
  Vector3
} from '@babylonjs/core';
import {DOCUMENT} from "@angular/common";


@Injectable({
  providedIn: 'root'
})
export class ViewerService {

  protected engine!: Engine;
  protected canvas!: HTMLCanvasElement;
  protected camera!: FreeCamera | ArcRotateCamera;
  protected light!: Light;

  rootMesh!: Mesh;
  scene!: Scene;

  public constructor(@Inject(DOCUMENT) private document: Document, private readonly ngZone: NgZone) {
  }

  createScene(canvas: HTMLCanvasElement): Scene {
    this.canvas = canvas;
    // this.canvas.style.height = '100%';
    // this.canvas.style.width = '100%';
    this.engine = new Engine(this.canvas, true);

    this.scene = new Scene(this.engine);
    this.scene.clearColor = new Color4(.8, .1, .1, 1);
    this.rootMesh = MeshBuilder.CreateDisc('root', {radius: .01}, this.scene);

    this.light = new HemisphericLight('light1', new Vector3(0, 1, 0), this.scene);

    // rotate scene by mouse-move
    // https://www.babylonjs-playground.com/#CGXLT#5

    let clicked = false;
    const currentPosition = {x: 0, y: 0};
    const currentRotation = {x: 0, y: 0};
    let mousemov = false;
    let framecount = 0;
    const mxframecount = 120; //4 secs at 60 fps
    const lastAngleDiff = {x: 0, y: 0};
    const oldAngle = {x: 0, y: 0};
    const newAngle = {x: 0, y: 0};

    this.scene.addMesh(this.rootMesh);

    this.scene.beforeRender = () => {
      mousemov = false;
    };

    this.scene.afterRender = () => {
      if (!mousemov && framecount < mxframecount) {
        lastAngleDiff.x = lastAngleDiff.x / 1.1;
        lastAngleDiff.y = lastAngleDiff.y / 1.1;
        this.rootMesh.rotation.x += lastAngleDiff.x;
        this.rootMesh.rotation.y += lastAngleDiff.y;
        framecount++;
        currentRotation.x = this.rootMesh.rotation.x;
        currentRotation.y = this.rootMesh.rotation.y;
      } else if (framecount >= mxframecount) {
        framecount = 0;
      }
    };

    this.camera = new ArcRotateCamera('Camera', 0, 0.8, 35, Vector3.Zero(), this.scene);
    this.camera.setTarget(this.rootMesh);


    this.canvas.addEventListener('pointerdown', (evt) => {
      currentPosition.x = evt.clientX;
      currentPosition.y = evt.clientY;
      currentRotation.x = this.rootMesh.rotation.x;
      currentRotation.y = this.rootMesh.rotation.y;
      clicked = true;
    });

    this.canvas.addEventListener('pointermove', (evt) => {

      if (clicked) {
        mousemov = true;
      }
      if (!clicked) {
        return;
      }
      oldAngle.x = this.rootMesh.rotation.x;
      oldAngle.y = this.rootMesh.rotation.y;
      this.rootMesh.rotation.y -= (evt.clientX - currentPosition.x) / 300.0;
      this.rootMesh.rotation.x -= (evt.clientY - currentPosition.y) / 300.0;
      newAngle.x = this.rootMesh.rotation.x;
      newAngle.y = this.rootMesh.rotation.y;
      lastAngleDiff.x = newAngle.x - oldAngle.x;
      lastAngleDiff.y = newAngle.y - oldAngle.y;
      currentPosition.x = evt.clientX;
      currentPosition.y = evt.clientY;
    });

    this.canvas.addEventListener('pointerup', () => {
      clicked = false;
    });
    return this.scene;
  }

  start(inZone = true): void {

    if (inZone) {
      this.ngZone.runOutsideAngular(() => {
        this.startTheEngine();
      });
    } else {
      this.startTheEngine();
    }
  }

  stop(): void {
    this.scene.dispose();
    this.engine.stopRenderLoop();
    this.engine.dispose();
    this.camera.dispose();
    window.removeEventListener('resize', () => {
    });
  }

  private startTheEngine() {
    let freshRender = true;
    const element = this.document.getElementById('fpsLabel');

    this.engine.runRenderLoop(() => {
      this.scene.render();
      if (element) {
        element.innerHTML = this.engine.getFps().toFixed() + ' fps';
      }
      if (freshRender) {
        this.engine.resize();
        freshRender = false;
      }
    });
    // window.addEventListener('resize', () => this.engine.resize());
  }

  resize(clientWidth: number, clientHeight: number) {
    this.engine.setSize(clientWidth, clientHeight, true);
  }
}
