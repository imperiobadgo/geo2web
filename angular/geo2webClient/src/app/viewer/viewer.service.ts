import {ElementRef, Inject, Injectable, NgZone} from '@angular/core';
import {
  ArcRotateCamera,
  AxesViewer,
  Axis,
  Color4,
  Engine,
  FreeCamera, Mesh,
  MeshBuilder, MultiRenderTarget,
  Scene,
  ShaderLanguage,
  ShaderMaterial,
  Vector3
} from '@babylonjs/core';
import {DOCUMENT} from "@angular/common";

// @ts-ignore
import planeVertexShaderSrc from '../../assets/babylon-simple-vertex.glsl';
// @ts-ignore
import planeFragmentShaderSrc from '../../assets/babylon-simple-fragment.glsl';
// @ts-ignore
import fullscreenVertexShaderSrc from '../../assets/babylon-fullscreen-vertex.glsl';
// @ts-ignore
import gridFragmentShaderSrc from '../../assets/babylon-grid-fragment.glsl';

@Injectable({
  providedIn: 'root'
})
export class ViewerService {

  protected el!: ElementRef;
  protected canvas!: HTMLCanvasElement;

  protected engine!: Engine;
  protected camera!: FreeCamera | ArcRotateCamera;

  scene!: Scene;
  renderTarget!: MultiRenderTarget;

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
    this.camera.wheelPrecision = 10;

    // this.camera.setTarget(this.rootMesh);
    this.camera.attachControl(this.canvas, true);

    const worldAxes = new AxesViewer(this.scene, 2);
    worldAxes.update(new Vector3(0, 0, 0), Axis.X, Axis.Y, Axis.Z);

    // let box = MeshBuilder.CreateBox("box", {height: 1, width: 0.75, depth: 0.25}, this.scene);
    let plane = MeshBuilder.CreatePlane("plane", {height: 2, width: 2, sideOrientation: Mesh.DOUBLESIDE}, this.scene);

    this.renderTarget = new MultiRenderTarget("renderTarget",
      {height: 100, width: 100}, 2, this.scene);


    const planeMaterial = new ShaderMaterial("shader", this.scene,
      {vertexSource: fullscreenVertexShaderSrc, fragmentSource: gridFragmentShaderSrc},
      {
        attributes: ["position", "uv"],
        uniforms: ["worldViewProjection"],
        needAlphaBlending: true,
        needAlphaTesting: true,
        shaderLanguage: ShaderLanguage.GLSL
      });

    plane.material = planeMaterial;

    plane.alwaysSelectAsActiveMesh = true;//disable frustum culling

    this.renderTarget.renderList = [];
    this.renderTarget.renderList.push(plane);

    this.scene.customRenderTargets.push(this.renderTarget);
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
    this.renderTarget.resize({width: clientWidth, height: clientHeight});
  }

  private updateThemeColors(): void {
    const style = getComputedStyle(this.el.nativeElement);
    const backgroundColor = style.getPropertyValue('--surface-card');

    this.scene.clearColor = Color4.FromHexString(backgroundColor);
  }

}
