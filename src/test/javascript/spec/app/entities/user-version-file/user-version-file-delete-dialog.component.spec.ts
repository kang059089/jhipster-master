/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { JhipsterMasterTestModule } from '../../../test.module';
import { UserVersionFileDeleteDialogComponent } from 'app/entities/user-version-file/user-version-file-delete-dialog.component';
import { UserVersionFileService } from 'app/entities/user-version-file/user-version-file.service';

describe('Component Tests', () => {
    describe('UserVersionFile Management Delete Component', () => {
        let comp: UserVersionFileDeleteDialogComponent;
        let fixture: ComponentFixture<UserVersionFileDeleteDialogComponent>;
        let service: UserVersionFileService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterMasterTestModule],
                declarations: [UserVersionFileDeleteDialogComponent]
            })
                .overrideTemplate(UserVersionFileDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(UserVersionFileDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserVersionFileService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
