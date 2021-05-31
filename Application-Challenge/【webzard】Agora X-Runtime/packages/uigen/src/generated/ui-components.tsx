import React from "react";
import { CombinedError } from "urql";
import { useForm } from "react-hook-form";
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
  List,
  ListItem,
  Flex,
  Divider,
  VStack,
  HStack,
  Box,
  Heading,
  FormErrorMessage,
  FormLabel,
  FormControl,
  Button,
} from "@chakra-ui/react";
import {
  Scalars,
  Role,
  useClassTableQuery,
  useUserTableQuery,
  useUserListQuery,
  useUserKanbanQuery,
  useCreateOneUserFormMutation,
  useUpdateOneUserFormMutation,
  useDeleteOneUserFormMutation,
  ButtonVariant,
} from "./data-components";

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
    <Table variant="striped">
      <Thead>
        <Tr>
          <Th>id</Th>
          <Th>name</Th>
          <Th>teacher</Th>
        </Tr>
      </Thead>
      <Tbody>
        {(data.classes || []).map((entity) => {
          return (
            <Tr key={entity.id}>
              <Td>{entity.id}</Td>
              <Td>{entity.name}</Td>
              <Td>{entity.teacher}</Td>
            </Tr>
          );
        })}
      </Tbody>
    </Table>
  );
};

export const UserTable: React.FC = () => {
  const [{ data, fetching, error }] = useUserTableQuery();
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
    <Table variant="striped">
      <Thead>
        <Tr>
          <Th>id</Th>
          <Th>name</Th>
          <Th>role</Th>
          <Th>createdAt</Th>
        </Tr>
      </Thead>
      <Tbody>
        {(data.users || []).map((entity) => {
          return (
            <Tr key={entity.id}>
              <Td>{entity.id}</Td>
              <Td>{entity.name}</Td>
              <Td>{entity.role}</Td>
              <Td>{entity.createdAt}</Td>
            </Tr>
          );
        })}
      </Tbody>
    </Table>
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
            <Flex flex="1">{entity.id}</Flex>
            <Flex flex="1">{entity.name}</Flex>
            <Flex flex="1">{entity.role}</Flex>
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
                  <Text mt={4}>{entity.id}</Text>
                  <Text mt={4}>{entity.name}</Text>
                  <Text mt={4}>{entity.role}</Text>
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
                  <Text mt={4}>{entity.id}</Text>
                  <Text mt={4}>{entity.name}</Text>
                  <Text mt={4}>{entity.role}</Text>
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
                  <Text mt={4}>{entity.id}</Text>
                  <Text mt={4}>{entity.name}</Text>
                  <Text mt={4}>{entity.role}</Text>
                </Box>
              );
            })}
        </VStack>
      </Box>
    </HStack>
  );
};

// form
export type CreateOneUserFormValues = {
  data: {
    email: Scalars["String"];
    name: Scalars["String"];
    role: Role;
  };
};
export const CreateOneUserForm: React.FC = () => {
  const [, trigger] = useCreateOneUserFormMutation();
  const {
    handleSubmit,
    register,
    formState: { errors, isSubmitting },
    reset,
  } = useForm<CreateOneUserFormValues>();

  async function onSubmit(values: CreateOneUserFormValues) {
    try {
      await trigger(values);
      reset();
    } catch (error) {}
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <FormControl isInvalid={Boolean(errors.data?.email)}>
        <FormLabel htmlFor="data.email">data.email</FormLabel>
        <renderer.mutation.String
          id="data.email"
          {...register("data.email", {
            required: "This is required",
          })}
        />
        <FormErrorMessage>
          {errors.data?.email && errors.data.email.message}
        </FormErrorMessage>
      </FormControl>
      <FormControl isInvalid={Boolean(errors.data?.name)}>
        <FormLabel htmlFor="data.name">data.name</FormLabel>
        <renderer.mutation.String
          id="data.name"
          {...register("data.name", {
            required: "This is required",
          })}
        />
        <FormErrorMessage>
          {errors.data?.name && errors.data.name.message}
        </FormErrorMessage>
      </FormControl>
      <FormControl isInvalid={Boolean(errors.data?.role)}>
        <FormLabel htmlFor="data.role">data.role</FormLabel>
        <renderer.mutation.Enum
          id="data.role"
          {...register("data.role", {
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
      <Button mt={4} isLoading={isSubmitting} type="submit">
        Submit
      </Button>
    </form>
  );
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
export const UpdateOneUserForm: React.FC = () => {
  const [, trigger] = useUpdateOneUserFormMutation();
  const {
    handleSubmit,
    register,
    formState: { errors, isSubmitting },
    reset,
  } = useForm<UpdateOneUserFormValues>();

  async function onSubmit(values: UpdateOneUserFormValues) {
    try {
      await trigger(values);
      reset();
    } catch (error) {}
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <FormControl isInvalid={Boolean(errors.data?.email?.set)}>
        <FormLabel htmlFor="data.email.set">data.email.set</FormLabel>
        <renderer.mutation.String
          id="data.email.set"
          {...register("data.email.set", {})}
        />
        <FormErrorMessage>
          {errors.data?.email?.set && errors.data.email.set.message}
        </FormErrorMessage>
      </FormControl>
      <FormControl isInvalid={Boolean(errors.data?.name?.set)}>
        <FormLabel htmlFor="data.name.set">data.name.set</FormLabel>
        <renderer.mutation.String
          id="data.name.set"
          {...register("data.name.set", {})}
        />
        <FormErrorMessage>
          {errors.data?.name?.set && errors.data.name.set.message}
        </FormErrorMessage>
      </FormControl>
      <FormControl isInvalid={Boolean(errors.data?.role?.set)}>
        <FormLabel htmlFor="data.role.set">data.role.set</FormLabel>
        <renderer.mutation.Enum
          id="data.role.set"
          {...register("data.role.set", {})}
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
        <FormLabel htmlFor="where.id">where.id</FormLabel>
        <renderer.mutation.Int
          id="where.id"
          {...register("where.id", {
            valueAsNumber: true,
          })}
        />
        <FormErrorMessage>
          {errors.where?.id && errors.where.id.message}
        </FormErrorMessage>
      </FormControl>
      <Button mt={4} isLoading={isSubmitting} type="submit">
        Submit
      </Button>
    </form>
  );
};

export type DeleteOneUserFormValues = {
  where: {
    id?: Scalars["Int"];
  };
};
export const DeleteOneUserForm: React.FC = () => {
  const [, trigger] = useDeleteOneUserFormMutation();
  const {
    handleSubmit,
    register,
    formState: { errors, isSubmitting },
    reset,
  } = useForm<DeleteOneUserFormValues>();

  async function onSubmit(values: DeleteOneUserFormValues) {
    try {
      await trigger(values);
      reset();
    } catch (error) {}
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <FormControl isInvalid={Boolean(errors.where?.id)}>
        <FormLabel htmlFor="where.id">where.id</FormLabel>
        <renderer.mutation.Int
          id="where.id"
          {...register("where.id", {
            valueAsNumber: true,
          })}
        />
        <FormErrorMessage>
          {errors.where?.id && errors.where.id.message}
        </FormErrorMessage>
      </FormControl>
      <Button mt={4} isLoading={isSubmitting} type="submit">
        Submit
      </Button>
    </form>
  );
};

// button
export type CreateUserButtonProps = {
  children?: Scalars["String"];
  variant?: ButtonVariant;
};
export const CreateUserButton: React.FC<CreateUserButtonProps> = (_props) => {
  const props = Object.assign(
    { children: "static button", variant: ButtonVariant.Outlined },
    _props
  );
  return <Button {...props} />;
};
