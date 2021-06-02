import { objectType } from "nexus";

const FILTER_ORDER_PAGINATION = {
  filtering: true,
  pagination: true,
  ordering: true,
} as const;

export const User = objectType({
  name: "User",
  definition(t) {
          t.model.id();
          t.model.email();
          t.model.role();
          t.model.name();
          t.model.teachedClasses(FILTER_ORDER_PAGINATION);
          t.model.participatedClasses(FILTER_ORDER_PAGINATION);
          t.model.createdAt();
          t.model.updatedAt();
      },
});

export const Class = objectType({
  name: "Class",
  definition(t) {
          t.model.id();
          t.model.name();
          t.model.teacher();
          t.model.students(FILTER_ORDER_PAGINATION);
          t.model.lessons(FILTER_ORDER_PAGINATION);
          t.model.createdAt();
          t.model.updatedAt();
      },
});

export const Lesson = objectType({
  name: "Lesson",
  definition(t) {
          t.model.id();
          t.model.name();
          t.model.class();
          t.model.startedAt();
          t.model.duration();
          t.model.thumbnails();
          t.model.createdAt();
          t.model.updatedAt();
      },
});


export const Query = objectType({
  name: 'Query',
  definition(t) {
    
      t.crud.user();
      t.crud.users(FILTER_ORDER_PAGINATION);
    
      t.crud.class();
      t.crud.classes(FILTER_ORDER_PAGINATION);
    
      t.crud.lesson();
      t.crud.lessons(FILTER_ORDER_PAGINATION);
    
  },
});

export const Mutation = objectType({
  name: 'Mutation',
  definition(t) {
    
      t.crud.createOneUser();
      t.crud.updateOneUser();
      t.crud.upsertOneUser();
      t.crud.deleteOneUser();
      t.crud.updateManyUser();
      t.crud.deleteManyUser();
    
      t.crud.createOneClass();
      t.crud.updateOneClass();
      t.crud.upsertOneClass();
      t.crud.deleteOneClass();
      t.crud.updateManyClass();
      t.crud.deleteManyClass();
    
      t.crud.createOneLesson();
      t.crud.updateOneLesson();
      t.crud.upsertOneLesson();
      t.crud.deleteOneLesson();
      t.crud.updateManyLesson();
      t.crud.deleteManyLesson();
    
  }
})
