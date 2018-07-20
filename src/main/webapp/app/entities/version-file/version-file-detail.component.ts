import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVersionFile } from 'app/shared/model/version-file.model';

@Component({
    selector: 'jhi-version-file-detail',
    templateUrl: './version-file-detail.component.html'
})
export class VersionFileDetailComponent implements OnInit {
    versionFile: IVersionFile;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ versionFile }) => {
            this.versionFile = versionFile;
        });
    }

    previousState() {
        window.history.back();
    }
}
