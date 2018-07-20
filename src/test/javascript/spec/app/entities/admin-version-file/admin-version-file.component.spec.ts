/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JhipsterMasterTestModule } from '../../../test.module';
import { AdminVersionFileComponent } from 'app/entities/admin-version-file/admin-version-file.component';
import { AdminVersionFileService } from 'app/entities/admin-version-file/admin-version-file.service';
import { AdminVersionFile } from 'app/shared/model/admin-version-file.model';

describe('Component Tests', () => {
    describe('AdminVersionFile Management Component', () => {
        let comp: AdminVersionFileComponent;
        let fixture: ComponentFixture<AdminVersionFileComponent>;
        let service: AdminVersionFileService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterMasterTestModule],
                declarations: [AdminVersionFileComponent],
                providers: []
            })
                .overrideTemplate(AdminVersionFileComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AdminVersionFileComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AdminVersionFileService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new AdminVersionFile(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.adminVersionFiles[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
