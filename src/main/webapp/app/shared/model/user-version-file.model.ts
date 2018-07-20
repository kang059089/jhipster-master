import { Moment } from 'moment';

export interface IUserVersionFile {
    id?: number;
    userId?: number;
    versionNo?: string;
    versionInfo?: string;
    versionReleaseDate?: Moment;
}

export class UserVersionFile implements IUserVersionFile {
    constructor(
        public id?: number,
        public userId?: number,
        public versionNo?: string,
        public versionInfo?: string,
        public versionReleaseDate?: Moment
    ) {}
}
