import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserVersionFile } from 'app/shared/model/user-version-file.model';
import { UserVersionFileService } from './user-version-file.service';

@Component({
    selector: 'jhi-user-version-file-delete-dialog',
    templateUrl: './user-version-file-delete-dialog.component.html'
})
export class UserVersionFileDeleteDialogComponent {
    userVersionFile: IUserVersionFile;

    constructor(
        private userVersionFileService: UserVersionFileService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.userVersionFileService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'userVersionFileListModification',
                content: 'Deleted an userVersionFile'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-user-version-file-delete-popup',
    template: ''
})
export class UserVersionFileDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ userVersionFile }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(UserVersionFileDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.userVersionFile = userVersionFile;
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
