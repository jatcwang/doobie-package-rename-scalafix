package org.typelevel.doobie

case class Fragment(str: String)

object Fragment {
  val empty = Fragment("")
}

object syntax {
  object all {
    val x: Int = 1
  }
}
