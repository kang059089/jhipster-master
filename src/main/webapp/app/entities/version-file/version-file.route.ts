import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { VersionFile } from 'app/shared/model/version-file.model';
import { VersionFileService } from './version-file.service';
import { VersionFileComponent } from './version-file.component';
import { VersionFileDetailComponent } from './version-file-detail.component';
import { VersionFileUpdateComponent } from './version-file-update.component';
import { VersionFileDeletePopupComponent } from './version-file-delete-dialog.component';
import { IVersionFile } from 'app/shared/model/version-file.model';

@Injectable({ providedIn: 'root' })
export class VersionFileResolve implements Resolve<IVersionFile> {
    constructor(private service: VersionFileService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((versionFile: HttpResponse<VersionFile>) => versionFile.body));
        }
        return of(new VersionFile());
    }
}

export const versionFileRoute: Routes = [
    {
        path: 'version-file',
        component: VersionFileComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterMasterApp.versionFile.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'version-file/:id/view',
        component: VersionFileDetailComponent,
        resolve: {
            versionFile: VersionFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterMasterApp.versionFile.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'version-file/new',
        component: VersionFileUpdateComponent,
        resolve: {
            versionFile: VersionFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterMasterApp.versionFile.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'version-file/:id/edit',
        component: VersionFileUpdateComponent,
        resolve: {
            versionFile: VersionFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterMasterApp.versionFile.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const versionFilePopupRoute: Routes = [
    {
        path: 'version-file/:id/delete',
        component: VersionFileDeletePopupComponent,
        resolve: {
            versionFile: VersionFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterMasterApp.versionFile.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
