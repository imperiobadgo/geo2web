import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {NoopAnimationsModule} from "@angular/platform-browser/animations";
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";

import {SplitterModule} from 'primeng/splitter';
import {MenubarModule} from 'primeng/menubar';
import {ToolbarModule} from "primeng/toolbar";
import {AutoCompleteModule} from 'primeng/autocomplete';
import {ButtonModule} from 'primeng/button';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {AlgebraPanelComponent} from './algebra-panel/algebra-panel.component';
import {AlgebraInputComponent} from './algebra-input/algebra-input.component';
import {HeaderComponent} from './header/header.component';
import {AlgebraPanelItemComponent} from './algebra-panel/algebra-panel-item/algebra-panel-item.component';
import { ViewerComponent } from './viewer/viewer.component';


@NgModule({
  declarations: [
    AppComponent,
    AlgebraPanelComponent,
    AlgebraInputComponent,
    HeaderComponent,
    AlgebraPanelItemComponent,
    ViewerComponent
  ],
  imports: [
    BrowserModule,
    NoopAnimationsModule,
    FormsModule,
    HttpClientModule,
    SplitterModule,
    MenubarModule,
    ToolbarModule,
    AutoCompleteModule,
    ButtonModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
