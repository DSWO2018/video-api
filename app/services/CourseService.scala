package services

import models.{Course, CourseRepository}

import scala.concurrent.Future

class CourseService(courseRepository: CourseRepository) {
  def getCourses(): Future[Seq[Course]] ={
    courseRepository.getCourses()
  }
  def getCourse(courseId: Int): Future[Seq[Course]] ={
    courseRepository.getCourse(courseId)
  }
}