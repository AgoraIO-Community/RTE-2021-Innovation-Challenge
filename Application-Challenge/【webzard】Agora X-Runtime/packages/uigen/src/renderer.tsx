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
  Tag,
  Text,
  Avatar,
  Flex,
  Kbd,
} from "@chakra-ui/react";
import dayjs from "dayjs";
import { Renderer } from "./generated/renderer-types";
import { UseFormRegisterReturn, Controller } from "react-hook-form";
import { Role } from "./generated/data-components";
import DatePicker from "./ui-kit/DatePicker";

const Int: Renderer["query"]["Int"] = ({ value, context }) => {
  switch (true) {
    case context.path.endsWith("id"):
      return <Kbd shadow="2xl">{value}</Kbd>;
    default:
      break;
  }
  return <>{value}</>;
};
const Float: Renderer["query"]["Float"] = ({ value }) => {
  return <>{value}</>;
};
const String: Renderer["query"]["String"] = ({ value, context }) => {
  switch (true) {
    case context.type === "User" && context.path.endsWith("email"):
      return <Text as="u">{value}</Text>;
    case context.type === "User" && context.path.endsWith("name"):
      return (
        <Flex alignItems="center">
          <Avatar name={value} size="sm" mr={1} />
          {value}
        </Flex>
      );
    default:
      break;
  }
  return <>{value}</>;
};
const Boolean: Renderer["query"]["Boolean"] = ({ value }) => {
  return <>{value}</>;
};
const Enum: Renderer["query"]["Enum"] = ({ value, context }) => {
  switch (true) {
    case context.type === "User" && context.path.endsWith("role"):
      return (
        <Tag
          colorScheme={
            value === Role.Admin
              ? "red"
              : value === Role.Teacher
              ? "cyan"
              : "yellow"
          }
        >
          {value}
        </Tag>
      );
    default:
      break;
  }
  return <>{value}</>;
};
const DateTime: Renderer["query"]["DateTime"] = ({ value }) => {
  return <Text as="small">{dayjs(value).format("YYYY-MM-DD HH:mm:ss")}</Text>;
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
