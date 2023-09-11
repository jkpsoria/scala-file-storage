/* Please README */
/* - Ignore `futureQuiz` method */
/* - Do not use `Option.get` and `for` comprehension.*/
/* - `None: Option[Int]` as 0 */
/* - Answer #1 to #9 in the code block like Example Quiz at line 42*/

import scala.concurrent.{ Await, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.util.Success

/* ---------- NOT YOUR BUSINESS - NO CHANGE ----------*/
def futureQuiz[T](question: String, assertion: String, expectation: T)(answer: => Future[T] | Unit): Future[T] =
  println(question)
  answer match
    case f: Future[_] =>
      f.map { value =>
        if (value == expectation) then
          println("    ✅ Correct")
          value
        else
          println(s"    ❌ Incorrect value ($value)")
          throw new AssertionError(s"\"$question\" is unanswered or incorrect.")
      }
      Await.ready(f, 10.seconds)
    case _ =>
      println("    😑 Unanswered or incorrect")
      Future.failed(new AssertionError(s"\"$question\" is unanswered or incorrect."))
/* ---------------------------------------------------*/

def f1: Future[Int] = Future.successful(1)
def f2: Future[Option[Int]] = Future.successful(Some(10))
def f3: Future[Option[Int]] = Future.successful(None)
def f4: Future[Seq[Int]] = Future.successful(1 to 10)
def f5: Future[Seq[Int]] = Future.successful(Nil)
def f6: Future[Option[Seq[Int]]] = Future.successful(Some(1 to 10))
def f7: Future[Option[Seq[Int]]] = Future.successful(None)
def f8: Future[Seq[Option[Int]]] = Future.successful(Seq(Some(100), Some(1000), None))
def f9: Future[Option[Future[Int]]] = Future.successful(Some(Future.successful(1)))
def f10: Future[Seq[Future[Int]]] = Future.successful((1 to 10).map(Future.successful))

@main def futureQuiz0830() =
  futureQuiz("Example Quiz: Do f1 + f2", "Answer should be", 11) {
    // Your code here...
    f1.flatMap(v => f2.map(_.getOrElse(0) + v))
  }

  futureQuiz("#1: Do f1 + f2 + f3", "Answer should be", 11) {
    // Your code here...
  }

  futureQuiz("#2: Get sum total of f4", "Answer should be", 55) {
    // Your code here...
  }

  futureQuiz("#3: Get sum total of f4 and f5", "Answer should be", 55) {
    // Your code here...
  }

  futureQuiz("#4: Transform f4 to `Future[Seq[Int], Seq[Int]]`", "Answer should be", (1 to 5, 6 to 10)) {
    // Answer should be like `Future((1 to 5, 6 to 10))`
    // Your code here...
  }

  futureQuiz(
      "#5: Transform f6 to `Future[Seq[Option[Int]]]`",
      "Answer should be",
      Seq(Some(1), Some(2), Some(3), Some(4), Some(5), Some(6), Some(7), Some(8), Some(9), Some(10))
  ) {
    // Your code here...
  }

  futureQuiz(
      "#6: Merge f6 and f7 to get `Future[Option[Seq[Int]]]`, and then transfer it to `Future[Seq[Int]]`",
      "Answer should be",
      Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  ) {
    // Answer should be like `Future((1 to 5, 6 to 10))`
    // Your code here...
  }

  futureQuiz("#7: Get sum total of f8", "Answer should be", 1100) {
    // Your code here...
  }

  futureQuiz("#8: Do f9 + f1", "Answer should be", 2) {
    // Your code here...
  }

  futureQuiz("#9: Get sum total of f10 and f4", "Answer should be", 110) {
    // Your code here...
  }
