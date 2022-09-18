import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {SceneComponent} from "./scene/scene.component";

//This will ensure that on app load, it'll display the SceneComponent first.
const routes: Routes = [{path: "", component:SceneComponent}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
