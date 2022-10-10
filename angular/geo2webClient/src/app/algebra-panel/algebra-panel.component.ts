import {Component, OnInit} from '@angular/core';
import {ConstructionElementRead} from "../domain/construction-element/construction-element-read";
import {ConstructionElementService} from "../services/construction-element.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-algebra-panel',
  templateUrl: './algebra-panel.component.html',
  styleUrls: ['./algebra-panel.component.scss']
})
export class AlgebraPanelComponent implements OnInit {

  public constructionElements: ConstructionElementRead[] = [];


  constructor(private constructionElementService: ConstructionElementService) {
  }

  ngOnInit(): void {
    this.getConstructionElements();
  }

  public getConstructionElements(): void {
    this.constructionElementService.getConstructionElements().subscribe(
      (response: ConstructionElementRead[]) => {
        this.constructionElements = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    )
  }

}
