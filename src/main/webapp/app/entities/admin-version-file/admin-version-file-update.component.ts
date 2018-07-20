import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IAdminVersionFile } from 'app/shared/model/admin-version-file.model';
import { AdminVersionFileService } from './admin-version-file.service';

@Component({
    selector: 'jhi-admin-version-file-update',
    templateUrl: './admin-version-file-update.component.html'
})
export class AdminVersionFileUpdateComponent implements OnInit {
    private _adminVersionFile: IAdminVersionFile;
    isSaving: boolean;
    versionReleaseDate: string;

    constructor(private adminVersionFileService: AdminVersionFileService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ adminVersionFile }) => {
            this.adminVersionFile = adminVersionFile;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.adminVersionFile.versionReleaseDate = moment(this.versionReleaseDate, DATE_TIME_FORMAT);
        if (this.adminVersionFile.id !== undefined) {
            this.subscribeToSaveResponse(this.adminVersionFileService.update(this.adminVersionFile));
        } else {
            this.subscribeToSaveResponse(this.adminVersionFileService.create(this.adminVersionFile));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAdminVersionFile>>) {
        result.subscribe((res: HttpResponse<IAdminVersionFile>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get adminVersionFile() {
        return this._adminVersionFile;
    }

    set adminVersionFile(adminVersionFile: IAdminVersionFile) {
        this._adminVersionFile = adminVersionFile;
        this.versionReleaseDate = moment(adminVersionFile.versionReleaseDate).format(DATE_TIME_FORMAT);
    }
}
