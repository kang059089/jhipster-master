/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JhipsterMasterTestModule } from '../../../test.module';
import { UserVersionFileComponent } from 'app/entities/user-version-file/user-version-file.component';
import { UserVersionFileService } from 'app/entities/user-version-file/user-version-file.service';
import { UserVersionFile } from 'app/shared/model/user-version-file.model';

describe('Component Tests', () => {
    describe('UserVersionFile Management Component', () => {
        let comp: UserVersionFileComponent;
        let fixture: ComponentFixture<UserVersionFileComponent>;
        let service: UserVersionFileService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterMasterTestModule],
                declarations: [UserVersionFileComponent],
                providers: []
            })
                .overrideTemplate(UserVersionFileComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(UserVersionFileComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserVersionFileService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new UserVersionFile(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.userVersionFiles[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
