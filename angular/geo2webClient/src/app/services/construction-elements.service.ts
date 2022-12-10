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
  }

  changeConstructionElements(readElements: ConstructionElementRead[]) {
    let elements = readElements.map(element => new ConstructionElement(element));
    this.constructionElements.next(elements);
  }
}
