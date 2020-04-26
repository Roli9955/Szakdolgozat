import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  private get options() {
    const headers = {
      'Content-Type': 'application/json'
    };

    if (window.localStorage.getItem('token')) {
      headers['Authorization'] = `Basic ${window.localStorage.getItem('token')}`;
    }

    return {
      headers: new HttpHeaders(headers)
    }
  };

  private url: String = "http://localhost:8081/";

  constructor(
    private httpClient: HttpClient
  ) { }

  public get<T>(route): Promise<T> {
    return this.httpClient.get(this.url + route, this.options).toPromise() as Promise<T>;
  }

  public post<T>(route, body): Promise<T> {
    return this.httpClient.post(this.url + route, body, this.options).toPromise() as Promise<T>;
  }

  public delete<T>(route): Promise<T> {
    return this.httpClient.delete(this.url + route, this.options).toPromise() as Promise<T>;
  }

  public put<T>(route, body): Promise<T> {
    return this.httpClient.put(this.url + route, body, this.options).toPromise() as Promise<T>;
  }

  public downloadFile<T>(route): any {
    return this.httpClient.get(this.url + route, {
      responseType: 'arraybuffer', headers: this.options.headers
    })
  }
  
}