import {Injectable} from '@angular/core';
import {ConstructionElementCommunicationService} from "./construction-element-communication.service";
import {ConstructionElement} from "../domain/construction-element/construction-element";
import {BehaviorSubject} from "rxjs";
import {ConstructionElementRead} from "../domain/construction-element/construction-element-read";

@Injectable({
  providedIn: 'root'
})
export class ConstructionElementsService {

  private constructionElements = new BehaviorSubject<ConstructionElement[]>([]);
  currentElements = this.constructionElements.asObservable();

  constructor(private communicationService: ConstructionElementCommunicationService) {
  }

  public refresh() {
    console.log("Refresh construction elements.");
    this.communicationService.getConstructionElements().subscribe(elements =>
      this.changeConstructionElements(elements)
    );
    // this.createDebugElements();
  }

  createDebugElements() {
    let elements :ConstructionElement[] = [];
    elements.push(new ConstructionElement(new class implements ConstructionElementRead {
      constructionIndex: number = 0;
      id: string = '00';
      input: string = 'sin(x)';
      name: string = 'a';
      output: string = '';
      shaderContent: string = 'sin(x)';
      transform: number[] = [1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1];
    }()))
    this.constructionElements.next(elements);
  }

  changeConstructionElements(readElements: ConstructionElementRead[]) {
    let elements = readElements.map(element => new ConstructionElement(element));
    this.constructionElements.next(elements);
  }
}
