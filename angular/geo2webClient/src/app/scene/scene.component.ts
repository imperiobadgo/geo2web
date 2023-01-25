import {AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import * as THREE from "three";
import {TrackballControls} from "three/examples/jsm/controls/TrackballControls";
import {TransformControls} from "three/examples/jsm/controls/TransformControls";

import {ResizeService} from "../services/resize.service";
import {ConstructionElement} from "../domain/construction-element/construction-element";
import {ConstructionElementsService} from "../services/construction-elements.service";

// @ts-ignore
import cubeFragmentShaderSrc from '../../assets/cube-fragment-shader.glsl';
// @ts-ignore
import cubeVertexShaderSrc from '../../assets/cube-vertex-shader.glsl';

// @ts-ignore
import gridFragmentShaderSrc from '../../assets/grid-fragment-shader.glsl';
// @ts-ignore
import sinusFragmentShaderSrc from '../../assets/sinus-fragment-shader.glsl';
// @ts-ignore
import fullscreenVertexShaderSrc from '../../assets/fullscreen-vertex-shader.glsl';

import renderVertexShaderSrc from '../../assets/render-vertex-shader.glsl';
import renderFragmentShaderSrc from '../../assets/render-fragment-shader.glsl';

@Component({
  selector: 'app-scene',
  templateUrl: './scene.component.html',
  styleUrls: ['./scene.component.scss']
})
export class SceneComponent implements OnInit, AfterViewInit {

  @ViewChild('sceneCanvas')
  private canvasRef!: ElementRef;

  //* Stage Properties

  @Input() public cameraZ: number = 400;

  @Input() public fieldOfView: number = 45;

  @Input('nearClipping') public nearClippingPlane: number = 1;

  @Input('farClipping') public farClippingPlane: number = 1000;

  //? Helper Properties (Private Properties);

  private camera!: THREE.PerspectiveCamera;
  private controls!: TrackballControls;
  private transformControl!: TransformControls;


  private get canvas(): HTMLCanvasElement {
    return this.canvasRef.nativeElement;
  }

  private renderer!: THREE.WebGLRenderer;
  private renderTarget!: THREE.WebGLMultipleRenderTargets;

  private scene!: THREE.Scene;
  private postProcessingScene!: THREE.Scene;

  private postProcessingCamera!: THREE.Camera;

  private gridObject!: THREE.Object3D;
  private gridShader!: THREE.ShaderMaterial;
  private sinusShader!: THREE.ShaderMaterial;

  private elementShaders!: THREE.ShaderMaterial[];

  /**
   * Create the scene
   *
   * @private
   * @memberof CubeComponent
   */
  private createScene() {
    //* Scene
    this.scene = new THREE.Scene();
    this.scene.background = new THREE.Color(0x000000)

    //*Camera
    let aspectRatio = this.getAspectRatio();
    this.camera = new THREE.PerspectiveCamera(
      this.fieldOfView,
      aspectRatio,
      this.nearClippingPlane,
      this.farClippingPlane
    )
    this.camera.position.z = this.cameraZ;
  }

  private getAspectRatio() {
    return this.canvas.clientWidth / this.canvas.clientHeight;
  }

  private createControls(camera: THREE.Camera) {

    this.controls = new TrackballControls(camera, this.renderer.domElement);

    this.controls.rotateSpeed = 3.0;
    this.controls.zoomSpeed = 1.2;
    this.controls.panSpeed = 0.1;
    this.controls.dynamicDampingFactor = 0.2;

    this.controls.keys = ['KeyA', 'KeyS', 'KeyD'];

    //Is not rendered currently, because it is using the default materials with only one shader output...
    this.transformControl = new TransformControls(camera, this.renderer.domElement);
    let cameraControl = this.controls;
    this.transformControl.addEventListener( 'dragging-changed', function ( event : any ) {
      cameraControl.enabled = ! event.value;
    } );
  }

  /**
   * Start the rendering loop
   *
   * @private
   * @memberof CubeComponent
   */
  private startRenderingLoop() {
    //* Renderer
    // Use canvas element in template
    this.renderer = new THREE.WebGLRenderer({canvas: this.canvas, antialias: true});
    this.renderer.setPixelRatio(devicePixelRatio);
    this.renderer.setSize(this.canvas.clientWidth, this.canvas.clientHeight);

    let pxr = this.renderer.getPixelRatio();
    // Create a multi render target with Float buffers
    this.renderTarget = new THREE.WebGLMultipleRenderTargets(
      this.canvas.clientWidth * pxr,
      this.canvas.clientHeight * pxr, 2);

    for (let i = 0, il = this.renderTarget.texture.length; i < il; i++) {

      this.renderTarget.texture[i].minFilter = THREE.NearestFilter;
      this.renderTarget.texture[i].magFilter = THREE.NearestFilter;

    }

    this.postProcessingScene = new THREE.Scene();
    this.postProcessingCamera = new THREE.OrthographicCamera(-1, 1, 1, -1, 0, 1);

    this.postProcessingScene.add(new THREE.Mesh(
      new THREE.PlaneGeometry(2, 2),
      new THREE.RawShaderMaterial({
        vertexShader: renderVertexShaderSrc,
        fragmentShader: renderFragmentShaderSrc,
        uniforms: {
          tDiffuse: {value: this.renderTarget.texture[0]},
          tTest: {value: this.renderTarget.texture[1]},
        },
        glslVersion: THREE.GLSL3
      })
    ));

    this.createControls(this.camera);

    this.transformControl.attach(this.gridObject);
    this.scene.add(this.transformControl);

    let component: SceneComponent = this;
    (function render() {
      requestAnimationFrame(render);
      component.updateThemeColors();

      // component.resizeCanvasToDisplaySize();
      component.controls.update();

      // component.gridShader.uniforms['clip2camera'].value = component.camera.projectionMatrixInverse;
      // component.gridShader.uniforms['camera2world'].value = component.camera.matrixWorld;

      component.sinusShader.uniforms['clip2camera'].value = component.camera.projectionMatrixInverse;
      component.sinusShader.uniforms['camera2world'].value = component.camera.matrixWorld;

      for (let i = 0; i < component.elementShaders.length; i++) {
        let shader = component.elementShaders[i];
        shader.uniforms['screenSize'].value = new THREE.Vector2(component.renderer.domElement.width, component.renderer.domElement.height);
        shader.uniforms['inverseCameraWorld'].value = component.camera.matrixWorldInverse;
      }

      // render scene into target
      component.renderer.setRenderTarget(component.renderTarget);

      component.renderer.render(component.scene, component.camera);

      // render post FX
      component.renderer.setRenderTarget(null);
      component.renderer.render(component.postProcessingScene, component.postProcessingCamera);
    }());
  }

  private updateThemeColors(): void {
    const style = getComputedStyle(this.el.nativeElement);
    const backgroundColor = style.getPropertyValue('--surface-card');

    this.scene.background = new THREE.Color(backgroundColor);
  }

  private resizeCanvasToDisplaySize(width: number, height: number) {
    const canvas = this.renderer.domElement;
    if (canvas.width !== width || canvas.height !== height) {
      // you must pass true here to update the style of canvas,
      // otherwise the canvas size is reset to the style size
      this.renderer.setSize(width, height, true);
      this.camera.aspect = width / height;
      this.camera.updateProjectionMatrix();

      //also update the renderTarget to new display size
      const dpr = this.renderer.getPixelRatio();
      this.renderTarget.setSize(width * dpr, height * dpr);
    }
  }

  constructor(private elementService: ConstructionElementsService, private el: ElementRef, private resizeService: ResizeService) {
    this.elementShaders = []; //create empty array
    elementService.currentElements.subscribe(elements => {
      this.resetSceneContent(elements);
    })
  }

  resetSceneContent(elements: ConstructionElement[]) {
    if (!this.scene) return;
    this.scene.clear();
    this.elementShaders.length = 0;//clear array by setting the length to 0. All elements with index greater than 0 are deleted.

    this.addGrid();
    this.addSinus();
    this.addExprerimentalCube();

    let elementUniforms = {
      screenSize: {value: new THREE.Vector2(300, 200)},
      inverseCameraWorld: {value: this.camera.matrixWorldInverse},
    }
    for (let i = 0; i < elements.length; i++) {
      let element = elements[i];

      let elementShader = new THREE.RawShaderMaterial({
        vertexShader: fullscreenVertexShaderSrc,
        fragmentShader: element.fragmentShader,
        depthWrite: false,
        depthTest: false,
        transparent: true,
        uniforms: elementUniforms,
        glslVersion: THREE.GLSL3
      });

      var elementQuad = new THREE.Mesh(
        new THREE.PlaneGeometry(2, 2),
        elementShader
      );
      elementQuad.frustumCulled = false;//disable frustum culling to show the content, even if the origin is not in view

      this.scene.add(elementQuad);
      this.elementShaders.push(elementShader);
    }
  }

  ngOnInit(): void {
    this.resizeService.addResizeEventListener(this.el.nativeElement, (elem: any) => {
      this.resizeCanvasToDisplaySize(elem.clientWidth, elem.clientHeight);
    });
  }

  ngOnDestroy() {
    this.resizeService.removeResizeEventListener(this.el.nativeElement);
  }

  ngAfterViewInit() {
    this.createScene();
    this.addExprerimentalCube();
    // this.addGrid();
    this.addSinus();
    this.startRenderingLoop();
  }

  private addGrid() {

    let gridUniforms = {
      clip2camera: {value: this.camera.projectionMatrixInverse},
      camera2world: {value: this.camera.matrixWorld}
    }
    this.gridShader = new THREE.RawShaderMaterial({
      vertexShader: fullscreenVertexShaderSrc,
      fragmentShader: gridFragmentShaderSrc,
      depthWrite: false,
      depthTest: false,
      transparent: true,
      uniforms: gridUniforms,
      glslVersion: THREE.GLSL3
    });

    this.gridObject = new THREE.Mesh(
      new THREE.PlaneGeometry(2, 2),
      this.gridShader
    );
    this.gridObject.frustumCulled = false;//disable frustum culling to show the content, even if the origin is not in view

    this.scene.add(this.gridObject);
  }

  private addSinus() {
    let sinusUniforms = {
      clip2camera: {value: this.camera.projectionMatrixInverse},
      camera2world: {value: this.camera.matrixWorld}
    }
    this.sinusShader = new THREE.RawShaderMaterial({
      vertexShader: fullscreenVertexShaderSrc,
      fragmentShader: sinusFragmentShaderSrc,
      depthWrite: false,
      depthTest: false,
      transparent: true,
      uniforms: sinusUniforms,
      glslVersion: THREE.GLSL3
    });

    var sinusQuad = new THREE.Mesh(
      new THREE.PlaneGeometry(2, 2),
      this.sinusShader
    );
    sinusQuad.frustumCulled = false;//disable frustum culling to show the content, even if the origin is not in view

    this.scene.add(sinusQuad);
  }

  private addExprerimentalCube() {
    let uniforms = {
      colorB: {value: new THREE.Color(0x00FF00)},
      colorA: {value: new THREE.Color(0xFF0000)}
    };

    let geometry = new THREE.BoxGeometry(1, 1, 1);
    let material = new THREE.RawShaderMaterial({
      uniforms: uniforms,
      fragmentShader: cubeFragmentShaderSrc,
      vertexShader: cubeVertexShaderSrc,
      glslVersion: THREE.GLSL3
    });

    let mesh = new THREE.Mesh(geometry, material);
    // mesh.position.x = 2;
    this.scene.add(mesh);
  }
}
