package swimmer

sealed trait Entity:
  val id: Long

  final case class Swimmer(id: Long = 0) extends Entity