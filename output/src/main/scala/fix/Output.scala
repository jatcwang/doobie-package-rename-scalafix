package fix

import org.typelevel.doobie._
import org.typelevel.doobie.hi._
import org.typelevel.doobie.h2.H2Transactor

object Database {
  def transactor = org.typelevel.doobie.Transactor.fromDriverManager[IO](
    "org.h2.Driver",
    "jdbc:h2:mem:test"
  )

  type Alias = org.typelevel.doobie.ConnectionIO[Int]
}
