package play.api.cache.redis

import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.reflect.ClassTag

/**
  * Internal non-blocking Redis API implementing REDIS protocol
  *
  * @see http://redis.io/commands
  * @author Karel Cemus
  */
trait RedisConnector {

  /** Retrieve a value from the cache.
    *
    * @param key cache storage key
    * @return stored record, Some if exists, otherwise None
    */
  def get[ T: ClassTag ]( key: String ): Future[ Option[ T ] ]

  /** Determines whether value exists in cache.
    *
    * @param key cache storage key
    * @return record existence, true if exists, otherwise false
    */
  def exists( key: String ): Future[ Boolean ]

  /** Retrieves all keys matching the given pattern. This method invokes KEYS command
    *
    * '''Warning:''' complexity is O(n) where n are all keys in the database
    *
    * @param pattern valid KEYS pattern with wildcards
    * @return list of matching keys
    */
  def matching( pattern: String ): Future[ Set[ String ] ]

  /** Set a value into the cache. Expiration time in seconds (0 second means eternity).
    *
    * @param key cache storage key
    * @param value value to store
    * @param expiration record duration in seconds
    * @return promise
    */
  def set( key: String, value: Any, expiration: Duration = Duration.Inf ): Future[ Unit ]

  /** refreshes expiration time on a given key, useful, e.g., when we want to refresh session duration
    *
    * @param key cache storage key
    * @param expiration new expiration in seconds
    * @return promise
    */
  def expire( key: String, expiration: Duration ): Future[ Unit ]

  /** Removes all keys in arguments. The other remove methods are for syntax sugar
    *
    * @param keys cache storage keys
    * @return promise
    */
  def remove( keys: String* ): Future[ Unit ]

  /** Remove all keys in cache
    *
    * @return promise
    */
  def invalidate( ): Future[ Unit ]

  /** Sends PING command to REDIS and expects PONG in return
    *
    * @return promise
    */
  def ping( ): Future[ Unit ]

  /** Starts the connector, tests the connection
    *
    * @return promise
    */
  def start( ): Future[ Unit ]

  /** Stops the connector
    *
    * @return promise
    */
  def stop( ): Future[ Unit ]
  
}
