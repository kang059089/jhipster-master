import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAdminVersionFile } from 'app/shared/model/admin-version-file.model';
import { AdminVersionFileService } from './admin-version-file.service';

@Component({
    selector: 'jhi-admin-version-file-delete-dialog',
    templateUrl: './admin-version-file-delete-dialog.component.html'
})
export class AdminVersionFileDeleteDialogComponent {
    adminVersionFile: IAdminVersionFile;

    constructor(
        private adminVersionFileService: AdminVersionFileService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.adminVersionFileService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'adminVersionFileListModification',
                content: 'Deleted an adminVersionFile'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-admin-version-file-delete-popup',
    template: ''
})
export class AdminVersionFileDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ adminVersionFile }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AdminVersionFileDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.adminVersionFile = adminVersionFile;
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
