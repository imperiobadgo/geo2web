import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ViewerComponent} from "./viewer/viewer.component";

//This will ensure that on app load, it'll display the SceneComponent first.
const routes: Routes = [{path: "", component:ViewerComponent}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
