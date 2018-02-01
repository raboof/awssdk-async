import java.nio.ByteBuffer

import scala.compat.java8.FutureConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

import org.reactivestreams.{ Subscriber, Subscription }

import software.amazon.awssdk.core.async.AsyncRequestProvider
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model.PutObjectRequest

object Main extends App {
  // Use default aws configuration, for example
  // AWS_ACCESS_KEY_ID, AWS_REGION and AWS_SECRET_ACCESS_KEY
  val lc = S3AsyncClient.create()
  val b = "abc123123123"
  for { obj ← 1 to 200 } {
    if (obj % 100 == 0) println(obj)
    val p = PutObjectRequest.builder().bucket(b).key("test-Obj" + obj.toString()).build()
    val response = lc.putObject(p, AsyncRequestProvider.fromString("B" * 10000)).toScala
    response.onComplete {
      case Success(_) ⇒ //OK
      case Failure(f) ⇒ 
        println(s"NOK: ${f.getMessage}")
        f.printStackTrace
    }
  }
  Thread.sleep(10000)
}
