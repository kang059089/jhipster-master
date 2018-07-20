import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IVersionFile } from 'app/shared/model/version-file.model';

type EntityResponseType = HttpResponse<IVersionFile>;
type EntityArrayResponseType = HttpResponse<IVersionFile[]>;

@Injectable({ providedIn: 'root' })
export class VersionFileService {
    private resourceUrl = SERVER_API_URL + 'api/version-files';

    constructor(private http: HttpClient) {}

    create(versionFile: IVersionFile): Observable<EntityResponseType> {
        return this.http.post<IVersionFile>(this.resourceUrl, versionFile, { observe: 'response' });
    }

    update(versionFile: IVersionFile): Observable<EntityResponseType> {
        return this.http.put<IVersionFile>(this.resourceUrl, versionFile, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IVersionFile>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IVersionFile[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
