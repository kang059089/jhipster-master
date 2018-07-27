import { Component, AfterViewInit, Renderer, ElementRef } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { JhiEventManager } from 'ng-jhipster';
import { LocalStorageService } from 'ngx-webstorage';

import { LoginService } from 'app/core/login/login.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';

import { AesServerProvider } from '../../core/auth/aes.service';
import { RsaServerProvider } from '../../core/auth/rsa.service';
import { SERVER_API_URL } from 'app/app.constants';

@Component({
    selector: 'jhi-login-modal',
    templateUrl: './login.component.html'
})
export class JhiLoginModalComponent implements AfterViewInit {
    authenticationError: boolean;
    password: string;
    rememberMe: boolean;
    username: string;
    credentials: any;

    constructor(
        private eventManager: JhiEventManager,
        private loginService: LoginService,
        private stateStorageService: StateStorageService,
        private elementRef: ElementRef,
        private renderer: Renderer,
        private router: Router,
        public activeModal: NgbActiveModal,
        private rsaServerProvider: RsaServerProvider,
        private aesServerProvider: AesServerProvider,
        private $localStorage: LocalStorageService,
        private http: HttpClient
    ) {
        this.credentials = {};
        const aesKey = this.aesServerProvider.randomString(16);
        let uuid = '';
        this.$localStorage.store('aesKey', aesKey);
        const aeskeyStr = rsaServerProvider.encrypt(aesKey);

        this.http.post(SERVER_API_URL + 'api/init', aeskeyStr).subscribe(
            res => {
                const uuidStr = res['uuid'];
                this.$localStorage.store('uuid', uuidStr);
            },
            err => {
                // 失败回调
                console.log(err);
            }
        );
    }

    ngAfterViewInit() {
        this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#username'), 'focus', []);
    }

    cancel() {
        this.credentials = {
            username: null,
            password: null,
            rememberMe: true
        };
        this.authenticationError = false;
        this.activeModal.dismiss('cancel');
    }

    login() {
        let useInfo = this.username + '####' + this.password;
        useInfo = this.aesServerProvider.encrypt(useInfo, this.$localStorage.retrieve('aesKey'));
        this.loginService
            .login({
                username: this.username,
                password: this.password,
                rememberMe: this.rememberMe,
                useInfo: useInfo
            })
            .then(() => {
                this.authenticationError = false;
                this.activeModal.dismiss('login success');
                if (this.router.url === '/register' || /^\/activate\//.test(this.router.url) || /^\/reset\//.test(this.router.url)) {
                    this.router.navigate(['']);
                }

                this.eventManager.broadcast({
                    name: 'authenticationSuccess',
                    content: 'Sending Authentication Success'
                });

                // // previousState was set in the authExpiredInterceptor before being redirected to login modal.
                // // since login is succesful, go to stored previousState and clear previousState
                const redirect = this.stateStorageService.getUrl();
                if (redirect) {
                    this.stateStorageService.storeUrl(null);
                    this.router.navigate([redirect]);
                }
            })
            .catch(() => {
                this.authenticationError = true;
            });
    }

    register() {
        this.activeModal.dismiss('to state register');
        this.router.navigate(['/register']);
    }

    requestResetPassword() {
        this.activeModal.dismiss('to state requestReset');
        this.router.navigate(['/reset', 'request']);
    }
}
