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
  floatLabel: string = "Enter input";

  constructor(private elementService: ConstructionElementService, private panelService: AlgebraPanelService) {
    this.clearInput();
  }

  ngOnInit(): void {
  }

  clearInput(): void {
    this.input = "";
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
