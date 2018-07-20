/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterMasterTestModule } from '../../../test.module';
import { VersionFileDetailComponent } from 'app/entities/version-file/version-file-detail.component';
import { VersionFile } from 'app/shared/model/version-file.model';

describe('Component Tests', () => {
    describe('VersionFile Management Detail Component', () => {
        let comp: VersionFileDetailComponent;
        let fixture: ComponentFixture<VersionFileDetailComponent>;
        const route = ({ data: of({ versionFile: new VersionFile(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterMasterTestModule],
                declarations: [VersionFileDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(VersionFileDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(VersionFileDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.versionFile).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
