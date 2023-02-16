import {ElementRef, Inject, Injectable, NgZone} from '@angular/core';
import {
  ArcRotateCamera, AxesViewer, Axis,
  Color4,
  Engine,
  FreeCamera,
  MeshBuilder,
  Scene,
  Vector3
} from '@babylonjs/core';
import {DOCUMENT} from "@angular/common";


@Injectable({
  providedIn: 'root'
})
export class ViewerService {

  protected el!: ElementRef;
  protected canvas!: HTMLCanvasElement;

  protected engine!: Engine;
  protected camera!: FreeCamera | ArcRotateCamera;

  scene!: Scene;

  public constructor(@Inject(DOCUMENT) private document: Document, private readonly ngZone: NgZone) {
  }

  createScene(el: ElementRef, canvas: HTMLCanvasElement): Scene {
    this.el = el;
    this.canvas = canvas;
    this.engine = new Engine(this.canvas, true);

    this.scene = new Scene(this.engine);
    this.scene.clearColor = new Color4(.8, .1, .1, 1);

    this.camera = new ArcRotateCamera('Camera', 0, 0.8, 35, Vector3.Zero(), this.scene);
    this.camera.panningInertia = 0.9;
    this.camera.panningSensibility = 90;
    this.camera.lowerRadiusLimit = 2;
    // this.camera.setTarget(this.rootMesh);
    this.camera.attachControl(this.canvas, true);

    const worldAxes = new AxesViewer(this.scene, 2);
    worldAxes.update(new Vector3(0, 0, 0), Axis.X, Axis.Y, Axis.Z);

    MeshBuilder.CreateBox("box",{height: 1, width: 0.75, depth: 0.25} , this.scene);

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
      this.updateThemeColors();
      this.scene.render();
      if (element) {
        element.innerHTML = this.engine.getFps().toFixed() + ' fps';
      }
      if (freshRender) {
        this.engine.resize();
        freshRender = false;
      }
    });
  }

  resize(clientWidth: number, clientHeight: number) {
    this.engine.setSize(clientWidth, clientHeight, true);
  }

  private updateThemeColors(): void {
    const style = getComputedStyle(this.el.nativeElement);
    const backgroundColor = style.getPropertyValue('--surface-card');

    this.scene.clearColor = Color4.FromHexString(backgroundColor);
  }

}
