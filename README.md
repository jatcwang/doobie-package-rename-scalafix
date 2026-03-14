# Doobie package rename scalafix rule 

Doobie has moved under the Typelevel organisation and as part of that 
doobie will be published under `org.typelevel` instead of `org.tpolecat`.

To avoid breakages, it is necessary for us to rename any references to 
`_root_.doobie` to `org.typelevel.doobie`.

This scalfix rule will perform that rename for you across your project.

