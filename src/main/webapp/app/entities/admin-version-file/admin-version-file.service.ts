import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAdminVersionFile } from 'app/shared/model/admin-version-file.model';

type EntityResponseType = HttpResponse<IAdminVersionFile>;
type EntityArrayResponseType = HttpResponse<IAdminVersionFile[]>;

@Injectable({ providedIn: 'root' })
export class AdminVersionFileService {
    private resourceUrl = SERVER_API_URL + 'api/admin-version-files';

    constructor(private http: HttpClient) {}

    create(adminVersionFile: IAdminVersionFile): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(adminVersionFile);
        return this.http
            .post<IAdminVersionFile>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(adminVersionFile: IAdminVersionFile): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(adminVersionFile);
        return this.http
            .put<IAdminVersionFile>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IAdminVersionFile>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAdminVersionFile[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(adminVersionFile: IAdminVersionFile): IAdminVersionFile {
        const copy: IAdminVersionFile = Object.assign({}, adminVersionFile, {
            versionReleaseDate:
                adminVersionFile.versionReleaseDate != null && adminVersionFile.versionReleaseDate.isValid()
                    ? adminVersionFile.versionReleaseDate.toJSON()
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.versionReleaseDate = res.body.versionReleaseDate != null ? moment(res.body.versionReleaseDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((adminVersionFile: IAdminVersionFile) => {
            adminVersionFile.versionReleaseDate =
                adminVersionFile.versionReleaseDate != null ? moment(adminVersionFile.versionReleaseDate) : null;
        });
        return res;
    }
}
