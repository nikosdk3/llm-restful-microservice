import com.typesafe.config.{Config, ConfigFactory}

object Config {
  val config: Config = ConfigFactory.load()

  val serverHost: String = config.getString("server.host")
  val serverPort: Int = config.getInt("server.port")
  
  val endpoint: String = config.getString("llm.endpoint")
}
