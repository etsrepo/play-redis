package play.api.cache.redis

import scala.concurrent.duration._

import org.specs2.mutable.Specification

/**
 * <p>Test of cache to be sure that keys are differentiated, expires etc.</p>
 */
class ModuleSpec extends Specification with Redis {

  private val Cache = injector.instanceOf[ CacheApi ]

  "Cache" should {

    "miss on get" in {
      Cache.get[ String ]( "module-test-1" ) must beNone
    }

    "hit after set" in {
      Cache.set( "module-test-2", "value" )
      Cache.get[ String ]( "module-test-2" ) must beSome[ Any ]
      Cache.get[ String ]( "module-test-2" ) must beSome( "value" )
    }

    "expire refreshes expiration" in {
      Cache.set( "module-test-10", "value", 2.second )
      Cache.get[ String ]( "module-test-10" ) must beSome( "value" )
      Cache.expire( "module-test-10", 1.minute )
      // wait until the first duration expires
      Thread.sleep( 3000 )
      Cache.get[ String ]( "module-test-10" ) must beSome( "value" )
    }

    "positive exists on existing keys" in {
      Cache.set( "module-test-11", "value" )
      Cache.exists( "module-test-11" ) must beTrue
    }

    "negative exists on expired and missing keys" in {
      Cache.set( "module-test-12A", "value", 1.second )
      // wait until the duration expires
      Thread.sleep( 2000 )
      Cache.exists( "module-test-12A" ) must beFalse
      Cache.exists( "module-test-12B" ) must beFalse
    }

    "miss after remove" in {
      Cache.set( "module-test-3", "value" )
      Cache.get[ String ]( "module-test-3" ) must beSome[ Any ]
      Cache.remove( "module-test-3" )
      Cache.get[ String ]( "module-test-3" ) must beNone
    }
  }
}
