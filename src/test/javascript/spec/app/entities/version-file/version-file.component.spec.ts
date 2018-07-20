/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JhipsterMasterTestModule } from '../../../test.module';
import { VersionFileComponent } from 'app/entities/version-file/version-file.component';
import { VersionFileService } from 'app/entities/version-file/version-file.service';
import { VersionFile } from 'app/shared/model/version-file.model';

describe('Component Tests', () => {
    describe('VersionFile Management Component', () => {
        let comp: VersionFileComponent;
        let fixture: ComponentFixture<VersionFileComponent>;
        let service: VersionFileService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterMasterTestModule],
                declarations: [VersionFileComponent],
                providers: []
            })
                .overrideTemplate(VersionFileComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(VersionFileComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VersionFileService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new VersionFile(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.versionFiles[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
