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

export type QueryContext = {
  path: string;
  type: string;
  component: string;
}

export type Renderer = {
  query: {
    Int: React.FC<{ value: number; context: QueryContext }>;
    Float: React.FC<{ value: number; context: QueryContext }>;
    String: React.FC<{ value: string; context: QueryContext }>;
    Boolean: React.FC<{ value: boolean; context: QueryContext }>;
    Enum: React.FC<{ value: string; context: QueryContext }>;
    DateTime: React.FC<{ value: string; context: QueryContext }>;
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
    InputObject: React.FC<UseFormRegisterReturn & { id?: string; defaultValue?: any }>;
    DateTime: React.FC<UseFormRegisterReturn & { id?: string }>;
  };
};
