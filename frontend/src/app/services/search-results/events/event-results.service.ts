import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../../../global/globals';
import {Observable} from 'rxjs';
import {Event} from '../../../dtos/event';

@Injectable({
  providedIn: 'root'
})
export class EventResultsService {

  private eventBaseUri: string = this.globals.backendUri + '/events';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  public findEventsFilteredByArtistID(id): Observable<Event[]> {
    console.log('Service Event-Results: findEventsFilteredByArtistID');
    return this.httpClient.get<Event[]>(this.eventBaseUri + '/artist/' + id);
  }

  public findEventsFilteredByAttributes(eventName, eventType, content, description): Observable<Event[]> {
    console.log('Service Event-Results: findEventsFilteredByAttributes');
    let parameters = new HttpParams();
    parameters = eventName ? parameters.append('eventName', eventName) : parameters;
    parameters = content ? parameters.append('content', content) : parameters;
    parameters = description ? parameters.append('description', description) : parameters;
    parameters = eventType ? parameters.append('eventType', eventType) : parameters;
    return this.httpClient.get<Event[]>(this.eventBaseUri, { params: parameters });
  }
}