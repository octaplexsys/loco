package loco.repository

import cats.data.NonEmptyList
import cats.effect.IO
import doobie.util.transactor.Transactor
import loco.EmbeddedDBEnv._
import loco.domain.AggregateVersion
import loco.test.FakeTimer
import loco.{IncrementFixture, UnitSpec}

class DoobieEventsRepositoryTest extends UnitSpec {

  trait ctx extends IncrementFixture {
    val transactor = Transactor.fromDriverManager[IO](
      "com.mysql.cj.jdbc.Driver",
      s"jdbc:mysql://localhost:$port/$schema",
      username,
      password)

    val codec = new Codec[IncrementEvent] {
      override def encode(e: IncrementEvent) = e.id

      override def decode(e: String) = IncrementEvent(e)
    }
    val repository = DoobieEventsRepository[IO, IncrementEvent](codec, transactor, "increment_events")
    val timer = FakeTimer[IO]()
  }

  "Doobie events repository" should "save events and retrieve events" in new ctx {

    val metaEvents = NonEmptyList.fromListUnsafe(List(
      metaEvent1(timer.instant),
      metaEvent2({
        timer.tick
        timer.instant
      }))
    )

    repository.saveEvents(metaEvents).unsafeRunSync()

    repository.fetchEvents(id, AggregateVersion.max).compile.to[List].unsafeRunSync() shouldBe metaEvents.toList
  }


}
