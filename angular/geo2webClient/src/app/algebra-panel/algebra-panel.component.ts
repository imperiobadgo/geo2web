import {Component, Injectable, OnInit} from '@angular/core';
import {ConstructionElementRead} from "../domain/construction-element/construction-element-read";
import {AlgebraPanelService} from "./services/algebra-panel.service";

@Component({
  selector: 'app-algebra-panel',
  templateUrl: './algebra-panel.component.html',
  styleUrls: ['./algebra-panel.component.scss']
})
@Injectable({
  providedIn: 'root'
})
export class AlgebraPanelComponent implements OnInit {

  constructionElements: ConstructionElementRead[] = [];

  constructor(private panelService: AlgebraPanelService) {
    //The AlgebraPanelService provides the data so that other components can also initiate an update.
    this.panelService.currentElements.subscribe(elements => this.constructionElements = elements);
  }

  ngOnInit(): void {
    this.refresh();
  }

  public refresh() {
    this.panelService.refresh();
  }

  public trackElement(index: number, item: ConstructionElementRead) {
    return item ? item.id : null;
  }

}
