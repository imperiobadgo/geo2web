import {AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ViewerService} from "./viewer.service";

@Component({
  selector: 'app-viewer',
  templateUrl: './viewer.component.html',
  styleUrls: ['./viewer.component.scss']
})
export class ViewerComponent implements AfterViewInit, OnDestroy, OnInit {

  @ViewChild('sceneCanvas')
  private canvasRef!: ElementRef<HTMLCanvasElement>;

  constructor(private viewerService: ViewerService) { }

  ngOnInit(): void {
    this.initScene();
  }

  initScene() {
    // get the scene object
    const scene = this.viewerService.createScene(this.canvasRef);
    // by setting blockfreeActiveMeshesAndRenderingGroups we tell the engine to
    // insert all meshes without indexing and checking them
    scene.blockfreeActiveMeshesAndRenderingGroups = true;
    // this.addPlanets(scene);
    // we have to set it back to its original state
    scene.blockfreeActiveMeshesAndRenderingGroups = false;

  }

  ngAfterViewInit(): void {
    this.viewerService.start(true);
  }

  ngOnDestroy(): void {
    this.viewerService.stop();
  }

}
