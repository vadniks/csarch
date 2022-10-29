package a

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.stereotype.Controller
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import java.lang.System.currentTimeMillis

const val prefix = "/webs"
const val endpoint = "/in"

@SpringBootApplication class App

@Configuration @EnableWebSocketMessageBroker
class WebSocketConfigurer : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(registry: MessageBrokerRegistry)
    { registry.enableSimpleBroker(prefix); registry.setApplicationDestinationPrefixes(prefix) }

    override fun registerStompEndpoints(registry: StompEndpointRegistry)
    { registry.apply { addEndpoint(endpoint); addEndpoint(endpoint).withSockJS() } }
}

data class StringsPair(val name: String, val message: String)
data class StringsTriple(val name: String, val message: String, val time: String)

@Controller class ChatController {
    @MessageMapping(endpoint) @SendTo("$prefix/ot")
    fun onMessageReceive(messageBundle: StringsPair) =
        StringsTriple(messageBundle.name, messageBundle.message, currentTimeMillis().toString())
}

@Configuration class WebConfigurer : WebMvcConfigurer {

    override fun addViewControllers(registry: ViewControllerRegistry)
    { registry.addViewController("/") }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) { registry
        .addResourceHandler("/static/**")
        .addResourceLocations("classpath:/static/") }
}

fun main(vararg args: String) { runApplication<App>(*args) }
