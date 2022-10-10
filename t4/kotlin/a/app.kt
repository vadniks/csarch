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

const val pf = "/webs"
const val en = "/in"

@SpringBootApplication class app

@Configuration @EnableWebSocketMessageBroker class wsc : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(a: MessageBrokerRegistry)
    { a.enableSimpleBroker(pf); a.setApplicationDestinationPrefixes(pf) }

    override fun registerStompEndpoints(a: StompEndpointRegistry)
    { a.apply { addEndpoint(en); addEndpoint(en).withSockJS() } }
}

data class pss(val a: String, val b: String)
data class tss(val a: String, val b: String, val c: String)

@Controller class chc { @MessageMapping(en) @SendTo("$pf/ot")
fun a(a: pss) = tss(a.a, a.b, currentTimeMillis().toString()) }

@Configuration class wmc : WebMvcConfigurer {

    override fun addViewControllers(a: ViewControllerRegistry) { a.addViewController("/") }

    override fun addResourceHandlers(a: ResourceHandlerRegistry)
    { a.addResourceHandler("/static/**").addResourceLocations("classpath:/static/") }
}

fun main(vararg args: String) { runApplication<app>(*args) }
