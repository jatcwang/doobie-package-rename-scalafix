package fix

import scalafix.v1._
import scala.meta._

class DoobiePackageRename extends SemanticRule("DoobiePackageRename") {
  
  private val newPackage = Term.Select(Term.Select(Term.Name("org"), Term.Name("typelevel")), Term.Name("doobie"))
  
}
