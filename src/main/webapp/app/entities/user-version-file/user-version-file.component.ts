import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IUserVersionFile } from 'app/shared/model/user-version-file.model';
import { Principal } from 'app/core';
import { UserVersionFileService } from './user-version-file.service';

@Component({
    selector: 'jhi-user-version-file',
    templateUrl: './user-version-file.component.html'
})
export class UserVersionFileComponent implements OnInit, OnDestroy {
    userVersionFiles: IUserVersionFile[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private userVersionFileService: UserVersionFileService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.userVersionFileService.query().subscribe(
            (res: HttpResponse<IUserVersionFile[]>) => {
                this.userVersionFiles = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInUserVersionFiles();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IUserVersionFile) {
        return item.id;
    }

    registerChangeInUserVersionFiles() {
        this.eventSubscriber = this.eventManager.subscribe('userVersionFileListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
