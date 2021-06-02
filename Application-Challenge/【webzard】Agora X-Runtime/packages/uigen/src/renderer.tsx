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
  Modal,
  ModalOverlay,
  ModalContent,
  ModalCloseButton,
  ModalBody,
  Button,
  useDisclosure,
  Image,
} from "@chakra-ui/react";
import dayjs from "dayjs";
import { Renderer } from "./generated/renderer-types";
import { UseFormRegisterReturn } from "react-hook-form";
import { Role } from "./generated/data-components";
import { AgoraClassRoom, AgoraClassRoomProps } from "./custom-components/Agora";

const ClassRoomModal: React.FC<AgoraClassRoomProps> = (props) => {
  const { isOpen, onOpen, onClose } = useDisclosure();

  return (
    <>
      <Button onClick={onOpen} variant="solid">
        {props.name}
      </Button>
      <Modal isOpen={isOpen} onClose={onClose} size="full">
        <ModalOverlay />
        <ModalContent>
          <ModalCloseButton />
          <ModalBody>
            <AgoraClassRoom {...props} />
          </ModalBody>
        </ModalContent>
      </Modal>
    </>
  );
};

const Int: Renderer["query"]["Int"] = ({ value, context }) => {
  switch (true) {
    case context.path.endsWith("id"):
      return <Kbd shadow="2xl">{value}</Kbd>;
    case context.type === "Lesson" && context.path.endsWith("duration"):
      return <Text>{Math.round(value / 60)}分钟</Text>;
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
    case context.component === "LessonTable" &&
      context.path === "class.students.name": {
      const { id, name } = context.unsafe_entity;
      return (
        <ClassRoomModal
          id={value.replace(/\s/g, "")}
          name={value}
          role={2}
          roomUuid={id.toString()}
          roomName={name}
        />
      );
    }
    case context.component === "LessonTable" &&
      context.path === "class.teacher.name": {
      const { id, name } = context.unsafe_entity;
      return (
        <ClassRoomModal
          id={value.replace(/\s/g, "")}
          name={value}
          role={1}
          roomUuid={id.toString()}
          roomName={name}
        />
      );
    }
    case context.type === "User" && context.path.endsWith("name"):
    case context.path.endsWith("students.name"):
    case context.path.endsWith("teacher.name"):
      return (
        <Flex alignItems="center">
          <Avatar name={value} size="sm" mr={1} />
          {value}
        </Flex>
      );
    case context.path.endsWith("thumbnails"):
      return <Image width={32} height={32} src={value} />;
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
