package testcases

import org.typelevel.doobie._
import org.typelevel.doobie.Fragment.empty

object App {
  
  val v: Fragment = org.typelevel.doobie.Fragment.empty

  type X = org.typelevel.doobie.SomeAlias

  val x = org.typelevel.doobie.syntax.all.x

  val em: org.typelevel.doobie.Fragment = empty

  val n = another.doobie.notThisOne
}
