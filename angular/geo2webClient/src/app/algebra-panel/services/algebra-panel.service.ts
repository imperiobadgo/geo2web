import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {ConstructionElementRead} from "../../domain/construction-element/construction-element-read";
import {ConstructionElementService} from "../../services/construction-element.service";

@Injectable({
  providedIn: 'root'
})
export class AlgebraPanelService {


  private constructionElements = new BehaviorSubject<ConstructionElementRead[]>([]);
  currentElements = this.constructionElements.asObservable();

  constructor(private constructionElementService: ConstructionElementService) {
  }

  public refresh() {
    console.log("Refresh construction elements.");
    this.constructionElementService.getConstructionElements().subscribe(elements =>
      this.changeConstructionElements(elements)
    );
  }

  changeConstructionElements(elements: ConstructionElementRead[]) {
    this.constructionElements.next(elements);
  }
}
