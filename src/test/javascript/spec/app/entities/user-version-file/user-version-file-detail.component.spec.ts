/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterMasterTestModule } from '../../../test.module';
import { UserVersionFileDetailComponent } from 'app/entities/user-version-file/user-version-file-detail.component';
import { UserVersionFile } from 'app/shared/model/user-version-file.model';

describe('Component Tests', () => {
    describe('UserVersionFile Management Detail Component', () => {
        let comp: UserVersionFileDetailComponent;
        let fixture: ComponentFixture<UserVersionFileDetailComponent>;
        const route = ({ data: of({ userVersionFile: new UserVersionFile(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterMasterTestModule],
                declarations: [UserVersionFileDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(UserVersionFileDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(UserVersionFileDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.userVersionFile).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
