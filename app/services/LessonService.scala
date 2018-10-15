package services

import models.{Lesson, LessonRepository}

import scala.concurrent.Future

class LessonService(lessonRepository: LessonRepository) {
  def getLessons(courseId: Int): Future[Seq[Lesson]] ={
    lessonRepository.getLessons(courseId)
  }
  def getLesson(lessonId: Int): Future[Seq[Lesson]] ={
    lessonRepository.getLesson(lessonId)
  }
}