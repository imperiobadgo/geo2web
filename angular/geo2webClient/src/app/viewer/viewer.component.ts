import {AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ViewerService} from "./viewer.service";
import {ResizeService} from "../services/resize.service";

@Component({
  selector: 'app-viewer',
  templateUrl: './viewer.component.html',
  styleUrls: ['./viewer.component.scss']
})
export class ViewerComponent implements AfterViewInit, OnDestroy, OnInit {

  @ViewChild('sceneCanvas')
  private canvasRef!: ElementRef;

  constructor(private viewerService: ViewerService, private el: ElementRef, private resizeService: ResizeService) { }

  ngOnInit(): void {
    this.resizeService.addResizeEventListener(this.el.nativeElement, (elem: any) => {
          this.resizeCanvasToDisplaySize(elem.clientWidth, elem.clientHeight);
        });
    // this.initScene();
  }

  initScene() {
    // get the scene object
    const scene = this.viewerService.createScene(this.canvasRef.nativeElement);
    // by setting blockfreeActiveMeshesAndRenderingGroups we tell the engine to
    // insert all meshes without indexing and checking them
    scene.blockfreeActiveMeshesAndRenderingGroups = true;
    // this.addPlanets(scene);
    // we have to set it back to its original state
    scene.blockfreeActiveMeshesAndRenderingGroups = false;

  }

  ngAfterViewInit(): void {
    this.initScene();
    this.viewerService.start(true);
  }

  ngOnDestroy(): void {
    this.resizeService.removeResizeEventListener(this.el.nativeElement);
    this.viewerService.stop();
  }

  private resizeCanvasToDisplaySize(clientWidth: number, clientHeight: number) {
    this.viewerService.resize(clientWidth, clientHeight);
  }
}
