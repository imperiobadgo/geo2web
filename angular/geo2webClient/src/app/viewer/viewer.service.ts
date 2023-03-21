import {ElementRef, Inject, Injectable, NgZone} from '@angular/core';
import {
  ArcRotateCamera,
  AxesViewer,
  Axis,
  Color4, Effect,
  Engine,
  FreeCamera, HemisphericLight, Mesh,
  MeshBuilder, MultiRenderTarget, PostProcess,
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
// @ts-ignore
import sinusFragmentShaderSrc from '../../assets/babylon-sinus-fragment.glsl';
// @ts-ignore
import depthVisualizationFragmentShaderSrc from '../../assets/babylon-depthVisualization-fragment.glsl';

const MAIN_RENDER_TEXTURE = "mainRenderTexture";
const MAIN_DEPTH_TEXTURE = "mainDepthTexture";

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
  gridMaterial!: ShaderMaterial;
  sinusMaterial!: ShaderMaterial;

  private mouseX!: number;
  private mouseY!: number;
  private hoveredObjectId!: number;

  public constructor(@Inject(DOCUMENT) private document: Document, private readonly ngZone: NgZone) {
  }

  createScene(el: ElementRef, canvas: HTMLCanvasElement): Scene {
    this.el = el;
    this.canvas = canvas;
    this.engine = new Engine(this.canvas, true);

    this.engine.setDepthWrite(true);

    this.scene = new Scene(this.engine);
    this.scene.clearColor = new Color4(.8, .1, .1, 1);

    this.camera = new ArcRotateCamera('Camera', 0, 0.8, 35, Vector3.Zero(), this.scene);
    this.camera.panningInertia = 0.9;
    this.camera.panningSensibility = 90;
    this.camera.lowerRadiusLimit = 2;
    this.camera.wheelPrecision = 10;
    this.camera.minZ = 1.0;
    this.camera.maxZ = 200.0;

    // this.camera.setTarget(this.rootMesh);
    this.camera.attachControl(this.canvas, true);

    const worldAxes = new AxesViewer(this.scene, 2);
    worldAxes.update(new Vector3(0, 0, 0), Axis.X, Axis.Y, Axis.Z);

    var light = new HemisphericLight("light", new Vector3(0, 1, 0), this.scene);

    let box = MeshBuilder.CreateBox("box", {height: 1, width: 1, depth: 1}, this.scene);
    let grid = MeshBuilder.CreatePlane("grid", {height: 2, width: 2, sideOrientation: Mesh.DOUBLESIDE}, this.scene);
    let sinus = MeshBuilder.CreatePlane("sinus", {height: 2, width: 2, sideOrientation: Mesh.DOUBLESIDE}, this.scene);

    this.renderTarget = new MultiRenderTarget("renderTarget",
      {height: 100, width: 100}, 2, this.scene,
      {
        generateDepthTexture: true
      });

    let boxMaterial = new ShaderMaterial("box-shader", this.scene,
      {vertexSource: planeVertexShaderSrc, fragmentSource: planeFragmentShaderSrc},
      {
        attributes: ["position", "uv"],
        uniforms: ["worldViewProjection"],
        needAlphaBlending: false,
        needAlphaTesting: true,
        shaderLanguage: ShaderLanguage.GLSL
      });

//, "logarithmicDepthConstant"
    box.material = boxMaterial;

    this.gridMaterial = new ShaderMaterial("grid-shader", this.scene,
      {vertexSource: fullscreenVertexShaderSrc, fragmentSource: gridFragmentShaderSrc},
      {
        attributes: ["position", "uv"],
        uniforms: ["clip2camera", "camera2world"],
        needAlphaBlending: false,
        needAlphaTesting: true,
        shaderLanguage: ShaderLanguage.GLSL
      });

    this.sinusMaterial = new ShaderMaterial("sinus-shader", this.scene,
      {vertexSource: fullscreenVertexShaderSrc, fragmentSource: sinusFragmentShaderSrc},
      {
        attributes: ["position", "uv"],
        uniforms: ["id", "color", "clip2camera", "camera2world", "nearClipping", "farClipping"],
        needAlphaBlending: false,
        needAlphaTesting: true,
        shaderLanguage: ShaderLanguage.GLSL,
      });

    grid.material = this.gridMaterial;
    grid.alwaysSelectAsActiveMesh = true;//disable frustum culling

    sinus.material = this.sinusMaterial;
    sinus.alwaysSelectAsActiveMesh = true;

    this.scene.customRenderTargets.push(this.renderTarget);

    Effect.ShadersStore["customFragmentShader"] = depthVisualizationFragmentShaderSrc;

    const viewerTarget = this.renderTarget;
    const postProcess = new PostProcess("My custom post process", "custom",
      [],
      [
        MAIN_RENDER_TEXTURE,
        MAIN_DEPTH_TEXTURE,
      ],
      1.0,
      this.camera);
    postProcess.onApply = function (effect) {
      effect.setTexture(MAIN_RENDER_TEXTURE, viewerTarget);
      effect.setTexture(MAIN_DEPTH_TEXTURE, viewerTarget.depthTexture);
    }

    this.renderTarget.renderList = [];
    this.renderTarget.renderList.push(box);
    // this.renderTarget.renderList.push(grid);
    this.renderTarget.renderList.push(sinus);

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

      let projectionMatrix = this.camera.getProjectionMatrix().clone();
      let projectionMatrixInverted = projectionMatrix.invert();

      this.gridMaterial.setMatrix("clip2camera", projectionMatrixInverted);
      this.gridMaterial.setMatrix("camera2world", this.camera.getWorldMatrix());
      this.sinusMaterial.setMatrix("clip2camera", projectionMatrixInverted);
      this.sinusMaterial.setMatrix("camera2world", this.camera.getWorldMatrix());

      const id = 1000;

      //the alpha value must apparently always be 1.0 for it to work
      const idValue = [
        ((id >> 0) & 0xFF) / 0xFF,
        ((id >> 8) & 0xFF) / 0xFF,
        ((id >> 16) & 0xFF) / 0xFF,
        1.0,
      ];

      if (id == this.hoveredObjectId) {
        this.sinusMaterial.setArray3("color", [0.0, 1.0, 0.0]);
      } else {
        this.sinusMaterial.setArray3("color", [1.0, 0.0, 0.0]);
      }

      this.sinusMaterial.setArray4("id", idValue);
      this.sinusMaterial.setFloat("nearClipping", this.camera.minZ);
      this.sinusMaterial.setFloat("farClipping", this.camera.maxZ);

      // viewerTarget.depthTexture.readPixels()

      this.scene.render();

      const pixelX = this.mouseX * this.canvas.width / this.canvas.clientWidth;
      const pixelY = this.canvas.height - this.mouseY * this.canvas.height / this.canvas.clientHeight - 1;

      const data = new Uint8Array(4);

      const pixelData = this.renderTarget.textures[1].readPixels(
        0, 0, data, false, true,
        pixelX, pixelY, 1, 1);

      pixelData?.then(x => {
        //don't convert the alpha value, because it is always 1.0 (or in this case 255)
        this.hoveredObjectId = data[0] + (data[1] << 8) + (data[2] << 16);// + (data[3] << 24);
      })

      if (element) {
        element.innerHTML = this.engine.getFps().toFixed() + ' fps';
      }
      if (freshRender) {
        this.engine.resize();
        freshRender = false;
      }
    });
  }


  onPointerMove(event: any) {
    const canvas = this.canvas;
    const rect = canvas.getBoundingClientRect();

    //Update the mouse position on mouse move
    this.mouseX = event.clientX - rect.left;
    this.mouseY = event.clientY - rect.top;

  }

  onPointerUp(event: any) {
    console.log("Id: " + this.hoveredObjectId);

    const pixelX = this.mouseX * this.canvas.width / this.canvas.clientWidth;
    const pixelY = this.canvas.height - this.mouseY * this.canvas.height / this.canvas.clientHeight - 1;

    const data = new Float32Array(4);

    const depthRgba = this.renderTarget.depthTexture.readPixels(
      0, 0, null, false, true,
      pixelX, pixelY, 1, 1);

    depthRgba?.then(x => {
      let z = this.unpackZFromRGBA(x);
      let depth = this.distanceToDepth(this.camera.minZ + z * (this.camera.maxZ - this.camera.minZ),
        this.camera.minZ,
        this.camera.maxZ)
      console.log(x + "  " + z + "   " + depth);
    })
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

  private unpackZFromRGBA(rgba: any): number {
    let z = 0;
    if (rgba instanceof Uint8Array) {
      z = rgba[0] / (255 * 255 * 255 * 255) + rgba[1] / (255 * 255 * 255) + rgba[2] / (255 * 255) + rgba[3] / 255;
    } else if (rgba instanceof Float32Array) {
      z = rgba[0] / (255 * 255 * 255) + rgba[1] / (255 * 255) + rgba[2] / 255 + rgba[3];
    }
    return Math.min(1, Math.max(0, z));
  }

  private distanceToDepth(distance: number, near: number, far: number) {
    return (far + near - 2. * near * far / distance) / 2. / (far - near) + 0.5;
  }

}
