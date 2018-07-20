import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterMasterSharedModule } from 'app/shared';
import {
    VersionFileComponent,
    VersionFileDetailComponent,
    VersionFileUpdateComponent,
    VersionFileDeletePopupComponent,
    VersionFileDeleteDialogComponent,
    versionFileRoute,
    versionFilePopupRoute
} from './';

const ENTITY_STATES = [...versionFileRoute, ...versionFilePopupRoute];

@NgModule({
    imports: [JhipsterMasterSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        VersionFileComponent,
        VersionFileDetailComponent,
        VersionFileUpdateComponent,
        VersionFileDeleteDialogComponent,
        VersionFileDeletePopupComponent
    ],
    entryComponents: [VersionFileComponent, VersionFileUpdateComponent, VersionFileDeleteDialogComponent, VersionFileDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterMasterVersionFileModule {}
