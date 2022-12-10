import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {ConstructionElementCreate} from "../domain/construction-element/construction-element-create";
import {ConstructionElementRead} from "../domain/construction-element/construction-element-read";
import {ConstructionElementWrite} from "../domain/construction-element/construction-element-write";

@Injectable({
  providedIn: 'root'
})
export class ConstructionElementCommunicationService {

  private apiServerUrl: string = environment.apiBaseUrl;

  private apiConstruction: string = 'construction';

  constructor(private http: HttpClient) {
  }

  public getConstructionElements(): Observable<ConstructionElementRead[]> {
    return this.http.get<ConstructionElementRead[]>(`${this.apiServerUrl}/${this.apiConstruction}`);
  }

  public getConstructionElement(id: string): Observable<ConstructionElementRead> {
    return this.http.get<ConstructionElementRead>(`${this.apiServerUrl}/${this.apiConstruction}/${id}`);
  }

  public addConstructionElement(element: ConstructionElementCreate): Observable<ConstructionElementRead> {
    // const headers = {'content-type': 'application/json'}//Set up the http.post for sending json
    // const body = JSON.stringify(element);//Convert to json for posting
    // console.log(body);
    return this.http.post<ConstructionElementRead>(`${this.apiServerUrl}/${this.apiConstruction}`, element);//, {'headers': headers});
  }

  public updateConstructionElement(element: ConstructionElementWrite): Observable<ConstructionElementRead> {
    // const headers = {'content-type': 'application/json'}//Set up the http.post for sending json
    // const body = JSON.stringify(element);//Convert to json for posting
    return this.http.put<ConstructionElementRead>(`${this.apiServerUrl}/${this.apiConstruction}`, element);// body, {'headers': headers});
  }

  public deleteConstructionElement(id: string): Observable<ConstructionElementRead> {
    return this.http.delete<ConstructionElementRead>(`${this.apiServerUrl}/${this.apiConstruction}/${id}`);
  }

  public deleteAllConstructionElements(): Observable<void> {
    return this.http.delete<void>(`${this.apiServerUrl}/${this.apiConstruction}`);
  }

}
