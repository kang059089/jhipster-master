import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { JhipsterMasterVersionFileModule } from './version-file/version-file.module';
import { JhipsterMasterAdminVersionFileModule } from './admin-version-file/admin-version-file.module';
import { JhipsterMasterUserVersionFileModule } from './user-version-file/user-version-file.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        JhipsterMasterVersionFileModule,
        JhipsterMasterAdminVersionFileModule,
        JhipsterMasterUserVersionFileModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterMasterEntityModule {}
