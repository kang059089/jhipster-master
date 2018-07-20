export interface IUserVersionfile {
    id?: number;
}

export class UserVersionfile implements IUserVersionfile {
    constructor(public id?: number) {}
}
