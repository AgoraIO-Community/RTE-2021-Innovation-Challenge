import React from "react";
import { CombinedError } from "urql";
import { User, Class, Lesson } from "./data-components";

export type AsyncResult<T> = {
  data: T;
  fetching: boolean;
  error: CombinedError | undefined | null;
};

export type FieldsRenderer<T> = {
  [K in keyof T]?: (result: AsyncResult<T[K]>) => React.ReactNode;
};

export type Renderer = {
    User?: ((result: AsyncResult<User>) => React.ReactNode)
    | FieldsRenderer<User>;
    Class?: ((result: AsyncResult<Class>) => React.ReactNode)
    | FieldsRenderer<Class>;
    Lesson?: ((result: AsyncResult<Lesson>) => React.ReactNode)
    | FieldsRenderer<Lesson>;
  };
