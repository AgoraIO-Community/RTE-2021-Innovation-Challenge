/*
  Warnings:

  - Added the required column `startedAt` to the `Lesson` table without a default value. This is not possible if the table is not empty.
  - Added the required column `duration` to the `Lesson` table without a default value. This is not possible if the table is not empty.

*/
-- AlterTable
ALTER TABLE "Lesson" ADD COLUMN     "startedAt" TIMESTAMP(3) NOT NULL,
ADD COLUMN     "duration" INTEGER NOT NULL;
