import gql from 'graphql-tag';
import * as Urql from 'urql';
export type Maybe<T> = T | null;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
export type Omit<T, K extends keyof T> = Pick<T, Exclude<keyof T, K>>;
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: string;
  String: string;
  Boolean: boolean;
  Int: number;
  Float: number;
  DateTime: string;
};

export type AffectedRowsOutput = {
  __typename?: 'AffectedRowsOutput';
  count: Scalars['Int'];
};

export type ButtonProps = {
  children?: Maybe<Scalars['String']>;
  variant?: Maybe<ButtonVariant>;
  isFullWidth?: Maybe<Scalars['Boolean']>;
};

export enum ButtonVariant {
  Link = 'link',
  Outlined = 'outlined',
  Solid = 'solid',
  Ghost = 'ghost',
  Unstyled = 'unstyled'
}

export type Class = {
  __typename?: 'Class';
  createdAt: Scalars['DateTime'];
  id: Scalars['Int'];
  lessons: Array<Lesson>;
  name: Scalars['String'];
  students: Array<User>;
  teacher: User;
  updatedAt: Scalars['DateTime'];
};


export type ClassLessonsArgs = {
  after?: Maybe<LessonWhereUniqueInput>;
  before?: Maybe<LessonWhereUniqueInput>;
  first?: Maybe<Scalars['Int']>;
  last?: Maybe<Scalars['Int']>;
  orderBy?: Maybe<Array<LessonOrderByInput>>;
  where?: Maybe<LessonWhereInput>;
};


export type ClassStudentsArgs = {
  after?: Maybe<UserWhereUniqueInput>;
  before?: Maybe<UserWhereUniqueInput>;
  first?: Maybe<Scalars['Int']>;
  last?: Maybe<Scalars['Int']>;
  orderBy?: Maybe<Array<UserOrderByInput>>;
  where?: Maybe<UserWhereInput>;
};

export type ClassCreateInput = {
  createdAt?: Maybe<Scalars['DateTime']>;
  lessons?: Maybe<LessonCreateNestedManyWithoutClassInput>;
  name: Scalars['String'];
  students?: Maybe<UserCreateNestedManyWithoutParticipatedClassesInput>;
  teacher: UserCreateNestedOneWithoutTeachedClassesInput;
  updatedAt?: Maybe<Scalars['DateTime']>;
};

export type ClassCreateManyTeacherInput = {
  createdAt?: Maybe<Scalars['DateTime']>;
  id?: Maybe<Scalars['Int']>;
  name: Scalars['String'];
  updatedAt?: Maybe<Scalars['DateTime']>;
};

export type ClassCreateManyTeacherInputEnvelope = {
  data?: Maybe<Array<ClassCreateManyTeacherInput>>;
  skipDuplicates?: Maybe<Scalars['Boolean']>;
};

export type ClassCreateNestedManyWithoutStudentsInput = {
  connect?: Maybe<Array<ClassWhereUniqueInput>>;
  connectOrCreate?: Maybe<Array<ClassCreateOrConnectWithoutStudentsInput>>;
  create?: Maybe<Array<ClassCreateWithoutStudentsInput>>;
};

export type ClassCreateNestedManyWithoutTeacherInput = {
  connect?: Maybe<Array<ClassWhereUniqueInput>>;
  connectOrCreate?: Maybe<Array<ClassCreateOrConnectWithoutTeacherInput>>;
  create?: Maybe<Array<ClassCreateWithoutTeacherInput>>;
  createMany?: Maybe<ClassCreateManyTeacherInputEnvelope>;
};

export type ClassCreateNestedOneWithoutLessonsInput = {
  connect?: Maybe<ClassWhereUniqueInput>;
  connectOrCreate?: Maybe<ClassCreateOrConnectWithoutLessonsInput>;
  create?: Maybe<ClassCreateWithoutLessonsInput>;
};

export type ClassCreateOrConnectWithoutLessonsInput = {
  create: ClassCreateWithoutLessonsInput;
  where: ClassWhereUniqueInput;
};

export type ClassCreateOrConnectWithoutStudentsInput = {
  create: ClassCreateWithoutStudentsInput;
  where: ClassWhereUniqueInput;
};

export type ClassCreateOrConnectWithoutTeacherInput = {
  create: ClassCreateWithoutTeacherInput;
  where: ClassWhereUniqueInput;
};

export type ClassCreateWithoutLessonsInput = {
  createdAt?: Maybe<Scalars['DateTime']>;
  name: Scalars['String'];
  students?: Maybe<UserCreateNestedManyWithoutParticipatedClassesInput>;
  teacher: UserCreateNestedOneWithoutTeachedClassesInput;
  updatedAt?: Maybe<Scalars['DateTime']>;
};

export type ClassCreateWithoutStudentsInput = {
  createdAt?: Maybe<Scalars['DateTime']>;
  lessons?: Maybe<LessonCreateNestedManyWithoutClassInput>;
  name: Scalars['String'];
  teacher: UserCreateNestedOneWithoutTeachedClassesInput;
  updatedAt?: Maybe<Scalars['DateTime']>;
};

export type ClassCreateWithoutTeacherInput = {
  createdAt?: Maybe<Scalars['DateTime']>;
  lessons?: Maybe<LessonCreateNestedManyWithoutClassInput>;
  name: Scalars['String'];
  students?: Maybe<UserCreateNestedManyWithoutParticipatedClassesInput>;
  updatedAt?: Maybe<Scalars['DateTime']>;
};

export type ClassListRelationFilter = {
  every?: Maybe<ClassWhereInput>;
  none?: Maybe<ClassWhereInput>;
  some?: Maybe<ClassWhereInput>;
};

export type ClassOrderByInput = {
  createdAt?: Maybe<SortOrder>;
  id?: Maybe<SortOrder>;
  name?: Maybe<SortOrder>;
  teacherId?: Maybe<SortOrder>;
  updatedAt?: Maybe<SortOrder>;
};

export type ClassScalarWhereInput = {
  AND?: Maybe<Array<ClassScalarWhereInput>>;
  NOT?: Maybe<Array<ClassScalarWhereInput>>;
  OR?: Maybe<Array<ClassScalarWhereInput>>;
  createdAt?: Maybe<DateTimeFilter>;
  id?: Maybe<IntFilter>;
  name?: Maybe<StringFilter>;
  teacherId?: Maybe<IntFilter>;
  updatedAt?: Maybe<DateTimeFilter>;
};

export type ClassUpdateInput = {
  createdAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
  lessons?: Maybe<LessonUpdateManyWithoutClassInput>;
  name?: Maybe<StringFieldUpdateOperationsInput>;
  students?: Maybe<UserUpdateManyWithoutParticipatedClassesInput>;
  teacher?: Maybe<UserUpdateOneRequiredWithoutTeachedClassesInput>;
  updatedAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
};

export type ClassUpdateManyMutationInput = {
  createdAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
  name?: Maybe<StringFieldUpdateOperationsInput>;
  updatedAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
};

export type ClassUpdateManyWithWhereWithoutStudentsInput = {
  data: ClassUpdateManyMutationInput;
  where: ClassScalarWhereInput;
};

export type ClassUpdateManyWithWhereWithoutTeacherInput = {
  data: ClassUpdateManyMutationInput;
  where: ClassScalarWhereInput;
};

export type ClassUpdateManyWithoutStudentsInput = {
  connect?: Maybe<Array<ClassWhereUniqueInput>>;
  connectOrCreate?: Maybe<Array<ClassCreateOrConnectWithoutStudentsInput>>;
  create?: Maybe<Array<ClassCreateWithoutStudentsInput>>;
  delete?: Maybe<Array<ClassWhereUniqueInput>>;
  deleteMany?: Maybe<Array<ClassScalarWhereInput>>;
  disconnect?: Maybe<Array<ClassWhereUniqueInput>>;
  set?: Maybe<Array<ClassWhereUniqueInput>>;
  update?: Maybe<Array<ClassUpdateWithWhereUniqueWithoutStudentsInput>>;
  updateMany?: Maybe<Array<ClassUpdateManyWithWhereWithoutStudentsInput>>;
  upsert?: Maybe<Array<ClassUpsertWithWhereUniqueWithoutStudentsInput>>;
};

export type ClassUpdateManyWithoutTeacherInput = {
  connect?: Maybe<Array<ClassWhereUniqueInput>>;
  connectOrCreate?: Maybe<Array<ClassCreateOrConnectWithoutTeacherInput>>;
  create?: Maybe<Array<ClassCreateWithoutTeacherInput>>;
  createMany?: Maybe<ClassCreateManyTeacherInputEnvelope>;
  delete?: Maybe<Array<ClassWhereUniqueInput>>;
  deleteMany?: Maybe<Array<ClassScalarWhereInput>>;
  disconnect?: Maybe<Array<ClassWhereUniqueInput>>;
  set?: Maybe<Array<ClassWhereUniqueInput>>;
  update?: Maybe<Array<ClassUpdateWithWhereUniqueWithoutTeacherInput>>;
  updateMany?: Maybe<Array<ClassUpdateManyWithWhereWithoutTeacherInput>>;
  upsert?: Maybe<Array<ClassUpsertWithWhereUniqueWithoutTeacherInput>>;
};

export type ClassUpdateOneRequiredWithoutLessonsInput = {
  connect?: Maybe<ClassWhereUniqueInput>;
  connectOrCreate?: Maybe<ClassCreateOrConnectWithoutLessonsInput>;
  create?: Maybe<ClassCreateWithoutLessonsInput>;
  update?: Maybe<ClassUpdateWithoutLessonsInput>;
  upsert?: Maybe<ClassUpsertWithoutLessonsInput>;
};

export type ClassUpdateWithWhereUniqueWithoutStudentsInput = {
  data: ClassUpdateWithoutStudentsInput;
  where: ClassWhereUniqueInput;
};

export type ClassUpdateWithWhereUniqueWithoutTeacherInput = {
  data: ClassUpdateWithoutTeacherInput;
  where: ClassWhereUniqueInput;
};

export type ClassUpdateWithoutLessonsInput = {
  createdAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
  name?: Maybe<StringFieldUpdateOperationsInput>;
  students?: Maybe<UserUpdateManyWithoutParticipatedClassesInput>;
  teacher?: Maybe<UserUpdateOneRequiredWithoutTeachedClassesInput>;
  updatedAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
};

export type ClassUpdateWithoutStudentsInput = {
  createdAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
  lessons?: Maybe<LessonUpdateManyWithoutClassInput>;
  name?: Maybe<StringFieldUpdateOperationsInput>;
  teacher?: Maybe<UserUpdateOneRequiredWithoutTeachedClassesInput>;
  updatedAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
};

export type ClassUpdateWithoutTeacherInput = {
  createdAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
  lessons?: Maybe<LessonUpdateManyWithoutClassInput>;
  name?: Maybe<StringFieldUpdateOperationsInput>;
  students?: Maybe<UserUpdateManyWithoutParticipatedClassesInput>;
  updatedAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
};

export type ClassUpsertWithWhereUniqueWithoutStudentsInput = {
  create: ClassCreateWithoutStudentsInput;
  update: ClassUpdateWithoutStudentsInput;
  where: ClassWhereUniqueInput;
};

export type ClassUpsertWithWhereUniqueWithoutTeacherInput = {
  create: ClassCreateWithoutTeacherInput;
  update: ClassUpdateWithoutTeacherInput;
  where: ClassWhereUniqueInput;
};

export type ClassUpsertWithoutLessonsInput = {
  create: ClassCreateWithoutLessonsInput;
  update: ClassUpdateWithoutLessonsInput;
};

export type ClassWhereInput = {
  AND?: Maybe<Array<ClassWhereInput>>;
  NOT?: Maybe<Array<ClassWhereInput>>;
  OR?: Maybe<Array<ClassWhereInput>>;
  createdAt?: Maybe<DateTimeFilter>;
  id?: Maybe<IntFilter>;
  lessons?: Maybe<LessonListRelationFilter>;
  name?: Maybe<StringFilter>;
  students?: Maybe<UserListRelationFilter>;
  teacher?: Maybe<UserWhereInput>;
  teacherId?: Maybe<IntFilter>;
  updatedAt?: Maybe<DateTimeFilter>;
};

export type ClassWhereUniqueInput = {
  id?: Maybe<Scalars['Int']>;
};


export type DateTimeFieldUpdateOperationsInput = {
  set?: Maybe<Scalars['DateTime']>;
};

export type DateTimeFilter = {
  equals?: Maybe<Scalars['DateTime']>;
  gt?: Maybe<Scalars['DateTime']>;
  gte?: Maybe<Scalars['DateTime']>;
  in?: Maybe<Array<Scalars['DateTime']>>;
  lt?: Maybe<Scalars['DateTime']>;
  lte?: Maybe<Scalars['DateTime']>;
  not?: Maybe<NestedDateTimeFilter>;
  notIn?: Maybe<Array<Scalars['DateTime']>>;
};

export type EnumRoleFieldUpdateOperationsInput = {
  set?: Maybe<Role>;
};

export type EnumRoleFilter = {
  equals?: Maybe<Role>;
  in?: Maybe<Array<Role>>;
  not?: Maybe<NestedEnumRoleFilter>;
  notIn?: Maybe<Array<Role>>;
};

export type IntFieldUpdateOperationsInput = {
  decrement?: Maybe<Scalars['Int']>;
  divide?: Maybe<Scalars['Int']>;
  increment?: Maybe<Scalars['Int']>;
  multiply?: Maybe<Scalars['Int']>;
  set?: Maybe<Scalars['Int']>;
};

export type IntFilter = {
  equals?: Maybe<Scalars['Int']>;
  gt?: Maybe<Scalars['Int']>;
  gte?: Maybe<Scalars['Int']>;
  in?: Maybe<Array<Scalars['Int']>>;
  lt?: Maybe<Scalars['Int']>;
  lte?: Maybe<Scalars['Int']>;
  not?: Maybe<NestedIntFilter>;
  notIn?: Maybe<Array<Scalars['Int']>>;
};

export type Lesson = {
  __typename?: 'Lesson';
  class: Class;
  createdAt: Scalars['DateTime'];
  duration: Scalars['Int'];
  id: Scalars['Int'];
  name: Scalars['String'];
  startedAt: Scalars['DateTime'];
  thumbnails: Array<Scalars['String']>;
  updatedAt: Scalars['DateTime'];
};

export type LessonCreateInput = {
  class: ClassCreateNestedOneWithoutLessonsInput;
  createdAt?: Maybe<Scalars['DateTime']>;
  duration: Scalars['Int'];
  name: Scalars['String'];
  startedAt: Scalars['DateTime'];
  thumbnails?: Maybe<LessonCreatethumbnailsInput>;
  updatedAt?: Maybe<Scalars['DateTime']>;
};

export type LessonCreateManyClassInput = {
  createdAt?: Maybe<Scalars['DateTime']>;
  duration: Scalars['Int'];
  id?: Maybe<Scalars['Int']>;
  name: Scalars['String'];
  startedAt: Scalars['DateTime'];
  thumbnails?: Maybe<LessonCreateManythumbnailsInput>;
  updatedAt?: Maybe<Scalars['DateTime']>;
};

export type LessonCreateManyClassInputEnvelope = {
  data?: Maybe<Array<LessonCreateManyClassInput>>;
  skipDuplicates?: Maybe<Scalars['Boolean']>;
};

export type LessonCreateManythumbnailsInput = {
  set?: Maybe<Array<Scalars['String']>>;
};

export type LessonCreateNestedManyWithoutClassInput = {
  connect?: Maybe<Array<LessonWhereUniqueInput>>;
  connectOrCreate?: Maybe<Array<LessonCreateOrConnectWithoutClassInput>>;
  create?: Maybe<Array<LessonCreateWithoutClassInput>>;
  createMany?: Maybe<LessonCreateManyClassInputEnvelope>;
};

export type LessonCreateOrConnectWithoutClassInput = {
  create: LessonCreateWithoutClassInput;
  where: LessonWhereUniqueInput;
};

export type LessonCreateWithoutClassInput = {
  createdAt?: Maybe<Scalars['DateTime']>;
  duration: Scalars['Int'];
  name: Scalars['String'];
  startedAt: Scalars['DateTime'];
  thumbnails?: Maybe<LessonCreatethumbnailsInput>;
  updatedAt?: Maybe<Scalars['DateTime']>;
};

export type LessonCreatethumbnailsInput = {
  set?: Maybe<Array<Scalars['String']>>;
};

export type LessonListRelationFilter = {
  every?: Maybe<LessonWhereInput>;
  none?: Maybe<LessonWhereInput>;
  some?: Maybe<LessonWhereInput>;
};

export type LessonOrderByInput = {
  classId?: Maybe<SortOrder>;
  createdAt?: Maybe<SortOrder>;
  duration?: Maybe<SortOrder>;
  id?: Maybe<SortOrder>;
  name?: Maybe<SortOrder>;
  startedAt?: Maybe<SortOrder>;
  thumbnails?: Maybe<SortOrder>;
  updatedAt?: Maybe<SortOrder>;
};

export type LessonScalarWhereInput = {
  AND?: Maybe<Array<LessonScalarWhereInput>>;
  NOT?: Maybe<Array<LessonScalarWhereInput>>;
  OR?: Maybe<Array<LessonScalarWhereInput>>;
  classId?: Maybe<IntFilter>;
  createdAt?: Maybe<DateTimeFilter>;
  duration?: Maybe<IntFilter>;
  id?: Maybe<IntFilter>;
  name?: Maybe<StringFilter>;
  startedAt?: Maybe<DateTimeFilter>;
  thumbnails?: Maybe<StringNullableListFilter>;
  updatedAt?: Maybe<DateTimeFilter>;
};

export type LessonUpdateInput = {
  class?: Maybe<ClassUpdateOneRequiredWithoutLessonsInput>;
  createdAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
  duration?: Maybe<IntFieldUpdateOperationsInput>;
  name?: Maybe<StringFieldUpdateOperationsInput>;
  startedAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
  thumbnails?: Maybe<LessonUpdatethumbnailsInput>;
  updatedAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
};

export type LessonUpdateManyMutationInput = {
  createdAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
  duration?: Maybe<IntFieldUpdateOperationsInput>;
  name?: Maybe<StringFieldUpdateOperationsInput>;
  startedAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
  thumbnails?: Maybe<LessonUpdatethumbnailsInput>;
  updatedAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
};

export type LessonUpdateManyWithWhereWithoutClassInput = {
  data: LessonUpdateManyMutationInput;
  where: LessonScalarWhereInput;
};

export type LessonUpdateManyWithoutClassInput = {
  connect?: Maybe<Array<LessonWhereUniqueInput>>;
  connectOrCreate?: Maybe<Array<LessonCreateOrConnectWithoutClassInput>>;
  create?: Maybe<Array<LessonCreateWithoutClassInput>>;
  createMany?: Maybe<LessonCreateManyClassInputEnvelope>;
  delete?: Maybe<Array<LessonWhereUniqueInput>>;
  deleteMany?: Maybe<Array<LessonScalarWhereInput>>;
  disconnect?: Maybe<Array<LessonWhereUniqueInput>>;
  set?: Maybe<Array<LessonWhereUniqueInput>>;
  update?: Maybe<Array<LessonUpdateWithWhereUniqueWithoutClassInput>>;
  updateMany?: Maybe<Array<LessonUpdateManyWithWhereWithoutClassInput>>;
  upsert?: Maybe<Array<LessonUpsertWithWhereUniqueWithoutClassInput>>;
};

export type LessonUpdateWithWhereUniqueWithoutClassInput = {
  data: LessonUpdateWithoutClassInput;
  where: LessonWhereUniqueInput;
};

export type LessonUpdateWithoutClassInput = {
  createdAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
  duration?: Maybe<IntFieldUpdateOperationsInput>;
  name?: Maybe<StringFieldUpdateOperationsInput>;
  startedAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
  thumbnails?: Maybe<LessonUpdatethumbnailsInput>;
  updatedAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
};

export type LessonUpdatethumbnailsInput = {
  push?: Maybe<Scalars['String']>;
  set?: Maybe<Array<Scalars['String']>>;
};

export type LessonUpsertWithWhereUniqueWithoutClassInput = {
  create: LessonCreateWithoutClassInput;
  update: LessonUpdateWithoutClassInput;
  where: LessonWhereUniqueInput;
};

export type LessonWhereInput = {
  AND?: Maybe<Array<LessonWhereInput>>;
  NOT?: Maybe<Array<LessonWhereInput>>;
  OR?: Maybe<Array<LessonWhereInput>>;
  class?: Maybe<ClassWhereInput>;
  classId?: Maybe<IntFilter>;
  createdAt?: Maybe<DateTimeFilter>;
  duration?: Maybe<IntFilter>;
  id?: Maybe<IntFilter>;
  name?: Maybe<StringFilter>;
  startedAt?: Maybe<DateTimeFilter>;
  thumbnails?: Maybe<StringNullableListFilter>;
  updatedAt?: Maybe<DateTimeFilter>;
};

export type LessonWhereUniqueInput = {
  id?: Maybe<Scalars['Int']>;
};

export type ModalProps = {
  triggerId: Scalars['String'];
  contentId: Scalars['String'];
  title?: Maybe<Scalars['String']>;
};

export type Mutation = {
  __typename?: 'Mutation';
  createOneClass: Class;
  createOneLesson: Lesson;
  createOneUser: User;
  deleteManyClass: AffectedRowsOutput;
  deleteManyLesson: AffectedRowsOutput;
  deleteManyUser: AffectedRowsOutput;
  deleteOneClass?: Maybe<Class>;
  deleteOneLesson?: Maybe<Lesson>;
  deleteOneUser?: Maybe<User>;
  updateManyClass: AffectedRowsOutput;
  updateManyLesson: AffectedRowsOutput;
  updateManyUser: AffectedRowsOutput;
  updateOneClass?: Maybe<Class>;
  updateOneLesson?: Maybe<Lesson>;
  updateOneUser?: Maybe<User>;
  upsertOneClass: Class;
  upsertOneLesson: Lesson;
  upsertOneUser: User;
};


export type MutationCreateOneClassArgs = {
  data: ClassCreateInput;
};


export type MutationCreateOneLessonArgs = {
  data: LessonCreateInput;
};


export type MutationCreateOneUserArgs = {
  data: UserCreateInput;
};


export type MutationDeleteManyClassArgs = {
  where?: Maybe<ClassWhereInput>;
};


export type MutationDeleteManyLessonArgs = {
  where?: Maybe<LessonWhereInput>;
};


export type MutationDeleteManyUserArgs = {
  where?: Maybe<UserWhereInput>;
};


export type MutationDeleteOneClassArgs = {
  where: ClassWhereUniqueInput;
};


export type MutationDeleteOneLessonArgs = {
  where: LessonWhereUniqueInput;
};


export type MutationDeleteOneUserArgs = {
  where: UserWhereUniqueInput;
};


export type MutationUpdateManyClassArgs = {
  data: ClassUpdateManyMutationInput;
  where?: Maybe<ClassWhereInput>;
};


export type MutationUpdateManyLessonArgs = {
  data: LessonUpdateManyMutationInput;
  where?: Maybe<LessonWhereInput>;
};


export type MutationUpdateManyUserArgs = {
  data: UserUpdateManyMutationInput;
  where?: Maybe<UserWhereInput>;
};


export type MutationUpdateOneClassArgs = {
  data: ClassUpdateInput;
  where: ClassWhereUniqueInput;
};


export type MutationUpdateOneLessonArgs = {
  data: LessonUpdateInput;
  where: LessonWhereUniqueInput;
};


export type MutationUpdateOneUserArgs = {
  data: UserUpdateInput;
  where: UserWhereUniqueInput;
};


export type MutationUpsertOneClassArgs = {
  create: ClassCreateInput;
  update: ClassUpdateInput;
  where: ClassWhereUniqueInput;
};


export type MutationUpsertOneLessonArgs = {
  create: LessonCreateInput;
  update: LessonUpdateInput;
  where: LessonWhereUniqueInput;
};


export type MutationUpsertOneUserArgs = {
  create: UserCreateInput;
  update: UserUpdateInput;
  where: UserWhereUniqueInput;
};

export type NestedDateTimeFilter = {
  equals?: Maybe<Scalars['DateTime']>;
  gt?: Maybe<Scalars['DateTime']>;
  gte?: Maybe<Scalars['DateTime']>;
  in?: Maybe<Array<Scalars['DateTime']>>;
  lt?: Maybe<Scalars['DateTime']>;
  lte?: Maybe<Scalars['DateTime']>;
  not?: Maybe<NestedDateTimeFilter>;
  notIn?: Maybe<Array<Scalars['DateTime']>>;
};

export type NestedEnumRoleFilter = {
  equals?: Maybe<Role>;
  in?: Maybe<Array<Role>>;
  not?: Maybe<NestedEnumRoleFilter>;
  notIn?: Maybe<Array<Role>>;
};

export type NestedIntFilter = {
  equals?: Maybe<Scalars['Int']>;
  gt?: Maybe<Scalars['Int']>;
  gte?: Maybe<Scalars['Int']>;
  in?: Maybe<Array<Scalars['Int']>>;
  lt?: Maybe<Scalars['Int']>;
  lte?: Maybe<Scalars['Int']>;
  not?: Maybe<NestedIntFilter>;
  notIn?: Maybe<Array<Scalars['Int']>>;
};

export type NestedStringFilter = {
  contains?: Maybe<Scalars['String']>;
  endsWith?: Maybe<Scalars['String']>;
  equals?: Maybe<Scalars['String']>;
  gt?: Maybe<Scalars['String']>;
  gte?: Maybe<Scalars['String']>;
  in?: Maybe<Array<Scalars['String']>>;
  lt?: Maybe<Scalars['String']>;
  lte?: Maybe<Scalars['String']>;
  not?: Maybe<NestedStringFilter>;
  notIn?: Maybe<Array<Scalars['String']>>;
  startsWith?: Maybe<Scalars['String']>;
};

export type Query = {
  __typename?: 'Query';
  class?: Maybe<Class>;
  classes: Array<Class>;
  lesson?: Maybe<Lesson>;
  lessons: Array<Lesson>;
  staticComponent: StaticComponent;
  user?: Maybe<User>;
  users: Array<User>;
};


export type QueryClassArgs = {
  where: ClassWhereUniqueInput;
};


export type QueryClassesArgs = {
  after?: Maybe<ClassWhereUniqueInput>;
  before?: Maybe<ClassWhereUniqueInput>;
  first?: Maybe<Scalars['Int']>;
  last?: Maybe<Scalars['Int']>;
  orderBy?: Maybe<Array<ClassOrderByInput>>;
  where?: Maybe<ClassWhereInput>;
};


export type QueryLessonArgs = {
  where: LessonWhereUniqueInput;
};


export type QueryLessonsArgs = {
  after?: Maybe<LessonWhereUniqueInput>;
  before?: Maybe<LessonWhereUniqueInput>;
  first?: Maybe<Scalars['Int']>;
  last?: Maybe<Scalars['Int']>;
  orderBy?: Maybe<Array<LessonOrderByInput>>;
  where?: Maybe<LessonWhereInput>;
};


export type QueryUserArgs = {
  where: UserWhereUniqueInput;
};


export type QueryUsersArgs = {
  after?: Maybe<UserWhereUniqueInput>;
  before?: Maybe<UserWhereUniqueInput>;
  first?: Maybe<Scalars['Int']>;
  last?: Maybe<Scalars['Int']>;
  orderBy?: Maybe<Array<UserOrderByInput>>;
  where?: Maybe<UserWhereInput>;
};

export enum QueryMode {
  Default = 'default',
  Insensitive = 'insensitive'
}

export enum Role {
  Admin = 'ADMIN',
  Student = 'STUDENT',
  Teacher = 'TEACHER'
}

export enum SortOrder {
  Asc = 'asc',
  Desc = 'desc'
}

export type StaticComponent = {
  __typename?: 'StaticComponent';
  button: Scalars['String'];
  modal: Scalars['String'];
};


export type StaticComponentButtonArgs = {
  props?: Maybe<ButtonProps>;
};


export type StaticComponentModalArgs = {
  props?: Maybe<ModalProps>;
};

export type StringFieldUpdateOperationsInput = {
  set?: Maybe<Scalars['String']>;
};

export type StringFilter = {
  contains?: Maybe<Scalars['String']>;
  endsWith?: Maybe<Scalars['String']>;
  equals?: Maybe<Scalars['String']>;
  gt?: Maybe<Scalars['String']>;
  gte?: Maybe<Scalars['String']>;
  in?: Maybe<Array<Scalars['String']>>;
  lt?: Maybe<Scalars['String']>;
  lte?: Maybe<Scalars['String']>;
  mode?: Maybe<QueryMode>;
  not?: Maybe<NestedStringFilter>;
  notIn?: Maybe<Array<Scalars['String']>>;
  startsWith?: Maybe<Scalars['String']>;
};

export type StringNullableListFilter = {
  equals?: Maybe<Array<Scalars['String']>>;
  has?: Maybe<Scalars['String']>;
  hasEvery?: Maybe<Array<Scalars['String']>>;
  hasSome?: Maybe<Array<Scalars['String']>>;
  isEmpty?: Maybe<Scalars['Boolean']>;
};

export type User = {
  __typename?: 'User';
  createdAt: Scalars['DateTime'];
  email: Scalars['String'];
  id: Scalars['Int'];
  name: Scalars['String'];
  participatedClasses: Array<Class>;
  role: Role;
  teachedClasses: Array<Class>;
  updatedAt: Scalars['DateTime'];
};


export type UserParticipatedClassesArgs = {
  after?: Maybe<ClassWhereUniqueInput>;
  before?: Maybe<ClassWhereUniqueInput>;
  first?: Maybe<Scalars['Int']>;
  last?: Maybe<Scalars['Int']>;
  orderBy?: Maybe<Array<ClassOrderByInput>>;
  where?: Maybe<ClassWhereInput>;
};


export type UserTeachedClassesArgs = {
  after?: Maybe<ClassWhereUniqueInput>;
  before?: Maybe<ClassWhereUniqueInput>;
  first?: Maybe<Scalars['Int']>;
  last?: Maybe<Scalars['Int']>;
  orderBy?: Maybe<Array<ClassOrderByInput>>;
  where?: Maybe<ClassWhereInput>;
};

export type UserCreateInput = {
  createdAt?: Maybe<Scalars['DateTime']>;
  email: Scalars['String'];
  name: Scalars['String'];
  participatedClasses?: Maybe<ClassCreateNestedManyWithoutStudentsInput>;
  role: Role;
  teachedClasses?: Maybe<ClassCreateNestedManyWithoutTeacherInput>;
  updatedAt?: Maybe<Scalars['DateTime']>;
};

export type UserCreateNestedManyWithoutParticipatedClassesInput = {
  connect?: Maybe<Array<UserWhereUniqueInput>>;
  connectOrCreate?: Maybe<Array<UserCreateOrConnectWithoutParticipatedClassesInput>>;
  create?: Maybe<Array<UserCreateWithoutParticipatedClassesInput>>;
};

export type UserCreateNestedOneWithoutTeachedClassesInput = {
  connect?: Maybe<UserWhereUniqueInput>;
  connectOrCreate?: Maybe<UserCreateOrConnectWithoutTeachedClassesInput>;
  create?: Maybe<UserCreateWithoutTeachedClassesInput>;
};

export type UserCreateOrConnectWithoutParticipatedClassesInput = {
  create: UserCreateWithoutParticipatedClassesInput;
  where: UserWhereUniqueInput;
};

export type UserCreateOrConnectWithoutTeachedClassesInput = {
  create: UserCreateWithoutTeachedClassesInput;
  where: UserWhereUniqueInput;
};

export type UserCreateWithoutParticipatedClassesInput = {
  createdAt?: Maybe<Scalars['DateTime']>;
  email: Scalars['String'];
  name: Scalars['String'];
  role: Role;
  teachedClasses?: Maybe<ClassCreateNestedManyWithoutTeacherInput>;
  updatedAt?: Maybe<Scalars['DateTime']>;
};

export type UserCreateWithoutTeachedClassesInput = {
  createdAt?: Maybe<Scalars['DateTime']>;
  email: Scalars['String'];
  name: Scalars['String'];
  participatedClasses?: Maybe<ClassCreateNestedManyWithoutStudentsInput>;
  role: Role;
  updatedAt?: Maybe<Scalars['DateTime']>;
};

export type UserListRelationFilter = {
  every?: Maybe<UserWhereInput>;
  none?: Maybe<UserWhereInput>;
  some?: Maybe<UserWhereInput>;
};

export type UserOrderByInput = {
  createdAt?: Maybe<SortOrder>;
  email?: Maybe<SortOrder>;
  id?: Maybe<SortOrder>;
  name?: Maybe<SortOrder>;
  role?: Maybe<SortOrder>;
  updatedAt?: Maybe<SortOrder>;
};

export type UserScalarWhereInput = {
  AND?: Maybe<Array<UserScalarWhereInput>>;
  NOT?: Maybe<Array<UserScalarWhereInput>>;
  OR?: Maybe<Array<UserScalarWhereInput>>;
  createdAt?: Maybe<DateTimeFilter>;
  email?: Maybe<StringFilter>;
  id?: Maybe<IntFilter>;
  name?: Maybe<StringFilter>;
  role?: Maybe<EnumRoleFilter>;
  updatedAt?: Maybe<DateTimeFilter>;
};

export type UserUpdateInput = {
  createdAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
  email?: Maybe<StringFieldUpdateOperationsInput>;
  name?: Maybe<StringFieldUpdateOperationsInput>;
  participatedClasses?: Maybe<ClassUpdateManyWithoutStudentsInput>;
  role?: Maybe<EnumRoleFieldUpdateOperationsInput>;
  teachedClasses?: Maybe<ClassUpdateManyWithoutTeacherInput>;
  updatedAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
};

export type UserUpdateManyMutationInput = {
  createdAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
  email?: Maybe<StringFieldUpdateOperationsInput>;
  name?: Maybe<StringFieldUpdateOperationsInput>;
  role?: Maybe<EnumRoleFieldUpdateOperationsInput>;
  updatedAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
};

export type UserUpdateManyWithWhereWithoutParticipatedClassesInput = {
  data: UserUpdateManyMutationInput;
  where: UserScalarWhereInput;
};

export type UserUpdateManyWithoutParticipatedClassesInput = {
  connect?: Maybe<Array<UserWhereUniqueInput>>;
  connectOrCreate?: Maybe<Array<UserCreateOrConnectWithoutParticipatedClassesInput>>;
  create?: Maybe<Array<UserCreateWithoutParticipatedClassesInput>>;
  delete?: Maybe<Array<UserWhereUniqueInput>>;
  deleteMany?: Maybe<Array<UserScalarWhereInput>>;
  disconnect?: Maybe<Array<UserWhereUniqueInput>>;
  set?: Maybe<Array<UserWhereUniqueInput>>;
  update?: Maybe<Array<UserUpdateWithWhereUniqueWithoutParticipatedClassesInput>>;
  updateMany?: Maybe<Array<UserUpdateManyWithWhereWithoutParticipatedClassesInput>>;
  upsert?: Maybe<Array<UserUpsertWithWhereUniqueWithoutParticipatedClassesInput>>;
};

export type UserUpdateOneRequiredWithoutTeachedClassesInput = {
  connect?: Maybe<UserWhereUniqueInput>;
  connectOrCreate?: Maybe<UserCreateOrConnectWithoutTeachedClassesInput>;
  create?: Maybe<UserCreateWithoutTeachedClassesInput>;
  update?: Maybe<UserUpdateWithoutTeachedClassesInput>;
  upsert?: Maybe<UserUpsertWithoutTeachedClassesInput>;
};

export type UserUpdateWithWhereUniqueWithoutParticipatedClassesInput = {
  data: UserUpdateWithoutParticipatedClassesInput;
  where: UserWhereUniqueInput;
};

export type UserUpdateWithoutParticipatedClassesInput = {
  createdAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
  email?: Maybe<StringFieldUpdateOperationsInput>;
  name?: Maybe<StringFieldUpdateOperationsInput>;
  role?: Maybe<EnumRoleFieldUpdateOperationsInput>;
  teachedClasses?: Maybe<ClassUpdateManyWithoutTeacherInput>;
  updatedAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
};

export type UserUpdateWithoutTeachedClassesInput = {
  createdAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
  email?: Maybe<StringFieldUpdateOperationsInput>;
  name?: Maybe<StringFieldUpdateOperationsInput>;
  participatedClasses?: Maybe<ClassUpdateManyWithoutStudentsInput>;
  role?: Maybe<EnumRoleFieldUpdateOperationsInput>;
  updatedAt?: Maybe<DateTimeFieldUpdateOperationsInput>;
};

export type UserUpsertWithWhereUniqueWithoutParticipatedClassesInput = {
  create: UserCreateWithoutParticipatedClassesInput;
  update: UserUpdateWithoutParticipatedClassesInput;
  where: UserWhereUniqueInput;
};

export type UserUpsertWithoutTeachedClassesInput = {
  create: UserCreateWithoutTeachedClassesInput;
  update: UserUpdateWithoutTeachedClassesInput;
};

export type UserWhereInput = {
  AND?: Maybe<Array<UserWhereInput>>;
  NOT?: Maybe<Array<UserWhereInput>>;
  OR?: Maybe<Array<UserWhereInput>>;
  createdAt?: Maybe<DateTimeFilter>;
  email?: Maybe<StringFilter>;
  id?: Maybe<IntFilter>;
  name?: Maybe<StringFilter>;
  participatedClasses?: Maybe<ClassListRelationFilter>;
  role?: Maybe<EnumRoleFilter>;
  teachedClasses?: Maybe<ClassListRelationFilter>;
  updatedAt?: Maybe<DateTimeFilter>;
};

export type UserWhereUniqueInput = {
  email?: Maybe<Scalars['String']>;
  id?: Maybe<Scalars['Int']>;
};

export type ClassTableQueryVariables = Exact<{ [key: string]: never; }>;


export type ClassTableQuery = (
  { __typename?: 'Query' }
  & { classes: Array<(
    { __typename?: 'Class' }
    & Pick<Class, 'id' | 'name'>
    & { teacher: (
      { __typename?: 'User' }
      & Pick<User, 'id' | 'name'>
    ), students: Array<(
      { __typename?: 'User' }
      & Pick<User, 'id' | 'name'>
    )> }
  )> }
);

export type CreateOneClassFormMutationVariables = Exact<{
  data: ClassCreateInput;
}>;


export type CreateOneClassFormMutation = (
  { __typename?: 'Mutation' }
  & { createOneClass: (
    { __typename?: 'Class' }
    & Pick<Class, 'id' | 'name'>
    & { teacher: (
      { __typename?: 'User' }
      & Pick<User, 'id' | 'name'>
    ) }
  ) }
);

export type LessonTableQueryVariables = Exact<{ [key: string]: never; }>;


export type LessonTableQuery = (
  { __typename?: 'Query' }
  & { lessons: Array<(
    { __typename?: 'Lesson' }
    & Pick<Lesson, 'id' | 'name' | 'startedAt' | 'duration' | 'thumbnails'>
    & { class: (
      { __typename?: 'Class' }
      & Pick<Class, 'id' | 'name'>
      & { teacher: (
        { __typename?: 'User' }
        & Pick<User, 'id' | 'name'>
      ), students: Array<(
        { __typename?: 'User' }
        & Pick<User, 'id' | 'name'>
      )> }
    ) }
  )> }
);

export type LessonCalendarQueryVariables = Exact<{ [key: string]: never; }>;


export type LessonCalendarQuery = (
  { __typename?: 'Query' }
  & { lessons: Array<(
    { __typename?: 'Lesson' }
    & Pick<Lesson, 'id' | 'name' | 'startedAt' | 'duration'>
    & { class: (
      { __typename?: 'Class' }
      & Pick<Class, 'id' | 'name'>
    ) }
  )> }
);

export type CreateOneLessonFormMutationVariables = Exact<{
  data: LessonCreateInput;
}>;


export type CreateOneLessonFormMutation = (
  { __typename?: 'Mutation' }
  & { createOneLesson: (
    { __typename?: 'Lesson' }
    & Pick<Lesson, 'id' | 'name' | 'startedAt' | 'duration'>
    & { class: (
      { __typename?: 'Class' }
      & Pick<Class, 'id' | 'name'>
    ) }
  ) }
);

export type UserTableQueryVariables = Exact<{ [key: string]: never; }>;


export type UserTableQuery = (
  { __typename?: 'Query' }
  & { users: Array<(
    { __typename?: 'User' }
    & Pick<User, 'id' | 'name' | 'role' | 'email' | 'createdAt'>
  )> }
);

export type UserListQueryVariables = Exact<{ [key: string]: never; }>;


export type UserListQuery = (
  { __typename?: 'Query' }
  & { users: Array<(
    { __typename?: 'User' }
    & Pick<User, 'id' | 'name' | 'role'>
  )> }
);

export type UserKanbanQueryVariables = Exact<{ [key: string]: never; }>;


export type UserKanbanQuery = (
  { __typename?: 'Query' }
  & { users: Array<(
    { __typename?: 'User' }
    & Pick<User, 'id' | 'name' | 'role'>
  )> }
);

export type CreateOneUserFormMutationVariables = Exact<{
  data: UserCreateInput;
}>;


export type CreateOneUserFormMutation = (
  { __typename?: 'Mutation' }
  & { createOneUser: (
    { __typename?: 'User' }
    & Pick<User, 'id' | 'name' | 'role' | 'createdAt'>
  ) }
);

export type UpdateOneUserFormMutationVariables = Exact<{
  data: UserUpdateInput;
  where: UserWhereUniqueInput;
}>;


export type UpdateOneUserFormMutation = (
  { __typename?: 'Mutation' }
  & { updateOneUser?: Maybe<(
    { __typename?: 'User' }
    & Pick<User, 'id' | 'name' | 'role' | 'createdAt'>
  )> }
);

export type DeleteOneUserFormMutationVariables = Exact<{
  where: UserWhereUniqueInput;
}>;


export type DeleteOneUserFormMutation = (
  { __typename?: 'Mutation' }
  & { deleteOneUser?: Maybe<(
    { __typename?: 'User' }
    & Pick<User, 'id'>
  )> }
);


export const ClassTableDocument = gql`
    query classTable {
  classes {
    id
    name
    teacher {
      id
      name
    }
    students {
      id
      name
    }
  }
}
    `;

export function useClassTableQuery(options: Omit<Urql.UseQueryArgs<ClassTableQueryVariables>, 'query'> = {}) {
  return Urql.useQuery<ClassTableQuery>({ query: ClassTableDocument, ...options });
};
export const CreateOneClassFormDocument = gql`
    mutation createOneClassForm($data: ClassCreateInput!) {
  createOneClass(data: $data) {
    id
    name
    teacher {
      id
      name
    }
  }
}
    `;

export function useCreateOneClassFormMutation() {
  return Urql.useMutation<CreateOneClassFormMutation, CreateOneClassFormMutationVariables>(CreateOneClassFormDocument);
};
export const LessonTableDocument = gql`
    query lessonTable {
  lessons {
    id
    name
    class {
      id
      name
      teacher {
        id
        name
      }
      students {
        id
        name
      }
    }
    class {
      teacher {
        id
        name
      }
    }
    class {
      students {
        id
        name
      }
    }
    startedAt
    duration
    thumbnails
  }
}
    `;

export function useLessonTableQuery(options: Omit<Urql.UseQueryArgs<LessonTableQueryVariables>, 'query'> = {}) {
  return Urql.useQuery<LessonTableQuery>({ query: LessonTableDocument, ...options });
};
export const LessonCalendarDocument = gql`
    query lessonCalendar {
  lessons {
    id
    name
    class {
      id
      name
    }
    startedAt
    duration
  }
}
    `;

export function useLessonCalendarQuery(options: Omit<Urql.UseQueryArgs<LessonCalendarQueryVariables>, 'query'> = {}) {
  return Urql.useQuery<LessonCalendarQuery>({ query: LessonCalendarDocument, ...options });
};
export const CreateOneLessonFormDocument = gql`
    mutation createOneLessonForm($data: LessonCreateInput!) {
  createOneLesson(data: $data) {
    id
    name
    class {
      id
      name
    }
    startedAt
    duration
  }
}
    `;

export function useCreateOneLessonFormMutation() {
  return Urql.useMutation<CreateOneLessonFormMutation, CreateOneLessonFormMutationVariables>(CreateOneLessonFormDocument);
};
export const UserTableDocument = gql`
    query userTable {
  users(where: {}, orderBy: [{id: desc}]) {
    id
    name
    role
    email
    createdAt
  }
}
    `;

export function useUserTableQuery(options: Omit<Urql.UseQueryArgs<UserTableQueryVariables>, 'query'> = {}) {
  return Urql.useQuery<UserTableQuery>({ query: UserTableDocument, ...options });
};
export const UserListDocument = gql`
    query userList {
  users {
    id
    name
    role
  }
}
    `;

export function useUserListQuery(options: Omit<Urql.UseQueryArgs<UserListQueryVariables>, 'query'> = {}) {
  return Urql.useQuery<UserListQuery>({ query: UserListDocument, ...options });
};
export const UserKanbanDocument = gql`
    query userKanban {
  users {
    id
    name
    role
  }
}
    `;

export function useUserKanbanQuery(options: Omit<Urql.UseQueryArgs<UserKanbanQueryVariables>, 'query'> = {}) {
  return Urql.useQuery<UserKanbanQuery>({ query: UserKanbanDocument, ...options });
};
export const CreateOneUserFormDocument = gql`
    mutation createOneUserForm($data: UserCreateInput!) {
  createOneUser(data: $data) {
    id
    name
    role
    createdAt
  }
}
    `;

export function useCreateOneUserFormMutation() {
  return Urql.useMutation<CreateOneUserFormMutation, CreateOneUserFormMutationVariables>(CreateOneUserFormDocument);
};
export const UpdateOneUserFormDocument = gql`
    mutation updateOneUserForm($data: UserUpdateInput!, $where: UserWhereUniqueInput!) {
  updateOneUser(data: $data, where: $where) {
    id
    name
    role
    createdAt
  }
}
    `;

export function useUpdateOneUserFormMutation() {
  return Urql.useMutation<UpdateOneUserFormMutation, UpdateOneUserFormMutationVariables>(UpdateOneUserFormDocument);
};
export const DeleteOneUserFormDocument = gql`
    mutation deleteOneUserForm($where: UserWhereUniqueInput!) {
  deleteOneUser(where: $where) {
    id
  }
}
    `;

export function useDeleteOneUserFormMutation() {
  return Urql.useMutation<DeleteOneUserFormMutation, DeleteOneUserFormMutationVariables>(DeleteOneUserFormDocument);
};