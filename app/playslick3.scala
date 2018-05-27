import java.util.concurrent.Executors

import scala.concurrent.ExecutionContext

package object playslick3 {
  val appExecutionContext = ExecutionContext.fromExecutorService( Executors.newFixedThreadPool( 15 ) )
}
