import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {SceneComponent} from './scene/scene.component';
import {AlgebraPanelComponent} from './algebra-panel/algebra-panel.component';
import {AlgebraInputComponent} from './algebra-input/algebra-input.component';
import {HeaderComponent} from './header/header.component';
import {InputTextModule} from 'primeng/inputtext';
import {ButtonModule} from 'primeng/button';
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import { AlgebraPanelItemComponent } from './algebra-panel/algebra-panel-item/algebra-panel-item.component';

@NgModule({
  declarations: [
    AppComponent,
    SceneComponent,
    AlgebraPanelComponent,
    AlgebraInputComponent,
    HeaderComponent,
    AlgebraPanelItemComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    InputTextModule,
    ButtonModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
