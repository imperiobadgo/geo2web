import {Component, Input, OnInit, SimpleChanges} from '@angular/core';
import {ConstructionElementRead} from "../../domain/construction-element/construction-element-read";
import {emptyConstructionElementRead} from "../../domain/construction-element/construction-element-factory";
import {ConstructionElementWrite} from "../../domain/construction-element/construction-element-write";
import {ConstructionElementService} from "../../services/construction-element.service";
import {AlgebraPanelService} from "../services/algebra-panel.service";

@Component({
  selector: 'app-algebra-panel-item',
  templateUrl: './algebra-panel-item.component.html',
  styleUrls: ['./algebra-panel-item.component.scss']
})
export class AlgebraPanelItemComponent implements OnInit {

  @Input() constructionElement: ConstructionElementRead;
  inputEdit: string = "";
  suggestions: string[] = [];
  nameEdit: string = "";


  constructor(private elementService: ConstructionElementService, private panelService: AlgebraPanelService) {
    this.constructionElement = emptyConstructionElementRead();

  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges){
    this.inputEdit = changes["constructionElement"].currentValue.input;
  }

  search({event}: { event: any }) {
    let query = event.query;
    let filtered: any [] = [];
    this.suggestions = filtered;
  }

  onEnter(): void {
    let newElement: ConstructionElementWrite =
      {
        id: this.constructionElement.id,
        name: this.constructionElement.name,
        input: this.inputEdit
      };
    this.elementService.updateConstructionElement(newElement).subscribe(
      (element: ConstructionElementRead) => {
        this.constructionElement = element;
      });
  }

  onDelete(): void {
    this.elementService.deleteConstructionElement(this.constructionElement.id).subscribe(
      (element: ConstructionElementRead) => {
        this.constructionElement = element;
        this.panelService.refresh();//Talk to the algebra panel through the service!!
      });
  }
}
