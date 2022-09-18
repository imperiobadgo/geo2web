import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {WebGLService} from "./services/web-gl.service";
import { interval } from 'rxjs';

@Component({
  selector: 'app-scene',
  templateUrl: './scene.component.html',
  styleUrls: ['./scene.component.css']
})
export class SceneComponent implements OnInit, AfterViewInit {
  @ViewChild('sceneCanvas') private canvas: ElementRef<HTMLCanvasElement> | undefined;

  /**
   * The interval of refresh rate for drawing our scene during one second of elapsed time (1000ms).
   */
  private _60fpsInterval = 16.666666666666666667;
  private gl: WebGLRenderingContext | undefined | null

  constructor(private webglService: WebGLService) { }

  ngAfterViewInit(): void {
    if (!this.canvas) {
      alert("canvas not supplied! cannot bind WebGL context!");
      return;
    }
    this.gl = this.webglService.initializeWebGLContext(this.canvas.nativeElement);


    // Set up to draw the scene periodically.
    const drawSceneInterval = interval(this._60fpsInterval);
    drawSceneInterval.subscribe(() => {
      this.drawScene();
    });
  }
  ngOnInit(): void {}

  /**
   * Draws the scene
   */
  private drawScene() {
    // prepare the scene and update the viewport
    this.webglService.updateViewport();
    this.webglService.prepareScene();

    if (!this.gl) {
      return;
    }

    // draw the scene
    const offset = 0;
    const vertexCount = 4;
    this.gl.drawArrays(
      this.gl.TRIANGLE_STRIP,
      offset,
      vertexCount
    );
  }

}
