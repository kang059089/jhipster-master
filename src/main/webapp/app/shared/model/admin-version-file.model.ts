import { Moment } from 'moment';

export interface IAdminVersionFile {
    id?: number;
    versionNo?: string;
    versionInfo?: string;
    forceUpdateState?: boolean;
    versionReleaseState?: boolean;
    appDownloadUrl?: string;
    versionReleaseDate?: Moment;
}

export class AdminVersionFile implements IAdminVersionFile {
    constructor(
        public id?: number,
        public versionNo?: string,
        public versionInfo?: string,
        public forceUpdateState?: boolean,
        public versionReleaseState?: boolean,
        public appDownloadUrl?: string,
        public versionReleaseDate?: Moment
    ) {
        this.forceUpdateState = false;
        this.versionReleaseState = false;
    }
}
