trait Loggable[A]:
  def log(a: A): String

given Loggable[String] = (a: String) => a

given Loggable[Int] with
  def log(a: Int): String = s"Int: $a"

extension [A](a: A)
  def logIt(using loggable: Loggable[A]): String = loggable.log(a)

trait Writer(msg: String):
  def write(): Unit = println(msg.logIt)

class WriterImpl(msg: String) extends Writer(msg)

trait Formatter:
  def format(str: String): String = s"Formatted: $str"

class FormatterImpl extends Formatter

class SayHello(writer: Writer, formatter: Formatter):
  def sayHello(): Unit =
    val message = formatter.format("Hello, World!")
    val customizedWriter = new Writer(message) {
      override def write(): Unit = println(message.logIt)
    }
    customizedWriter.write()

@main def helloWorld() =
  val writer = new WriterImpl("Default Message")
  // Outputs: Default Message
  writer.write()

  val formatter = new FormatterImpl

  val sh = new SayHello(writer, formatter)
  // Outputs: Formatted: Hello, World!
  sh.sayHello()
