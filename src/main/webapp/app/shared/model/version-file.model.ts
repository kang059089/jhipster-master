export interface IVersionFile {
    id?: number;
    versionNo?: string;
    versionInfo?: string;
    updateDate?: string;
    latestVersionNo?: string;
    latestVersionInfo?: string;
    latestUpdateDate?: string;
    appDownloadUrl?: string;
}

export class VersionFile implements IVersionFile {
    constructor(
        public id?: number,
        public versionNo?: string,
        public versionInfo?: string,
        public updateDate?: string,
        public latestVersionNo?: string,
        public latestVersionInfo?: string,
        public latestUpdateDate?: string,
        public appDownloadUrl?: string
    ) {}
}
