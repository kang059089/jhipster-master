import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { UserVersionFile } from 'app/shared/model/user-version-file.model';
import { UserVersionFileService } from './user-version-file.service';
import { UserVersionFileComponent } from './user-version-file.component';
import { UserVersionFileDetailComponent } from './user-version-file-detail.component';
import { UserVersionFileUpdateComponent } from './user-version-file-update.component';
import { UserVersionFileDeletePopupComponent } from './user-version-file-delete-dialog.component';
import { IUserVersionFile } from 'app/shared/model/user-version-file.model';

@Injectable({ providedIn: 'root' })
export class UserVersionFileResolve implements Resolve<IUserVersionFile> {
    constructor(private service: UserVersionFileService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((userVersionFile: HttpResponse<UserVersionFile>) => userVersionFile.body));
        }
        return of(new UserVersionFile());
    }
}

export const userVersionFileRoute: Routes = [
    {
        path: 'user-version-file',
        component: UserVersionFileComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterMasterApp.userVersionFile.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-version-file/:id/view',
        component: UserVersionFileDetailComponent,
        resolve: {
            userVersionFile: UserVersionFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterMasterApp.userVersionFile.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-version-file/new',
        component: UserVersionFileUpdateComponent,
        resolve: {
            userVersionFile: UserVersionFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterMasterApp.userVersionFile.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-version-file/:id/edit',
        component: UserVersionFileUpdateComponent,
        resolve: {
            userVersionFile: UserVersionFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterMasterApp.userVersionFile.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const userVersionFilePopupRoute: Routes = [
    {
        path: 'user-version-file/:id/delete',
        component: UserVersionFileDeletePopupComponent,
        resolve: {
            userVersionFile: UserVersionFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterMasterApp.userVersionFile.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
