import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IVersionFile } from 'app/shared/model/version-file.model';
import { VersionFileService } from './version-file.service';

@Component({
    selector: 'jhi-version-file-update',
    templateUrl: './version-file-update.component.html'
})
export class VersionFileUpdateComponent implements OnInit {
    private _versionFile: IVersionFile;
    isSaving: boolean;

    constructor(private versionFileService: VersionFileService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ versionFile }) => {
            this.versionFile = versionFile;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.versionFile.id !== undefined) {
            this.subscribeToSaveResponse(this.versionFileService.update(this.versionFile));
        } else {
            this.subscribeToSaveResponse(this.versionFileService.create(this.versionFile));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IVersionFile>>) {
        result.subscribe((res: HttpResponse<IVersionFile>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get versionFile() {
        return this._versionFile;
    }

    set versionFile(versionFile: IVersionFile) {
        this._versionFile = versionFile;
    }
}
