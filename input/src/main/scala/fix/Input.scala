package fix

import doobie._
import doobie.hi._
import doobie.h2.H2Transactor

object Database {
  def transactor = doobie.Transactor.fromDriverManager[IO](
    "org.h2.Driver",
    "jdbc:h2:mem:test"
  )
  
  type Alias = doobie.ConnectionIO[Int]
}
