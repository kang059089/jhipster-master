import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IUserVersionFile } from 'app/shared/model/user-version-file.model';
import { UserVersionFileService } from './user-version-file.service';

@Component({
    selector: 'jhi-user-version-file-update',
    templateUrl: './user-version-file-update.component.html'
})
export class UserVersionFileUpdateComponent implements OnInit {
    private _userVersionFile: IUserVersionFile;
    isSaving: boolean;
    versionReleaseDate: string;

    constructor(private userVersionFileService: UserVersionFileService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ userVersionFile }) => {
            this.userVersionFile = userVersionFile;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.userVersionFile.versionReleaseDate = moment(this.versionReleaseDate, DATE_TIME_FORMAT);
        if (this.userVersionFile.id !== undefined) {
            this.subscribeToSaveResponse(this.userVersionFileService.update(this.userVersionFile));
        } else {
            this.subscribeToSaveResponse(this.userVersionFileService.create(this.userVersionFile));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IUserVersionFile>>) {
        result.subscribe((res: HttpResponse<IUserVersionFile>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get userVersionFile() {
        return this._userVersionFile;
    }

    set userVersionFile(userVersionFile: IUserVersionFile) {
        this._userVersionFile = userVersionFile;
        this.versionReleaseDate = moment(userVersionFile.versionReleaseDate).format(DATE_TIME_FORMAT);
    }
}
