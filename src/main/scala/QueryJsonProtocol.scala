import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

// This trait defines how to serialize and deserialize JSON for the LLM API requests and responses.
// It uses Spray JSON (a library for handling JSON in Scala) to automatically convert between
// case classes and JSON objects. It also integrates with Akka HTTP to allow marshalling
// and unmarshalling of JSON entities in HTTP requests and responses.

trait QueryJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  
  import spray.json.DefaultJsonProtocol.listFormat

}
