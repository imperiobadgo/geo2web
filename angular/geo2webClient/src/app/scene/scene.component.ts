import {AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import * as THREE from "three";
import {TrackballControls} from "three/examples/jsm/controls/TrackballControls";
import {ResizeService} from "../services/resize.service";
// @ts-ignore
import cubeFragmentShaderSrc from '../../assets/cube-fragment-shader.glsl';
// @ts-ignore
import cubeVertexShaderSrc from '../../assets/cube-vertex-shader.glsl';

// @ts-ignore
import gridFragmentShaderSrc from '../../assets/grid-fragment-shader.glsl';
// @ts-ignore
import fullscreenVertexShaderSrc from '../../assets/fullscreen-vertex-shader.glsl';

@Component({
  selector: 'app-scene',
  templateUrl: './scene.component.html',
  styleUrls: ['./scene.component.scss']
})
export class SceneComponent implements OnInit, AfterViewInit {

  @ViewChild('sceneCanvas')
  private canvasRef!: ElementRef;

  //* Cube Properties

  @Input() public rotationSpeedX: number = 0.05;

  @Input() public rotationSpeedY: number = 0.01;

  @Input() public size: number = 200;

  @Input() public texture: string = "/assets/texture.jpg";


  //* Stage Properties

  @Input() public cameraZ: number = 400;

  @Input() public fieldOfView: number = 1;

  @Input('nearClipping') public nearClippingPlane: number = 1;

  @Input('farClipping') public farClippingPlane: number = 1000;

  //? Helper Properties (Private Properties);

  private camera!: THREE.PerspectiveCamera;
  private controls!: TrackballControls;

  private get canvas(): HTMLCanvasElement {
    return this.canvasRef.nativeElement;
  }

  private loader = new THREE.TextureLoader();
  private geometry = new THREE.BoxGeometry(1, 1, 1);
  private material = new THREE.MeshBasicMaterial({map: this.loader.load(this.texture)});

  private cube: THREE.Mesh = new THREE.Mesh(this.geometry, this.material);

  private renderer!: THREE.WebGLRenderer;

  private scene!: THREE.Scene;

  private gridShader!: THREE.ShaderMaterial;

  /**
   *Animate the cube
   *
   * @private
   * @memberof CubeComponent
   */
  private animateCube() {
    this.cube.rotation.x += this.rotationSpeedX;
    this.cube.rotation.y += this.rotationSpeedY;
  }

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
    this.scene.add(this.cube);
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
    this.controls.panSpeed = 0.005;
    this.controls.dynamicDampingFactor = 0.2;

    this.controls.keys = ['KeyA', 'KeyS', 'KeyD'];

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
    this.renderer = new THREE.WebGLRenderer({canvas: this.canvas});
    this.renderer.setPixelRatio(devicePixelRatio);
    this.renderer.setSize(this.canvas.clientWidth, this.canvas.clientHeight);

    this.createControls(this.camera);

    let component: SceneComponent = this;
    (function render() {
      requestAnimationFrame(render);
      component.updateThemeColors();

      // component.resizeCanvasToDisplaySize();
      component.controls.update();
      component.gridShader.uniforms['screenSize'].value = new THREE.Vector2(component.renderer.domElement.width, component.renderer.domElement.height);
      component.gridShader.uniforms['inverseCameraWorld'].value = component.camera.matrixWorldInverse;
      // component.gridShader.uniforms['cameraPosition'].value = component.camera.position;
      component.gridShader.uniforms['fov'].value = component.camera.fov;

      //component.animateCube();
      component.renderer.render(component.scene, component.camera);
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

      // set render target sizes here
    }
  }

  constructor(private el: ElementRef, private resizeService: ResizeService) {
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
    this.addGrid();
    this.startRenderingLoop();
  }

  private addGrid() {
    let gridUniforms = {
      screenSize: {type: "vec2", value: new THREE.Vector2(300, 200)},
      inverseCameraWorld: {type: "mat4", value: this.camera.matrixWorldInverse},
      // cameraPosition: {type: "vec3", value: this.camera.position},
      fov: {type: "float", value: this.camera.fov},
    }
    this.gridShader = new THREE.ShaderMaterial({
      vertexShader: fullscreenVertexShaderSrc,
      fragmentShader: gridFragmentShaderSrc,
      depthWrite: false,
      depthTest: false,
      transparent: true,
      uniforms: gridUniforms,
    });

    var quad = new THREE.Mesh(
      new THREE.PlaneGeometry(2, 2),
      this.gridShader
    );


    this.scene.add(quad);
  }

  private addExprerimentalCube() {
    let uniforms = {
      colorB: {type: 'vec3', value: new THREE.Color(0x00FF00)},
      colorA: {type: 'vec3', value: new THREE.Color(0xFF0000)}
    };

    let geometry = new THREE.BoxGeometry(1, 1, 1);
    let material = new THREE.ShaderMaterial({
      uniforms: uniforms,
      fragmentShader: cubeFragmentShaderSrc,
      vertexShader: cubeVertexShaderSrc,
    });

    let mesh = new THREE.Mesh(geometry, material);
    mesh.position.x = 2;
    this.scene.add(mesh);
  }
}
