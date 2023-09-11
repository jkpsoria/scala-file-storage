package scala.util
  sealed abstract class Either[+A, +B] extends Product with Serializable:
    def isLeft: Boolean
    def isRight: Boolean

    def fold[C](fa: A => C, fb: B => C): C = this match
      case Right(b) => fb(b)
      case Left(a) => fa(a)

    def swap: Either[B, A] = this match
      case Left(a) => Right(a)
      case Right(b) => Left(b)

    def joinRight[A1 >: A, B1 >: B, C](using B1 <:< Either[A1, C]): Either[A1, C] = this match
      case Right(b) => b
      case _        => this.asInstanceOf[Either[A1, C]]

    def joinLeft[A1 >: A, B1 >: B, C](using A1 <:< Either[C, B1]): Either[C, B1] = this match
      case Left(a) => a
      case _       => this.asInstanceOf[Either[C, B1]]

    def foreach[U](f: B => U): Unit = this match
      case Right(b) => f(b)
      case _        =>

    def getOrElse[B1 >: B](or: => B1): B1 = this match
      case Right(b) => b
      case _        => or

    def orElse[A1 >: A, B1 >: B](or: => Either[A1, B1]): Either[A1, B1] = this match
      case Right(_) => this
      case _        => or

    final def contains[B1 >: B](elem: B1): Boolean = this match
      case Right(b) => b == elem
      case _        => false

    def forall(f: B => Boolean): Boolean = this match
      case Right(b) => f(b)
      case _        => true

    def exists(p: B => Boolean): Boolean = this match
      case Right(b) => p(b)
      case _        => false

    def flatMap[A1 >: A, B1](f: B => Either[A1, B1]): Either[A1, B1] = this match
      case Right(b) => f(b)
      case _        => this.asInstanceOf[Either[A1, B1]]

    def flatten[A1 >: A, B1](implicit ev: B <:< Either[A1, B1]): Either[A1, B1] =
      flatMap(ev)

    def map[B1](f: B => B1): Either[A, B1] = this match
      case Right(b) => Right(f(b))
      case _        => this.asInstanceOf[Either[A, B1]]

    def filterOrElse[A1 >: A](p: B => Boolean, zero: => A1): Either[A1, B] = this match
      case Right(b) if !p(b) => Left(zero)
      case _                 => this

    def toSeq: Seq[B] = this match
      case Right(b) => Seq(b)
      case _        => Seq.empty

    def toOption: Option[B] = this match
      case Right(b) => Some(b)
      case _        => None

    def toTry(implicit ev: A <:< Throwable): Try[B] = this match
      case Right(b) => Success(b)
      case Left(a)  => Failure(a)

  final case class Left[+A, +B](value: A) extends Either[A, B]:
    def isLeft  = true
    def isRight = false

  final case class Right[+A, +B](value: B) extends Either[A, B]:
    def isLeft  = false
    def isRight = true

  object Either:
    def cond[A, B](test: Boolean, right: => B, left: => A): Either[A, B] =
      if (test) Right(right) else Left(left)
