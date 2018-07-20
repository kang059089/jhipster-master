import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAdminVersionFile } from 'app/shared/model/admin-version-file.model';

@Component({
    selector: 'jhi-admin-version-file-detail',
    templateUrl: './admin-version-file-detail.component.html'
})
export class AdminVersionFileDetailComponent implements OnInit {
    adminVersionFile: IAdminVersionFile;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ adminVersionFile }) => {
            this.adminVersionFile = adminVersionFile;
        });
    }

    previousState() {
        window.history.back();
    }
}
