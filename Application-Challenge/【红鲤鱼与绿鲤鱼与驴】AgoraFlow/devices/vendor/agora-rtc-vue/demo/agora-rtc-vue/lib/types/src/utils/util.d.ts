declare type IFunction<T> = (...argu: any[]) => T;
export declare const isFunction: (f: any) => f is IFunction<any>;
export declare const isPendingPromise: (ret: any) => boolean;
export declare const isArray: (a: any) => a is any[];
export declare const isPureObject: (v: any) => v is object;
export declare function arrayRemoveAfterFindIndex(array: any[], findIndex: IFunction<boolean>): void;
export declare function methodExist(object: {
    [k: string]: any;
    [j: number]: any;
}, methodName: string): boolean;
export declare function asyncTaskListBuilder(array: any[], builderMethod: IFunction<any>): Promise<any[]>;
export {};
