import React from "react";
import { CombinedError } from "urql";
import { UseFormRegisterReturn } from "react-hook-form";
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
  query: {
    Int: React.FC<{ value: number }>;
    Float: React.FC<{ value: number }>;
    String: React.FC<{ value: string }>;
    Boolean: React.FC<{ value: boolean }>;
    Enum: React.FC<{ value: string }>;
    DateTime: React.FC<{ value: string }>;
        User?: ((result: AsyncResult<User>) => React.ReactNode)
      | FieldsRenderer<User>;
        Class?: ((result: AsyncResult<Class>) => React.ReactNode)
      | FieldsRenderer<Class>;
        Lesson?: ((result: AsyncResult<Lesson>) => React.ReactNode)
      | FieldsRenderer<Lesson>;
      };
  mutation: {
    Int: React.FC<UseFormRegisterReturn & { id?: string }>;
    Float: React.FC<UseFormRegisterReturn & { id?: string }>;
    String: React.FC<UseFormRegisterReturn & { id?: string }>;
    Boolean: React.FC<UseFormRegisterReturn & { id?: string }>;
    Enum: React.FC<
      UseFormRegisterReturn & {
        id?: string;
        options: { text: string; value: string }[];
      }
    >;
    DateTime: React.FC<UseFormRegisterReturn & { id?: string }>;
  };
};
