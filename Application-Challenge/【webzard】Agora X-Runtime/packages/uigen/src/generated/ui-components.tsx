import React, { useEffect, useState } from "react";
import { CombinedError } from "urql";
import { useForm, DeepPartial, useFieldArray } from "react-hook-form";
import { renderer } from "../renderer";
import {
  Spinner,
  Center,
  Text,
  Popover,
  PopoverTrigger,
  PopoverContent,
  PopoverCloseButton,
  PopoverHeader,
  PopoverBody,
  Table,
  Thead,
  Tbody,
  Tr,
  Td,
  Th,
  FormErrorMessage,
  FormLabel,
  FormControl,
  Button,
  ModalFooter,
  useToast,
  IconButton,
  Flex,
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalBody,
  ModalCloseButton,
  useDisclosure,
  List,
  ListItem,
  Divider,
  VStack,
  HStack,
  Box,
  Heading,
} from "@chakra-ui/react";
import { AddIcon, CloseIcon } from "@chakra-ui/icons";
import {
  Scalars,
  Role,
  useClassTableQuery,
  ClassTableQuery,
  useCreateOneClassFormMutation,
  CreateOneClassFormMutation,
  ButtonVariant,
  useLessonTableQuery,
  LessonTableQuery,
  useLessonCalendarQuery,
  LessonCalendarQuery,
  useCreateOneLessonFormMutation,
  CreateOneLessonFormMutation,
  useUserTableQuery,
  UserTableQuery,
  useUserListQuery,
  UserListQuery,
  useUserKanbanQuery,
  UserKanbanQuery,
  useCreateOneUserFormMutation,
  CreateOneUserFormMutation,
  useUpdateOneUserFormMutation,
  UpdateOneUserFormMutation,
  useDeleteOneUserFormMutation,
  DeleteOneUserFormMutation,
} from "./data-components";
import Calendar from "../ui-kit/Calendar";
import dayjs from "dayjs";
import { useTranslation, initReactI18next } from "react-i18next";
import { AgoraClassroom } from "../custom-components/Agora";

export const Error: React.FC<{ error: CombinedError }> = ({ error }) => {
  return (
    <Center>
      <Popover trigger="hover">
        <PopoverTrigger>
          <Text>{error.message}</Text>
        </PopoverTrigger>
        <PopoverContent>
          <PopoverCloseButton />
          <PopoverHeader>Error Detail:</PopoverHeader>
          <PopoverBody>
            <pre style={{ overflow: "auto" }}>
              {JSON.stringify(
                {
                  ...error,
                  networkError: {
                    message: error.networkError?.message,
                    stack: error.networkError?.stack,
                  },
                },
                null,
                2
              )}
            </pre>
          </PopoverBody>
        </PopoverContent>
      </Popover>
    </Center>
  );
};
export const Empty: React.FC<{ message?: string }> = ({
  message = "no data",
}) => {
  return <Center>{message}</Center>;
};

// table
export const ClassTable: React.FC = () => {
  const [{ data, fetching, error }] = useClassTableQuery();
  const { isOpen, onOpen, onClose } = useDisclosure();
  const [selected, setSelected] =
    useState<ClassTableQuery["classes"][0] | null>(null);
  useEffect(() => {
    if (selected) {
      onOpen();
    } else {
      onClose();
    }
  }, [selected]);
  const { t } = useTranslation();

  if (error) {
    return <Error error={error} />;
  }
  if (fetching) {
    return (
      <Center>
        <Spinner />
      </Center>
    );
  }
  if (!data) {
    return <Empty />;
  }

  return (
    <>
      <Table variant="striped">
        <Thead>
          <Tr>
            <Th>{t("ClassTable.id")}</Th>
            <Th>{t("ClassTable.name")}</Th>
            <Th>{t("ClassTable.teacher.name")}</Th>
            <Th>{t("ClassTable.students.name")}</Th>
          </Tr>
        </Thead>
        <Tbody>
          {(data.classes || []).map((entity) => {
            return (
              <Tr key={entity.id}>
                <Td>
                  {renderer.query.Int({
                    value: entity.id,
                    context: {
                      type: "Class",
                      path: "id",
                      component: "ClassTable",
                      unsafe_entity: entity,
                    },
                  })}
                </Td>
                <Td>
                  {renderer.query.String({
                    value: entity.name,
                    context: {
                      type: "Class",
                      path: "name",
                      component: "ClassTable",
                      unsafe_entity: entity,
                    },
                  })}
                </Td>
                <Td>
                  {renderer.query.String({
                    value: entity.teacher.name,
                    context: {
                      type: "User",
                      path: "teacher.name",
                      component: "ClassTable",
                      unsafe_entity: entity,
                    },
                  })}
                </Td>
                <Td>
                  {(entity.students || []).map((subEntity) => {
                    return (
                      <Box key={subEntity.id || subEntity}>
                        {renderer.query.String({
                          value: subEntity.name || subEntity,
                          context: {
                            type: "User",
                            path: "students.name",
                            component: "ClassTable",
                            unsafe_entity: entity,
                          },
                        })}
                      </Box>
                    );
                  })}
                </Td>
              </Tr>
            );
          })}
        </Tbody>
      </Table>
    </>
  );
};

export const LessonTable: React.FC = () => {
  const [{ data, fetching, error }] = useLessonTableQuery();
  const { isOpen, onOpen, onClose } = useDisclosure();
  const [selected, setSelected] =
    useState<LessonTableQuery["lessons"][0] | null>(null);
  useEffect(() => {
    if (selected) {
      onOpen();
    } else {
      onClose();
    }
  }, [selected]);
  const { t } = useTranslation();

  if (error) {
    return <Error error={error} />;
  }
  if (fetching) {
    return (
      <Center>
        <Spinner />
      </Center>
    );
  }
  if (!data) {
    return <Empty />;
  }

  return (
    <>
      <Table variant="striped">
        <Thead>
          <Tr>
            <Th>{t("LessonTable.id")}</Th>
            <Th>{t("LessonTable.name")}</Th>
            <Th>{t("LessonTable.class.name")}</Th>
            <Th>{t("LessonTable.class.teacher.name")}</Th>
            <Th>{t("LessonTable.class.students.name")}</Th>
            <Th>{t("LessonTable.startedAt")}</Th>
            <Th>{t("LessonTable.duration")}</Th>
            <Th>{t("LessonTable.thumbnails")}</Th>
          </Tr>
        </Thead>
        <Tbody>
          {(data.lessons || []).map((entity) => {
            return (
              <Tr key={entity.id}>
                <Td>
                  {renderer.query.Int({
                    value: entity.id,
                    context: {
                      type: "Lesson",
                      path: "id",
                      component: "LessonTable",
                      unsafe_entity: entity,
                    },
                  })}
                </Td>
                <Td>
                  {renderer.query.String({
                    value: entity.name,
                    context: {
                      type: "Lesson",
                      path: "name",
                      component: "LessonTable",
                      unsafe_entity: entity,
                    },
                  })}
                </Td>
                <Td>
                  {renderer.query.String({
                    value: entity.class.name,
                    context: {
                      type: "Class",
                      path: "class.name",
                      component: "LessonTable",
                      unsafe_entity: entity,
                    },
                  })}
                </Td>
                <Td>
                  {renderer.query.String({
                    value: entity.class.teacher.name,
                    context: {
                      type: "Class",
                      path: "class.teacher.name",
                      component: "LessonTable",
                      unsafe_entity: entity,
                    },
                  })}
                </Td>
                <Td>
                  {(entity.class.students || []).map((subEntity) => {
                    return (
                      <Box key={subEntity.id || subEntity}>
                        {renderer.query.String({
                          value: subEntity.name || subEntity,
                          context: {
                            type: "Class",
                            path: "class.students.name",
                            component: "LessonTable",
                            unsafe_entity: entity,
                          },
                        })}
                      </Box>
                    );
                  })}
                </Td>
                <Td>
                  {renderer.query.DateTime({
                    value: entity.startedAt,
                    context: {
                      type: "Lesson",
                      path: "startedAt",
                      component: "LessonTable",
                      unsafe_entity: entity,
                    },
                  })}
                </Td>
                <Td>
                  {renderer.query.Int({
                    value: entity.duration,
                    context: {
                      type: "Lesson",
                      path: "duration",
                      component: "LessonTable",
                      unsafe_entity: entity,
                    },
                  })}
                </Td>
                <Td>
                  {(entity.thumbnails || []).map((subEntity) => {
                    return (
                      <Box key={subEntity.id || subEntity}>
                        {renderer.query.String({
                          value: subEntity.name || subEntity,
                          context: {
                            type: "Lesson",
                            path: "thumbnails",
                            component: "LessonTable",
                            unsafe_entity: entity,
                          },
                        })}
                      </Box>
                    );
                  })}
                </Td>
              </Tr>
            );
          })}
        </Tbody>
      </Table>
    </>
  );
};

export const UserTable: React.FC = () => {
  const [{ data, fetching, error }] = useUserTableQuery();
  const { isOpen, onOpen, onClose } = useDisclosure();
  const [selected, setSelected] =
    useState<UserTableQuery["users"][0] | null>(null);
  useEffect(() => {
    if (selected) {
      onOpen();
    } else {
      onClose();
    }
  }, [selected]);
  const { t } = useTranslation();

  if (error) {
    return <Error error={error} />;
  }
  if (fetching) {
    return (
      <Center>
        <Spinner />
      </Center>
    );
  }
  if (!data) {
    return <Empty />;
  }

  return (
    <>
      <Table variant="striped">
        <Thead>
          <Tr>
            <Th>{t("UserTable.id")}</Th>
            <Th>{t("UserTable.name")}</Th>
            <Th>{t("UserTable.role")}</Th>
            <Th>{t("UserTable.email")}</Th>
            <Th>{t("UserTable.createdAt")}</Th>
          </Tr>
        </Thead>
        <Tbody>
          {(data.users || []).map((entity) => {
            return (
              <Tr key={entity.id} onClick={() => setSelected(entity)}>
                <Td>
                  {renderer.query.Int({
                    value: entity.id,
                    context: {
                      type: "User",
                      path: "id",
                      component: "UserTable",
                      unsafe_entity: entity,
                    },
                  })}
                </Td>
                <Td>
                  {renderer.query.String({
                    value: entity.name,
                    context: {
                      type: "User",
                      path: "name",
                      component: "UserTable",
                      unsafe_entity: entity,
                    },
                  })}
                </Td>
                <Td>
                  {renderer.query.Enum({
                    value: entity.role,
                    context: {
                      type: "User",
                      path: "role",
                      component: "UserTable",
                      unsafe_entity: entity,
                    },
                  })}
                </Td>
                <Td>
                  {renderer.query.String({
                    value: entity.email,
                    context: {
                      type: "User",
                      path: "email",
                      component: "UserTable",
                      unsafe_entity: entity,
                    },
                  })}
                </Td>
                <Td>
                  {renderer.query.DateTime({
                    value: entity.createdAt,
                    context: {
                      type: "User",
                      path: "createdAt",
                      component: "UserTable",
                      unsafe_entity: entity,
                    },
                  })}
                </Td>
              </Tr>
            );
          })}
        </Tbody>
      </Table>
      <Modal isOpen={isOpen} onClose={onClose}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>编辑用户</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <UpdateOneUserForm
              afterSubmit={() => setSelected(null)}
              defaultValues={{
                where: { id: selected?.id },
                data: {
                  email: { set: selected?.email },
                  name: { set: selected?.name },
                  role: { set: selected?.role },
                },
              }}
            />
          </ModalBody>
        </ModalContent>
      </Modal>
    </>
  );
};

// list
export const UserList: React.FC = () => {
  const [{ data, fetching, error }] = useUserListQuery();
  if (error) {
    return <Error error={error} />;
  }
  if (fetching) {
    return (
      <Center>
        <Spinner />
      </Center>
    );
  }
  if (!data) {
    return <Empty />;
  }

  return (
    <List borderWidth="1px" rounded="md">
      {(data.users || []).map((entity) => {
        return (
          <ListItem
            key={entity.id}
            display="flex"
            justifyContent="space-between"
            p="6"
          >
            <Flex flex="1">
              {renderer.query.Int({
                value: entity.id,
                context: {
                  type: "User",
                  path: "id",
                  component: "UserTable",
                  unsafe_entity: entity,
                },
              })}
            </Flex>
            <Flex flex="1">
              {renderer.query.String({
                value: entity.name,
                context: {
                  type: "User",
                  path: "name",
                  component: "UserTable",
                  unsafe_entity: entity,
                },
              })}
            </Flex>
            <Flex flex="1">
              {renderer.query.Enum({
                value: entity.role,
                context: {
                  type: "User",
                  path: "role",
                  component: "UserTable",
                  unsafe_entity: entity,
                },
              })}
            </Flex>
          </ListItem>
        );
      })}
    </List>
  );
};

// kanban
export const UserKanban: React.FC = () => {
  const [{ data, fetching, error }] = useUserKanbanQuery();
  if (error) {
    return <Error error={error} />;
  }
  if (fetching) {
    return (
      <Center>
        <Spinner />
      </Center>
    );
  }
  if (!data) {
    return <Empty />;
  }

  return (
    <HStack spacing="6" align="flex-start">
      <Box boxShadow="xs" rounded="md" p="6" flex="1">
        <Box>TEACHER</Box>
        <VStack>
          {(data.users || [])
            .filter((entity) => entity.role === Role.Teacher)
            .map((entity) => {
              return (
                <Box
                  width="full"
                  key={entity.id}
                  p={5}
                  shadow="md"
                  borderWidth="1px"
                  flex="1"
                  borderRadius="md"
                >
                  <Box mt={4}>
                    {renderer.query.Int({
                      value: entity.id,
                      context: {
                        type: "User",
                        path: "id",
                        component: "UserTable",
                        unsafe_entity: entity,
                      },
                    })}
                  </Box>
                  <Box mt={4}>
                    {renderer.query.String({
                      value: entity.name,
                      context: {
                        type: "User",
                        path: "name",
                        component: "UserTable",
                        unsafe_entity: entity,
                      },
                    })}
                  </Box>
                  <Box mt={4}>
                    {renderer.query.Enum({
                      value: entity.role,
                      context: {
                        type: "User",
                        path: "role",
                        component: "UserTable",
                        unsafe_entity: entity,
                      },
                    })}
                  </Box>
                </Box>
              );
            })}
        </VStack>
      </Box>
      <Box boxShadow="xs" rounded="md" p="6" flex="1">
        <Box>STUDENT</Box>
        <VStack>
          {(data.users || [])
            .filter((entity) => entity.role === Role.Student)
            .map((entity) => {
              return (
                <Box
                  width="full"
                  key={entity.id}
                  p={5}
                  shadow="md"
                  borderWidth="1px"
                  flex="1"
                  borderRadius="md"
                >
                  <Box mt={4}>
                    {renderer.query.Int({
                      value: entity.id,
                      context: {
                        type: "User",
                        path: "id",
                        component: "UserTable",
                        unsafe_entity: entity,
                      },
                    })}
                  </Box>
                  <Box mt={4}>
                    {renderer.query.String({
                      value: entity.name,
                      context: {
                        type: "User",
                        path: "name",
                        component: "UserTable",
                        unsafe_entity: entity,
                      },
                    })}
                  </Box>
                  <Box mt={4}>
                    {renderer.query.Enum({
                      value: entity.role,
                      context: {
                        type: "User",
                        path: "role",
                        component: "UserTable",
                        unsafe_entity: entity,
                      },
                    })}
                  </Box>
                </Box>
              );
            })}
        </VStack>
      </Box>
      <Box boxShadow="xs" rounded="md" p="6" flex="1">
        <Box>ADMIN</Box>
        <VStack>
          {(data.users || [])
            .filter((entity) => entity.role === Role.Admin)
            .map((entity) => {
              return (
                <Box
                  width="full"
                  key={entity.id}
                  p={5}
                  shadow="md"
                  borderWidth="1px"
                  flex="1"
                  borderRadius="md"
                >
                  <Box mt={4}>
                    {renderer.query.Int({
                      value: entity.id,
                      context: {
                        type: "User",
                        path: "id",
                        component: "UserTable",
                        unsafe_entity: entity,
                      },
                    })}
                  </Box>
                  <Box mt={4}>
                    {renderer.query.String({
                      value: entity.name,
                      context: {
                        type: "User",
                        path: "name",
                        component: "UserTable",
                        unsafe_entity: entity,
                      },
                    })}
                  </Box>
                  <Box mt={4}>
                    {renderer.query.Enum({
                      value: entity.role,
                      context: {
                        type: "User",
                        path: "role",
                        component: "UserTable",
                        unsafe_entity: entity,
                      },
                    })}
                  </Box>
                </Box>
              );
            })}
        </VStack>
      </Box>
    </HStack>
  );
};

// calendar
export const LessonCalendar: React.FC = () => {
  const [{ data, fetching, error }] = useLessonCalendarQuery();
  if (error) {
    return <Error error={error} />;
  }
  if (fetching) {
    return (
      <Center>
        <Spinner />
      </Center>
    );
  }
  if (!data) {
    return <Empty />;
  }

  return (
    <Calendar
      schedules={data.lessons.map((entity) => ({
        id: entity.id,
        title: entity.class?.name,
        body: entity.name,
        start: entity.startedAt,
        end: dayjs(entity.startedAt)
          .add(entity.duration, "second")
          .toISOString(),
      }))}
    />
  );
};

// form
export type CreateOneClassFormProps = {
  afterSubmit?: () => unknown;
  variant?: "plain" | "modal";
  defaultValues?: DeepPartial<CreateOneClassFormValues>;
};
export type CreateOneClassFormValues = {
  data: {
    name: Scalars["String"];
    students?: {
      connect?: {
        id?: Scalars["Int"];
      }[];
    };
    teacher: {
      connect?: {
        id?: Scalars["Int"];
      };
    };
  };
};
export const CreateOneClassForm: React.FC<CreateOneClassFormProps> = ({
  afterSubmit,
  variant = "plain",
  defaultValues,
}) => {
  const [, trigger] = useCreateOneClassFormMutation();
  const toast = useToast();
  const { t } = useTranslation();

  const {
    handleSubmit,
    register,
    formState: { errors, isSubmitting },
    reset,
    control,
  } = useForm<CreateOneClassFormValues>({
    defaultValues,
  });
  const data_students_connect_FieldArray = useFieldArray({
    name: "data.students.connect",
    control,
    keyName: "_id",
  });

  async function onSubmit(values: CreateOneClassFormValues) {
    try {
      const result = await trigger(values);
      if (result.error) {
        throw result.error;
      }
      reset();
    } catch (error) {
      toast({
        title: "Submit failed.",
        description: error.message || String(error),
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    } finally {
      afterSubmit?.();
    }
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <FormControl isInvalid={Boolean(errors.data?.name)}>
        <FormLabel htmlFor="data.name">{t("data.name")}</FormLabel>
        <renderer.mutation.String
          id="data.name"
          {...register("data.name" as const, {
            required: "This is required",
          })}
        />
        <FormErrorMessage>
          {errors.data?.name && errors.data.name.message}
        </FormErrorMessage>
      </FormControl>

      <FormControl isInvalid={Boolean(errors.data?.students?.connect)}>
        <FormLabel>{t("data.students.connect")}</FormLabel>
        {data_students_connect_FieldArray.fields.map((field, index) => {
          return (
            <Box key={field._id}>
              <Flex justify-content="space-between">
                <Box flex="1" mb={2}>
                  <renderer.mutation.InputObject
                    {...register(`data.students.connect.${index}.id` as const, {
                      valueAsNumber: true,
                    })}
                    defaultValue={field.value}
                  />
                </Box>
                <IconButton aria-label="remove-one" ml={4}>
                  <CloseIcon
                    onClick={() =>
                      data_students_connect_FieldArray.remove(index)
                    }
                  />
                </IconButton>
              </Flex>
              <FormErrorMessage>
                {errors.data?.students?.connect &&
                  errors.data.students.connect[0]?.id?.message}
              </FormErrorMessage>
            </Box>
          );
        })}
        <IconButton aria-label="add-one" mt={4}>
          <AddIcon
            onClick={() => data_students_connect_FieldArray.append({})}
          />
        </IconButton>
      </FormControl>

      <FormControl isInvalid={Boolean(errors.data?.teacher?.connect?.id)}>
        <FormLabel htmlFor="data.teacher.connect.id">
          {t("data.teacher.connect.id")}
        </FormLabel>
        <renderer.mutation.Int
          id="data.teacher.connect.id"
          {...register("data.teacher.connect.id" as const, {
            valueAsNumber: true,
          })}
        />
        <FormErrorMessage>
          {errors.data?.teacher?.connect?.id &&
            errors.data.teacher.connect.id.message}
        </FormErrorMessage>
      </FormControl>

      {variant === "plain" ? (
        <Button mt={4} isLoading={isSubmitting} type="submit">
          Submit
        </Button>
      ) : null}
      {variant === "modal" ? (
        <ModalFooter>
          <Button mt={4} isLoading={isSubmitting} type="submit">
            Submit
          </Button>
        </ModalFooter>
      ) : null}
    </form>
  );
};

export type CreateOneLessonFormProps = {
  afterSubmit?: () => unknown;
  variant?: "plain" | "modal";
  defaultValues?: DeepPartial<CreateOneLessonFormValues>;
};
export type CreateOneLessonFormValues = {
  data: {
    class: {
      connect?: {
        id?: Scalars["Int"];
      };
    };
    duration: Scalars["Int"];
    name: Scalars["String"];
    startedAt: Scalars["String"];
  };
};
export const CreateOneLessonForm: React.FC<CreateOneLessonFormProps> = ({
  afterSubmit,
  variant = "plain",
  defaultValues,
}) => {
  const [, trigger] = useCreateOneLessonFormMutation();
  const toast = useToast();
  const { t } = useTranslation();

  const {
    handleSubmit,
    register,
    formState: { errors, isSubmitting },
    reset,
    control,
  } = useForm<CreateOneLessonFormValues>({
    defaultValues,
  });

  async function onSubmit(values: CreateOneLessonFormValues) {
    try {
      const result = await trigger(values);
      if (result.error) {
        throw result.error;
      }
      reset();
    } catch (error) {
      toast({
        title: "Submit failed.",
        description: error.message || String(error),
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    } finally {
      afterSubmit?.();
    }
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <FormControl isInvalid={Boolean(errors.data?.class?.connect?.id)}>
        <FormLabel htmlFor="data.class.connect.id">
          {t("data.class.connect.id")}
        </FormLabel>
        <renderer.mutation.Int
          id="data.class.connect.id"
          {...register("data.class.connect.id" as const, {
            valueAsNumber: true,
          })}
        />
        <FormErrorMessage>
          {errors.data?.class?.connect?.id &&
            errors.data.class.connect.id.message}
        </FormErrorMessage>
      </FormControl>

      <FormControl isInvalid={Boolean(errors.data?.duration)}>
        <FormLabel htmlFor="data.duration">{t("data.duration")}</FormLabel>
        <renderer.mutation.Int
          id="data.duration"
          {...register("data.duration" as const, {
            required: "This is required",
            valueAsNumber: true,
          })}
        />
        <FormErrorMessage>
          {errors.data?.duration && errors.data.duration.message}
        </FormErrorMessage>
      </FormControl>

      <FormControl isInvalid={Boolean(errors.data?.name)}>
        <FormLabel htmlFor="data.name">{t("data.name")}</FormLabel>
        <renderer.mutation.String
          id="data.name"
          {...register("data.name" as const, {
            required: "This is required",
          })}
        />
        <FormErrorMessage>
          {errors.data?.name && errors.data.name.message}
        </FormErrorMessage>
      </FormControl>

      <FormControl isInvalid={Boolean(errors.data?.startedAt)}>
        <FormLabel htmlFor="data.startedAt">{t("data.startedAt")}</FormLabel>
        <renderer.mutation.String
          id="data.startedAt"
          {...register("data.startedAt" as const, {
            required: "This is required",
          })}
        />
        <FormErrorMessage>
          {errors.data?.startedAt && errors.data.startedAt.message}
        </FormErrorMessage>
      </FormControl>

      {variant === "plain" ? (
        <Button mt={4} isLoading={isSubmitting} type="submit">
          Submit
        </Button>
      ) : null}
      {variant === "modal" ? (
        <ModalFooter>
          <Button mt={4} isLoading={isSubmitting} type="submit">
            Submit
          </Button>
        </ModalFooter>
      ) : null}
    </form>
  );
};

export type CreateOneUserFormProps = {
  afterSubmit?: () => unknown;
  variant?: "plain" | "modal";
  defaultValues?: DeepPartial<CreateOneUserFormValues>;
};
export type CreateOneUserFormValues = {
  data: {
    email: Scalars["String"];
    name: Scalars["String"];
    role: Role;
  };
};
export const CreateOneUserForm: React.FC<CreateOneUserFormProps> = ({
  afterSubmit,
  variant = "plain",
  defaultValues,
}) => {
  const [, trigger] = useCreateOneUserFormMutation();
  const toast = useToast();
  const { t } = useTranslation();

  const {
    handleSubmit,
    register,
    formState: { errors, isSubmitting },
    reset,
    control,
  } = useForm<CreateOneUserFormValues>({
    defaultValues,
  });

  async function onSubmit(values: CreateOneUserFormValues) {
    try {
      const result = await trigger(values);
      if (result.error) {
        throw result.error;
      }
      reset();
    } catch (error) {
      toast({
        title: "Submit failed.",
        description: error.message || String(error),
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    } finally {
      afterSubmit?.();
    }
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <FormControl isInvalid={Boolean(errors.data?.email)}>
        <FormLabel htmlFor="data.email">{t("data.email")}</FormLabel>
        <renderer.mutation.String
          id="data.email"
          {...register("data.email" as const, {
            required: "This is required",
          })}
        />
        <FormErrorMessage>
          {errors.data?.email && errors.data.email.message}
        </FormErrorMessage>
      </FormControl>

      <FormControl isInvalid={Boolean(errors.data?.name)}>
        <FormLabel htmlFor="data.name">{t("data.name")}</FormLabel>
        <renderer.mutation.String
          id="data.name"
          {...register("data.name" as const, {
            required: "This is required",
          })}
        />
        <FormErrorMessage>
          {errors.data?.name && errors.data.name.message}
        </FormErrorMessage>
      </FormControl>

      <FormControl isInvalid={Boolean(errors.data?.role)}>
        <FormLabel htmlFor="data.role">{t("data.role")}</FormLabel>
        <renderer.mutation.Enum
          id="data.role"
          {...register("data.role" as const, {
            required: "This is required",
          })}
          options={[
            { text: Role.Teacher, value: Role.Teacher },
            { text: Role.Student, value: Role.Student },
            { text: Role.Admin, value: Role.Admin },
          ]}
        />
        <FormErrorMessage>
          {errors.data?.role && errors.data.role.message}
        </FormErrorMessage>
      </FormControl>

      {variant === "plain" ? (
        <Button mt={4} isLoading={isSubmitting} type="submit">
          Submit
        </Button>
      ) : null}
      {variant === "modal" ? (
        <ModalFooter>
          <Button mt={4} isLoading={isSubmitting} type="submit">
            Submit
          </Button>
        </ModalFooter>
      ) : null}
    </form>
  );
};

export type UpdateOneUserFormProps = {
  afterSubmit?: () => unknown;
  variant?: "plain" | "modal";
  defaultValues?: DeepPartial<UpdateOneUserFormValues>;
};
export type UpdateOneUserFormValues = {
  data: {
    email?: {
      set?: Scalars["String"];
    };
    name?: {
      set?: Scalars["String"];
    };
    role?: {
      set?: Role;
    };
  };
  where: {
    id?: Scalars["Int"];
  };
};
export const UpdateOneUserForm: React.FC<UpdateOneUserFormProps> = ({
  afterSubmit,
  variant = "plain",
  defaultValues,
}) => {
  const [, trigger] = useUpdateOneUserFormMutation();
  const toast = useToast();
  const { t } = useTranslation();

  const {
    handleSubmit,
    register,
    formState: { errors, isSubmitting },
    reset,
    control,
  } = useForm<UpdateOneUserFormValues>({
    defaultValues,
  });

  async function onSubmit(values: UpdateOneUserFormValues) {
    try {
      const result = await trigger(values);
      if (result.error) {
        throw result.error;
      }
      reset();
    } catch (error) {
      toast({
        title: "Submit failed.",
        description: error.message || String(error),
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    } finally {
      afterSubmit?.();
    }
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <FormControl isInvalid={Boolean(errors.data?.email?.set)}>
        <FormLabel htmlFor="data.email.set">{t("data.email.set")}</FormLabel>
        <renderer.mutation.String
          id="data.email.set"
          {...register("data.email.set" as const, {})}
        />
        <FormErrorMessage>
          {errors.data?.email?.set && errors.data.email.set.message}
        </FormErrorMessage>
      </FormControl>

      <FormControl isInvalid={Boolean(errors.data?.name?.set)}>
        <FormLabel htmlFor="data.name.set">{t("data.name.set")}</FormLabel>
        <renderer.mutation.String
          id="data.name.set"
          {...register("data.name.set" as const, {})}
        />
        <FormErrorMessage>
          {errors.data?.name?.set && errors.data.name.set.message}
        </FormErrorMessage>
      </FormControl>

      <FormControl isInvalid={Boolean(errors.data?.role?.set)}>
        <FormLabel htmlFor="data.role.set">{t("data.role.set")}</FormLabel>
        <renderer.mutation.Enum
          id="data.role.set"
          {...register("data.role.set" as const, {})}
          options={[
            { text: Role.Teacher, value: Role.Teacher },
            { text: Role.Student, value: Role.Student },
            { text: Role.Admin, value: Role.Admin },
          ]}
        />
        <FormErrorMessage>
          {errors.data?.role?.set && errors.data.role.set.message}
        </FormErrorMessage>
      </FormControl>

      <FormControl isInvalid={Boolean(errors.where?.id)}>
        <FormLabel htmlFor="where.id">{t("where.id")}</FormLabel>
        <renderer.mutation.Int
          id="where.id"
          {...register("where.id" as const, {
            valueAsNumber: true,
          })}
        />
        <FormErrorMessage>
          {errors.where?.id && errors.where.id.message}
        </FormErrorMessage>
      </FormControl>

      {variant === "plain" ? (
        <Button mt={4} isLoading={isSubmitting} type="submit">
          Submit
        </Button>
      ) : null}
      {variant === "modal" ? (
        <ModalFooter>
          <Button mt={4} isLoading={isSubmitting} type="submit">
            Submit
          </Button>
        </ModalFooter>
      ) : null}
    </form>
  );
};

export type DeleteOneUserFormProps = {
  afterSubmit?: () => unknown;
  variant?: "plain" | "modal";
  defaultValues?: DeepPartial<DeleteOneUserFormValues>;
};
export type DeleteOneUserFormValues = {
  where: {
    id?: Scalars["Int"];
  };
};
export const DeleteOneUserForm: React.FC<DeleteOneUserFormProps> = ({
  afterSubmit,
  variant = "plain",
  defaultValues,
}) => {
  const [, trigger] = useDeleteOneUserFormMutation();
  const toast = useToast();
  const { t } = useTranslation();

  const {
    handleSubmit,
    register,
    formState: { errors, isSubmitting },
    reset,
    control,
  } = useForm<DeleteOneUserFormValues>({
    defaultValues,
  });

  async function onSubmit(values: DeleteOneUserFormValues) {
    try {
      const result = await trigger(values);
      if (result.error) {
        throw result.error;
      }
      reset();
    } catch (error) {
      toast({
        title: "Submit failed.",
        description: error.message || String(error),
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    } finally {
      afterSubmit?.();
    }
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <FormControl isInvalid={Boolean(errors.where?.id)}>
        <FormLabel htmlFor="where.id">{t("where.id")}</FormLabel>
        <renderer.mutation.Int
          id="where.id"
          {...register("where.id" as const, {
            valueAsNumber: true,
          })}
        />
        <FormErrorMessage>
          {errors.where?.id && errors.where.id.message}
        </FormErrorMessage>
      </FormControl>

      {variant === "plain" ? (
        <Button mt={4} isLoading={isSubmitting} type="submit">
          Submit
        </Button>
      ) : null}
      {variant === "modal" ? (
        <ModalFooter>
          <Button mt={4} isLoading={isSubmitting} type="submit">
            Submit
          </Button>
        </ModalFooter>
      ) : null}
    </form>
  );
};

// button
export type CreateClassButtonProps = {
  children?: Scalars["String"];
  variant?: ButtonVariant;
  isFullWidth?: Scalars["Boolean"];
} & {
  onClick?: () => unknown;
};
export const CreateClassButton: React.FC<CreateClassButtonProps> = (_props) => {
  const props = Object.assign(
    { children: "创建课程", variant: ButtonVariant.Solid, isFullWidth: true },
    _props
  );
  return <Button {...props} />;
};
export type CreateLessonButtonProps = {
  children?: Scalars["String"];
  variant?: ButtonVariant;
  isFullWidth?: Scalars["Boolean"];
} & {
  onClick?: () => unknown;
};
export const CreateLessonButton: React.FC<CreateLessonButtonProps> = (
  _props
) => {
  const props = Object.assign(
    {
      children: "新增课时内容",
      variant: ButtonVariant.Solid,
      isFullWidth: true,
    },
    _props
  );
  return <Button {...props} />;
};
export type CreateUserButtonProps = {
  children?: Scalars["String"];
  variant?: ButtonVariant;
  isFullWidth?: Scalars["Boolean"];
} & {
  onClick?: () => unknown;
};
export const CreateUserButton: React.FC<CreateUserButtonProps> = (_props) => {
  const props = Object.assign(
    { children: "创建用户", variant: ButtonVariant.Solid, isFullWidth: true },
    _props
  );
  return <Button {...props} />;
};
export type DeleteUserButtonProps = {
  children?: Scalars["String"];
  variant?: ButtonVariant;
  isFullWidth?: Scalars["Boolean"];
} & {
  onClick?: () => unknown;
};
export const DeleteUserButton: React.FC<DeleteUserButtonProps> = (_props) => {
  const props = Object.assign(
    {
      children: "删除用户",
      variant: ButtonVariant.Outlined,
      isFullWidth: true,
    },
    _props
  );
  return <Button {...props} />;
};

// modal
export const CreateOneClassModal: React.FC = () => {
  const { isOpen, onOpen, onClose } = useDisclosure();

  return (
    <>
      <CreateClassButton onClick={onOpen} />
      <Modal isOpen={isOpen} onClose={onClose}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>创建课程</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <CreateOneClassForm afterSubmit={onClose} />
          </ModalBody>
        </ModalContent>
      </Modal>
    </>
  );
};
export const CreateOneLessonModal: React.FC = () => {
  const { isOpen, onOpen, onClose } = useDisclosure();

  return (
    <>
      <CreateLessonButton onClick={onOpen} />
      <Modal isOpen={isOpen} onClose={onClose}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>新增课时内容</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <CreateOneLessonForm afterSubmit={onClose} />
          </ModalBody>
        </ModalContent>
      </Modal>
    </>
  );
};
export const CreateOneUserModal: React.FC = () => {
  const { isOpen, onOpen, onClose } = useDisclosure();

  return (
    <>
      <CreateUserButton onClick={onOpen} />
      <Modal isOpen={isOpen} onClose={onClose}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>创建用户</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <CreateOneUserForm afterSubmit={onClose} />
          </ModalBody>
        </ModalContent>
      </Modal>
    </>
  );
};
export const DeleteOneUserModal: React.FC = () => {
  const { isOpen, onOpen, onClose } = useDisclosure();

  return (
    <>
      <DeleteUserButton onClick={onOpen} />
      <Modal isOpen={isOpen} onClose={onClose}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>删除用户</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <DeleteOneUserForm afterSubmit={onClose} />
          </ModalBody>
        </ModalContent>
      </Modal>
    </>
  );
};
