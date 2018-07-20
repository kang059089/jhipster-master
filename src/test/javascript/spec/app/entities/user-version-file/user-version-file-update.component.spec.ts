/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { JhipsterMasterTestModule } from '../../../test.module';
import { UserVersionFileUpdateComponent } from 'app/entities/user-version-file/user-version-file-update.component';
import { UserVersionFileService } from 'app/entities/user-version-file/user-version-file.service';
import { UserVersionFile } from 'app/shared/model/user-version-file.model';

describe('Component Tests', () => {
    describe('UserVersionFile Management Update Component', () => {
        let comp: UserVersionFileUpdateComponent;
        let fixture: ComponentFixture<UserVersionFileUpdateComponent>;
        let service: UserVersionFileService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterMasterTestModule],
                declarations: [UserVersionFileUpdateComponent]
            })
                .overrideTemplate(UserVersionFileUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(UserVersionFileUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserVersionFileService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new UserVersionFile(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.userVersionFile = entity;
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
                    const entity = new UserVersionFile();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.userVersionFile = entity;
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
