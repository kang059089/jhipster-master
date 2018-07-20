import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IUserVersionFile } from 'app/shared/model/user-version-file.model';

type EntityResponseType = HttpResponse<IUserVersionFile>;
type EntityArrayResponseType = HttpResponse<IUserVersionFile[]>;

@Injectable({ providedIn: 'root' })
export class UserVersionFileService {
    private resourceUrl = SERVER_API_URL + 'api/user-version-files';

    constructor(private http: HttpClient) {}

    create(userVersionFile: IUserVersionFile): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(userVersionFile);
        return this.http
            .post<IUserVersionFile>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(userVersionFile: IUserVersionFile): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(userVersionFile);
        return this.http
            .put<IUserVersionFile>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IUserVersionFile>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IUserVersionFile[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(userVersionFile: IUserVersionFile): IUserVersionFile {
        const copy: IUserVersionFile = Object.assign({}, userVersionFile, {
            versionReleaseDate:
                userVersionFile.versionReleaseDate != null && userVersionFile.versionReleaseDate.isValid()
                    ? userVersionFile.versionReleaseDate.toJSON()
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.versionReleaseDate = res.body.versionReleaseDate != null ? moment(res.body.versionReleaseDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((userVersionFile: IUserVersionFile) => {
            userVersionFile.versionReleaseDate =
                userVersionFile.versionReleaseDate != null ? moment(userVersionFile.versionReleaseDate) : null;
        });
        return res;
    }
}
