import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVersionFile } from 'app/shared/model/version-file.model';
import { VersionFileService } from './version-file.service';

@Component({
    selector: 'jhi-version-file-delete-dialog',
    templateUrl: './version-file-delete-dialog.component.html'
})
export class VersionFileDeleteDialogComponent {
    versionFile: IVersionFile;

    constructor(
        private versionFileService: VersionFileService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.versionFileService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'versionFileListModification',
                content: 'Deleted an versionFile'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-version-file-delete-popup',
    template: ''
})
export class VersionFileDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ versionFile }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(VersionFileDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.versionFile = versionFile;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
