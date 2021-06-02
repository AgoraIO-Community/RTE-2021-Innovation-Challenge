import React from "react";
import {
  forwardRef,
  Input,
  NumberInput,
  NumberInputField,
  NumberInputStepper,
  NumberIncrementStepper,
  NumberDecrementStepper,
  Select,
  SelectProps,
  Switch,
} from "@chakra-ui/react";
import dayjs from "dayjs";
import { Renderer } from "./generated/renderer-types";
import { UseFormRegisterReturn } from "react-hook-form";

const Int: Renderer["query"]["Int"] = ({ value }) => {
  return <>{value}</>;
};
const Float: Renderer["query"]["Float"] = ({ value }) => {
  return <>{value}</>;
};
const String: Renderer["query"]["String"] = ({ value }) => {
  return <>{value}</>;
};
const Boolean: Renderer["query"]["Boolean"] = ({ value }) => {
  return <>{value}</>;
};
const Enum: Renderer["query"]["Enum"] = ({ value }) => {
  return <>{value}</>;
};
const DateTime: Renderer["query"]["DateTime"] = ({ value }) => {
  return <>{dayjs(value).format("DD/MM/YYYY HH:mm:ss")}</>;
};

const IntInput = forwardRef<UseFormRegisterReturn & { id?: string }, "input">(
  (props, ref) => {
    return (
      <NumberInput>
        <NumberInputField {...props} ref={ref} />
        <NumberInputStepper>
          <NumberIncrementStepper />
          <NumberDecrementStepper />
        </NumberInputStepper>
      </NumberInput>
    );
  }
);

const FloatInput = forwardRef<
  UseFormRegisterReturn & { id?: string; step?: number },
  "input"
>(({ step = 0.1, ...rest }, ref) => {
  return (
    <NumberInput step={step}>
      <NumberInputField {...rest} ref={ref} />
      <NumberInputStepper>
        <NumberIncrementStepper />
        <NumberDecrementStepper />
      </NumberInputStepper>
    </NumberInput>
  );
});

export const EnumInput = forwardRef<
  UseFormRegisterReturn & {
    id?: string;
    options: { text: string; value: string }[];
  } & SelectProps,
  "select"
>(({ options, ...rest }, ref) => {
  return (
    <Select {...rest} ref={ref}>
      {options.map((o) => (
        <option key={o.value} value={o.value}>
          {o.text}
        </option>
      ))}
    </Select>
  );
});

export const InputObjectInput = forwardRef<
  UseFormRegisterReturn & { id?: string },
  "input"
>((props, ref) => {
  return (
    <NumberInput>
      <NumberInputField
        {...props}
        ref={ref}
        onChange={(evt) => {
          console.log(evt);
          props.onChange(evt);
        }}
      />
      <NumberInputStepper>
        <NumberIncrementStepper />
        <NumberDecrementStepper />
      </NumberInputStepper>
    </NumberInput>
  );
});

export const renderer: Renderer = {
  query: {
    Int,
    Float,
    String,
    Boolean,
    Enum,
    DateTime,
  },
  mutation: {
    Int: IntInput,
    Float: FloatInput,
    String: Input,
    Boolean: Switch,
    Enum: EnumInput,
    DateTime: Input,
    InputObject: InputObjectInput,
  },
};
