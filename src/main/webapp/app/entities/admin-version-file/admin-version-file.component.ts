import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IAdminVersionFile } from 'app/shared/model/admin-version-file.model';
import { Principal } from 'app/core';
import { AdminVersionFileService } from './admin-version-file.service';

@Component({
    selector: 'jhi-admin-version-file',
    templateUrl: './admin-version-file.component.html'
})
export class AdminVersionFileComponent implements OnInit, OnDestroy {
    adminVersionFiles: IAdminVersionFile[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private adminVersionFileService: AdminVersionFileService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.adminVersionFileService.query().subscribe(
            (res: HttpResponse<IAdminVersionFile[]>) => {
                this.adminVersionFiles = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInAdminVersionFiles();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IAdminVersionFile) {
        return item.id;
    }

    registerChangeInAdminVersionFiles() {
        this.eventSubscriber = this.eventManager.subscribe('adminVersionFileListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
