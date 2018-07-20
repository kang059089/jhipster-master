import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IVersionFile } from 'app/shared/model/version-file.model';
import { Principal } from 'app/core';
import { VersionFileService } from './version-file.service';

@Component({
    selector: 'jhi-version-file',
    templateUrl: './version-file.component.html'
})
export class VersionFileComponent implements OnInit, OnDestroy {
    versionFiles: IVersionFile[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private versionFileService: VersionFileService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.versionFileService.query().subscribe(
            (res: HttpResponse<IVersionFile[]>) => {
                this.versionFiles = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInVersionFiles();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IVersionFile) {
        return item.id;
    }

    registerChangeInVersionFiles() {
        this.eventSubscriber = this.eventManager.subscribe('versionFileListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
