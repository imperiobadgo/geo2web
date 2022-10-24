import {Component, OnInit} from '@angular/core';
import {ThemeService} from "./services/theme.service";
import {MenuItem} from "primeng/api";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'geo2webClient';

  items: MenuItem[] = [];

  constructor(private themeService: ThemeService) {
  }

  ngOnInit(): void {
    this.items = [
      {
        label: 'Theme',
        items: [{
          label: 'blue',
          command: () => {
            this.changeTheme('saga-blue');
          }
        },
          {
            label: 'orange',
            command: () => {
              this.changeTheme('saga-orange');
            }
          },
          {
            label: 'dark blue',
            command: () => {
              this.changeTheme('vela-blue');
            }
          },
          {
            label: 'dark orange',
            command: () => {
              this.changeTheme('vela-orange');
            }
          }]
      },
      {
        label: 'Help'
      }
    ];
  }

  changeTheme(newTheme: string) {
    this.themeService.switchTheme(newTheme);
  }


}
