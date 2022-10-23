import { Component } from '@angular/core';
import {ThemeService} from "./services/theme.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'geo2webClient';


  constructor(private themeService: ThemeService) {  }

  changeTheme(newTheme: string) {
    this.themeService.switchTheme(newTheme);
  }
}
