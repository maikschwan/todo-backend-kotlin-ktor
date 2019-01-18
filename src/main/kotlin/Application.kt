import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.jackson.jackson
import io.ktor.request.host
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.util.*

fun main(args: Array<String>) {
    val port = System.getenv("PORT")?.toInt() ?: 8080
    val server = embeddedServer(Netty, port = port) {
        install(ContentNegotiation) {
            jackson {
                enable(SerializationFeature.INDENT_OUTPUT)
            }
        }

        install(CORS) {
            method(HttpMethod.Delete)
            method(HttpMethod.Patch)
            header(HttpHeaders.ContentType)
            anyHost()
        }

        routing {
            route("/") {
                get {
                    call.respond(Todos.getAll())
                }
                get("{id}") {
                    var url = urlFor(call)
                    call.respond(Todos.get(url))
                }
                post {
                    val item = call.receive<Item>()
                    item.url = urlFor(call);

                    call.respond(Todos.save(item.url!!, item))
                }
                patch("{id}") {
                    val newItem = call.receive<PatchItem>()
                    var url = urlFor(call)
                    call.respond(Todos.update(url, newItem))
                }
                delete("{id}") {
                    var url = urlFor(call)
                    call.respond(Todos.delete(url)!!)
                }
                delete {
                    call.respond(Todos.deleteAll())
                }
            }
        }
    }
    server.start(wait = true)
}

fun urlFor(call: ApplicationCall): String {
    val host = call.request.host()
    val id = call.parameters["id"] ?: UUID.randomUUID().toString()
    return "https://${host}/${id}"
}