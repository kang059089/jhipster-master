import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterMasterSharedModule } from 'app/shared';
import {
    AdminVersionFileComponent,
    AdminVersionFileDetailComponent,
    AdminVersionFileUpdateComponent,
    AdminVersionFileDeletePopupComponent,
    AdminVersionFileDeleteDialogComponent,
    adminVersionFileRoute,
    adminVersionFilePopupRoute
} from './';

const ENTITY_STATES = [...adminVersionFileRoute, ...adminVersionFilePopupRoute];

@NgModule({
    imports: [JhipsterMasterSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AdminVersionFileComponent,
        AdminVersionFileDetailComponent,
        AdminVersionFileUpdateComponent,
        AdminVersionFileDeleteDialogComponent,
        AdminVersionFileDeletePopupComponent
    ],
    entryComponents: [
        AdminVersionFileComponent,
        AdminVersionFileUpdateComponent,
        AdminVersionFileDeleteDialogComponent,
        AdminVersionFileDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterMasterAdminVersionFileModule {}
