package testcases

import scalafix.v1._
import scala.meta._

class DoobiePackageRename extends SemanticRule("DoobiePackageRename") {

  private val newPackage = "org.typelevel.doobie"

  private def isDoobiePackage(sym: Symbol): Boolean = {
    val normalized = sym.normalized.value
    normalized == "doobie." || normalized == "_root_.doobie."
  }

  private def isDoobiePackageName(name: String): Boolean = {
    name == "doobie" || name == "_root_.doobie"
  }

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case ref @ Term.Select(qual: Term.Name, name) if isDoobiePackage(qual.symbol) =>
        Patch.replaceTree(ref, s"$newPackage.$name")
      case ref @ Type.Select(qual: Term.Name, name) if isDoobiePackage(qual.symbol) =>
        Patch.replaceTree(ref, s"$newPackage.$name")
      case Importer(refTerm, _) if isDoobiePackage(refTerm.symbol) =>
        Patch.replaceTree(refTerm, newPackage)
      // Handle package declarations
      case Pkg.After_4_9_9(ref, _) =>
        val pkgName = ref.syntax
        if (isDoobiePackageName(pkgName)) {
          Patch.replaceTree(ref, newPackage)
        } else {
          Patch.empty
        }
    }.asPatch
  }

}
