import * as Typegen from 'nexus-plugin-prisma/typegen'
import * as Prisma from '@prisma/client';

// Pagination type
type Pagination = {
    first?: boolean
    last?: boolean
    before?: boolean
    after?: boolean
}

// Prisma custom scalar names
type CustomScalars = 'DateTime'

// Prisma model type definitions
interface PrismaModels {
  User: Prisma.User
  Class: Prisma.Class
  Lesson: Prisma.Lesson
}

// Prisma input types metadata
interface NexusPrismaInputs {
  Query: {
    users: {
      filtering: 'AND' | 'OR' | 'NOT' | 'id' | 'email' | 'role' | 'name' | 'teachedClasses' | 'participatedClasses' | 'createdAt' | 'updatedAt'
      ordering: 'id' | 'email' | 'role' | 'name' | 'createdAt' | 'updatedAt'
    }
    classes: {
      filtering: 'AND' | 'OR' | 'NOT' | 'id' | 'name' | 'teacher' | 'teacherId' | 'students' | 'lessons' | 'createdAt' | 'updatedAt'
      ordering: 'id' | 'name' | 'teacherId' | 'createdAt' | 'updatedAt'
    }
    lessons: {
      filtering: 'AND' | 'OR' | 'NOT' | 'id' | 'name' | 'class' | 'classId' | 'startedAt' | 'duration' | 'thumbnails' | 'createdAt' | 'updatedAt'
      ordering: 'id' | 'name' | 'classId' | 'startedAt' | 'duration' | 'thumbnails' | 'createdAt' | 'updatedAt'
    }
  },
  User: {
    teachedClasses: {
      filtering: 'AND' | 'OR' | 'NOT' | 'id' | 'name' | 'teacher' | 'teacherId' | 'students' | 'lessons' | 'createdAt' | 'updatedAt'
      ordering: 'id' | 'name' | 'teacherId' | 'createdAt' | 'updatedAt'
    }
    participatedClasses: {
      filtering: 'AND' | 'OR' | 'NOT' | 'id' | 'name' | 'teacher' | 'teacherId' | 'students' | 'lessons' | 'createdAt' | 'updatedAt'
      ordering: 'id' | 'name' | 'teacherId' | 'createdAt' | 'updatedAt'
    }
  }
  Class: {
    students: {
      filtering: 'AND' | 'OR' | 'NOT' | 'id' | 'email' | 'role' | 'name' | 'teachedClasses' | 'participatedClasses' | 'createdAt' | 'updatedAt'
      ordering: 'id' | 'email' | 'role' | 'name' | 'createdAt' | 'updatedAt'
    }
    lessons: {
      filtering: 'AND' | 'OR' | 'NOT' | 'id' | 'name' | 'class' | 'classId' | 'startedAt' | 'duration' | 'thumbnails' | 'createdAt' | 'updatedAt'
      ordering: 'id' | 'name' | 'classId' | 'startedAt' | 'duration' | 'thumbnails' | 'createdAt' | 'updatedAt'
    }
  }
  Lesson: {

  }
}

// Prisma output types metadata
interface NexusPrismaOutputs {
  Query: {
    user: 'User'
    users: 'User'
    class: 'Class'
    classes: 'Class'
    lesson: 'Lesson'
    lessons: 'Lesson'
  },
  Mutation: {
    createOneUser: 'User'
    updateOneUser: 'User'
    updateManyUser: 'AffectedRowsOutput'
    deleteOneUser: 'User'
    deleteManyUser: 'AffectedRowsOutput'
    upsertOneUser: 'User'
    createOneClass: 'Class'
    updateOneClass: 'Class'
    updateManyClass: 'AffectedRowsOutput'
    deleteOneClass: 'Class'
    deleteManyClass: 'AffectedRowsOutput'
    upsertOneClass: 'Class'
    createOneLesson: 'Lesson'
    updateOneLesson: 'Lesson'
    updateManyLesson: 'AffectedRowsOutput'
    deleteOneLesson: 'Lesson'
    deleteManyLesson: 'AffectedRowsOutput'
    upsertOneLesson: 'Lesson'
  },
  User: {
    id: 'Int'
    email: 'String'
    role: 'Role'
    name: 'String'
    teachedClasses: 'Class'
    participatedClasses: 'Class'
    createdAt: 'DateTime'
    updatedAt: 'DateTime'
  }
  Class: {
    id: 'Int'
    name: 'String'
    teacher: 'User'
    teacherId: 'Int'
    students: 'User'
    lessons: 'Lesson'
    createdAt: 'DateTime'
    updatedAt: 'DateTime'
  }
  Lesson: {
    id: 'Int'
    name: 'String'
    class: 'Class'
    classId: 'Int'
    startedAt: 'DateTime'
    duration: 'Int'
    thumbnails: 'String'
    createdAt: 'DateTime'
    updatedAt: 'DateTime'
  }
}

// Helper to gather all methods relative to a model
interface NexusPrismaMethods {
  User: Typegen.NexusPrismaFields<'User'>
  Class: Typegen.NexusPrismaFields<'Class'>
  Lesson: Typegen.NexusPrismaFields<'Lesson'>
  Query: Typegen.NexusPrismaFields<'Query'>
  Mutation: Typegen.NexusPrismaFields<'Mutation'>
}

interface NexusPrismaGenTypes {
  inputs: NexusPrismaInputs
  outputs: NexusPrismaOutputs
  methods: NexusPrismaMethods
  models: PrismaModels
  pagination: Pagination
  scalars: CustomScalars
}

declare global {
  interface NexusPrismaGen extends NexusPrismaGenTypes {}

  type NexusPrisma<
    TypeName extends string,
    ModelOrCrud extends 'model' | 'crud'
  > = Typegen.GetNexusPrisma<TypeName, ModelOrCrud>;
}
  