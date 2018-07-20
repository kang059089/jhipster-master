import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserVersionFile } from 'app/shared/model/user-version-file.model';

@Component({
    selector: 'jhi-user-version-file-detail',
    templateUrl: './user-version-file-detail.component.html'
})
export class UserVersionFileDetailComponent implements OnInit {
    userVersionFile: IUserVersionFile;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ userVersionFile }) => {
            this.userVersionFile = userVersionFile;
        });
    }

    previousState() {
        window.history.back();
    }
}
