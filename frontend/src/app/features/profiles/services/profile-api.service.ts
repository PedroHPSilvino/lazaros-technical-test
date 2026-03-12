import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../../environments/environment';
import { Profile } from '../../../shared/models/profile.model';
import { SaveProfileRequest } from '../../../shared/models/save-profile-request.model';

@Injectable({
  providedIn: 'root',
})
export class ProfileApiService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/profiles`;

  findAll(): Observable<Profile[]> {
    return this.http.get<Profile[]>(this.apiUrl);
  }

  findById(id: number): Observable<Profile> {
    return this.http.get<Profile>(`${this.apiUrl}/${id}`);
  }

  create(payload: SaveProfileRequest): Observable<Profile> {
    return this.http.post<Profile>(this.apiUrl, payload);
  }

  update(id: number, payload: SaveProfileRequest): Observable<Profile> {
    return this.http.put<Profile>(`${this.apiUrl}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}