/*
rule = DoobiePackageRename
*/
package testcases

import doobie.*
import doobie.Fragment.empty

object App {
  
  val v: Fragment = doobie.Fragment.empty

  type X = doobie.SomeAlias

  val x = doobie.syntax.all.x

  val em: doobie.Fragment = empty

  val n = another.doobie.notThisOne
}
