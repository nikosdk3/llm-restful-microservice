import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model.*
import akka.http.scaladsl.model.StatusCodes.*
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.unmarshalling.Unmarshal
import org.slf4j.LoggerFactory
import spray.json.*

import java.io.{File, IOException}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object LLMService extends Directives with QueryJsonProtocol {
  // Logger for tracking application logs
  private val logger = LoggerFactory.getLogger(getClass)

  // Define the route handling logic for the "/query" endpoint
  def route(implicit system: ActorSystem): Route =
    path("query") {
      post {
        // Extract the Authorization header for the Bearer token (API key)
        headerValueByName("Authorization") { authorizationHeader =>
          // Strip the 'Bearer ' prefix and retrieve the actual API key
          val apiKey = authorizationHeader.stripPrefix("Bearer ").trim
          entity(as[String]) { userMessage =>
            // Extract the body of the POST request, which contains the user's message
            val responseFuture: Future[String] = handleQuery(userMessage, apiKey)

            // Handle the Future response
            onComplete(responseFuture) {
              case Success(response) =>
                complete(HttpEntity(ContentTypes.`application/json`, response.toJson.prettyPrint))
              case Failure(exception) =>
                // Log error if the processing fails
                logger.error("Error processing LLM query", exception)
                complete(StatusCodes.InternalServerError, s"Error: ${exception.getMessage}")
            }
          }
        }
      }
    }

  /**
   * Handles the query to the LLM API.
   * This method sends a request to OpenAI's API and processes the response.
   *
   * @param userMessage the user input message (prompt).
   * @param apiKey the API key used for authentication.
   * @return a Future containing the LLM response.
   */
  private def handleQuery(userMessage: String, apiKey: String)(implicit system: ActorSystem): Future[String] = {
    import system.dispatcher
    val logger = LoggerFactory.getLogger("LLM")
    val apiUrl = Config.endpoint // OpenAI endpoint for completions

    // Format message for the request
    val userPrompt = s"User: $userMessage"

    // Construct JSON payload for API request
    val payload = JsObject(
      "model" -> JsString("gpt-3.5-turbo"), // Define the model to use
      "messages" -> JsArray(
        JsObject(
          "role" -> JsString("user"),
          "content" -> JsString(userPrompt)
        )
      ),
      "max_completion_tokens" -> JsNumber(20) // Define the max token count for the response
    )

    // Log the constructed payload for debugging
    logger.debug(s"Sending payload to API: ${payload.prettyPrint}")

    // Create HTTP Request
    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = apiUrl,
      entity = HttpEntity(ContentTypes.`application/json`, payload.prettyPrint)
    ).withHeaders(Authorization(OAuth2BearerToken(apiKey)))

    // Make the HTTP call and process response
    Http().singleRequest(request).flatMap { response =>
      response.status match {
        case StatusCodes.OK =>
          Unmarshal(response.entity).to[String].map { responseBody =>
            // Parse the response body to extract the generated completion text
            val jsonResponse = responseBody.parseJson.asJsObject
            val completionText = jsonResponse.fields("choices").convertTo[List[JsObject]].head.fields("message").convertTo[JsObject].fields("content").convertTo[String]

            logger.info("LLM response successfully processed")

            s"LLM: $completionText"
          }
        case _ => Unmarshal(response.entity).to[String].flatMap { entity =>
          val error = s"Api request failed with status code ${response.status} and entity $entity"
          logger.error(error)
          Future.failed(new IOException(error))
        }
      }
    }
  }
}
