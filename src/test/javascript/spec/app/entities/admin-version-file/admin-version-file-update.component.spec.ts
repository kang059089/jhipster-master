/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { JhipsterMasterTestModule } from '../../../test.module';
import { AdminVersionFileUpdateComponent } from 'app/entities/admin-version-file/admin-version-file-update.component';
import { AdminVersionFileService } from 'app/entities/admin-version-file/admin-version-file.service';
import { AdminVersionFile } from 'app/shared/model/admin-version-file.model';

describe('Component Tests', () => {
    describe('AdminVersionFile Management Update Component', () => {
        let comp: AdminVersionFileUpdateComponent;
        let fixture: ComponentFixture<AdminVersionFileUpdateComponent>;
        let service: AdminVersionFileService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterMasterTestModule],
                declarations: [AdminVersionFileUpdateComponent]
            })
                .overrideTemplate(AdminVersionFileUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AdminVersionFileUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AdminVersionFileService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new AdminVersionFile(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.adminVersionFile = entity;
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
                    const entity = new AdminVersionFile();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.adminVersionFile = entity;
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
