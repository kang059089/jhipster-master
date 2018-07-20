import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterMasterSharedModule } from 'app/shared';
import {
    UserVersionFileComponent,
    UserVersionFileDetailComponent,
    UserVersionFileUpdateComponent,
    UserVersionFileDeletePopupComponent,
    UserVersionFileDeleteDialogComponent,
    userVersionFileRoute,
    userVersionFilePopupRoute
} from './';

const ENTITY_STATES = [...userVersionFileRoute, ...userVersionFilePopupRoute];

@NgModule({
    imports: [JhipsterMasterSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        UserVersionFileComponent,
        UserVersionFileDetailComponent,
        UserVersionFileUpdateComponent,
        UserVersionFileDeleteDialogComponent,
        UserVersionFileDeletePopupComponent
    ],
    entryComponents: [
        UserVersionFileComponent,
        UserVersionFileUpdateComponent,
        UserVersionFileDeleteDialogComponent,
        UserVersionFileDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterMasterUserVersionFileModule {}
