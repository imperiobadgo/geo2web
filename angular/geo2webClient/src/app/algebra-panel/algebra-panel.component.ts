import {Component, Injectable, OnInit} from '@angular/core';
import {ConstructionElementRead} from "../domain/construction-element/construction-element-read";
import {ConstructionElementsService} from "../services/construction-elements.service";
import {ConstructionElement} from "../domain/construction-element/construction-element";

@Component({
  selector: 'app-algebra-panel',
  templateUrl: './algebra-panel.component.html',
  styleUrls: ['./algebra-panel.component.scss']
})
@Injectable({
  providedIn: 'root'
})
export class AlgebraPanelComponent implements OnInit {

  constructionElements: ConstructionElement[] = [];

  constructor(private elementService: ConstructionElementsService) {
    //The AlgebraPanelService provides the data so that other components can also initiate an update.
    this.elementService.currentElements.subscribe(elements => this.constructionElements = elements);
  }

  ngOnInit(): void {
    this.refresh();
  }

  public refresh() {
    this.elementService.refresh();
  }

  public trackElement(index: number, item: ConstructionElementRead) {
    return item ? item.id : null;
  }

}
