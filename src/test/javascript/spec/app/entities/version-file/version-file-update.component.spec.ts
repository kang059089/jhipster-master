/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { JhipsterMasterTestModule } from '../../../test.module';
import { VersionFileUpdateComponent } from 'app/entities/version-file/version-file-update.component';
import { VersionFileService } from 'app/entities/version-file/version-file.service';
import { VersionFile } from 'app/shared/model/version-file.model';

describe('Component Tests', () => {
    describe('VersionFile Management Update Component', () => {
        let comp: VersionFileUpdateComponent;
        let fixture: ComponentFixture<VersionFileUpdateComponent>;
        let service: VersionFileService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterMasterTestModule],
                declarations: [VersionFileUpdateComponent]
            })
                .overrideTemplate(VersionFileUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(VersionFileUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VersionFileService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new VersionFile(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.versionFile = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new VersionFile();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.versionFile = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
