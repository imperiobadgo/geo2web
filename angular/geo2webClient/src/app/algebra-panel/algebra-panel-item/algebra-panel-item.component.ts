import {Component, Input, OnInit, SimpleChanges} from '@angular/core';
import {ConstructionElementRead} from "../../domain/construction-element/construction-element-read";
import {emptyConstructionElementRead} from "../../domain/construction-element/construction-element-factory";
import {ConstructionElementCommunicationService} from "../../services/construction-element-communication.service";
import {ConstructionElement} from "../../domain/construction-element/construction-element";
import {ConstructionElementsService} from "../../services/construction-elements.service";

@Component({
  selector: 'app-algebra-panel-item',
  templateUrl: './algebra-panel-item.component.html',
  styleUrls: ['./algebra-panel-item.component.scss']
})
export class AlgebraPanelItemComponent implements OnInit {

  @Input() constructionElement: ConstructionElement;
  suggestions: string[] = [];


  constructor(private elementCommunicationService: ConstructionElementCommunicationService, private elementService: ConstructionElementsService) {
    this.constructionElement = new ConstructionElement(emptyConstructionElementRead());
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges){
    this.constructionElement.input = changes["constructionElement"].currentValue.input;
  }

  search({event}: { event: any }) {
    let query = event.query;
    let filtered: any [] = [];
    this.suggestions = filtered;
  }

  onEnter(): void {
    let newElement = this.constructionElement.createWriteConstructionElement();
    this.elementCommunicationService.updateConstructionElement(newElement).subscribe(
      (element: ConstructionElementRead) => {
        this.constructionElement = new ConstructionElement(element);
      });
  }

  onDelete(): void {
    this.elementCommunicationService.deleteConstructionElement(this.constructionElement.id).subscribe(
      (element: ConstructionElementRead) => {
        this.constructionElement = new ConstructionElement(element);
        this.elementService.refresh();//Talk to the algebra panel through the service!!
      });
  }
}
