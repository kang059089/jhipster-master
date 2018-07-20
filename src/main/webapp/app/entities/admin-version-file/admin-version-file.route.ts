import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { AdminVersionFile } from 'app/shared/model/admin-version-file.model';
import { AdminVersionFileService } from './admin-version-file.service';
import { AdminVersionFileComponent } from './admin-version-file.component';
import { AdminVersionFileDetailComponent } from './admin-version-file-detail.component';
import { AdminVersionFileUpdateComponent } from './admin-version-file-update.component';
import { AdminVersionFileDeletePopupComponent } from './admin-version-file-delete-dialog.component';
import { IAdminVersionFile } from 'app/shared/model/admin-version-file.model';

@Injectable({ providedIn: 'root' })
export class AdminVersionFileResolve implements Resolve<IAdminVersionFile> {
    constructor(private service: AdminVersionFileService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((adminVersionFile: HttpResponse<AdminVersionFile>) => adminVersionFile.body));
        }
        return of(new AdminVersionFile());
    }
}

export const adminVersionFileRoute: Routes = [
    {
        path: 'admin-version-file',
        component: AdminVersionFileComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterMasterApp.adminVersionFile.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'admin-version-file/:id/view',
        component: AdminVersionFileDetailComponent,
        resolve: {
            adminVersionFile: AdminVersionFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterMasterApp.adminVersionFile.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'admin-version-file/new',
        component: AdminVersionFileUpdateComponent,
        resolve: {
            adminVersionFile: AdminVersionFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterMasterApp.adminVersionFile.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'admin-version-file/:id/edit',
        component: AdminVersionFileUpdateComponent,
        resolve: {
            adminVersionFile: AdminVersionFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterMasterApp.adminVersionFile.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const adminVersionFilePopupRoute: Routes = [
    {
        path: 'admin-version-file/:id/delete',
        component: AdminVersionFileDeletePopupComponent,
        resolve: {
            adminVersionFile: AdminVersionFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterMasterApp.adminVersionFile.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
