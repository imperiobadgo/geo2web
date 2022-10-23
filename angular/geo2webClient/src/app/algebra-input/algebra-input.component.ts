import {Component, OnInit} from '@angular/core';
import {ConstructionElementService} from "../services/construction-element.service";
import {ConstructionElementCreate} from "../domain/construction-element/construction-element-create";
import {AlgebraPanelService} from "../algebra-panel/services/algebra-panel.service";

@Component({
  selector: 'app-algebra-input',
  templateUrl: './algebra-input.component.html',
  styleUrls: ['./algebra-input.component.scss']
})
export class AlgebraInputComponent implements OnInit {

  input: string = "";
  suggestions: string[] = [];
  floatLabel: string = "Enter input";

  testSuggestions: string[] = [];

  constructor(private elementService: ConstructionElementService, private panelService: AlgebraPanelService) {
    this.clearInput();
    this.testSuggestions = [
      "a",
      "b",
      "c",
      "allow",
      "barrow"
    ];
  }

  ngOnInit(): void {
  }

  clearInput(): void {
    this.input = "";
  }

  search({event}: { event: any }) {
    let query = event.query;
    let filtered: any [] = [];
    if (query.length == 0){
      this.suggestions = this.testSuggestions;
      return;
    }
    let startSearchIndex = 0;
    let lastDevider = "";
    for (let i = 0; i < query.length; i++) {
      let current = query[i];
      if (current == "="){
        startSearchIndex = i;
        lastDevider = "=";
      }else if (current == ","){
        startSearchIndex = i;
        lastDevider = ",";
      }
    }
    let previousInput = query.substring(0, startSearchIndex);
    let searchInput = query.substring(startSearchIndex+lastDevider.length);
    for (let i = 0; i < this.testSuggestions.length; i++) {
      let test = this.testSuggestions[i];
      if (test.toLowerCase().includes(searchInput)){
        filtered.push(previousInput + lastDevider + test);
      }
    }
    this.suggestions = filtered;
  }

  onEnter(): void {
    let newElement: ConstructionElementCreate =
      {
        name: "",
        input: this.input
      };
    this.elementService.addConstructionElement(newElement).subscribe(
      () => {
        this.panelService.refresh();//Talk to the algebra panel through the service!!
        this.clearInput();
      });
  }

}
