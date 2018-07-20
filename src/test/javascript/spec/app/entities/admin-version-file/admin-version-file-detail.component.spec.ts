/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterMasterTestModule } from '../../../test.module';
import { AdminVersionFileDetailComponent } from 'app/entities/admin-version-file/admin-version-file-detail.component';
import { AdminVersionFile } from 'app/shared/model/admin-version-file.model';

describe('Component Tests', () => {
    describe('AdminVersionFile Management Detail Component', () => {
        let comp: AdminVersionFileDetailComponent;
        let fixture: ComponentFixture<AdminVersionFileDetailComponent>;
        const route = ({ data: of({ adminVersionFile: new AdminVersionFile(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterMasterTestModule],
                declarations: [AdminVersionFileDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(AdminVersionFileDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AdminVersionFileDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.adminVersionFile).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
