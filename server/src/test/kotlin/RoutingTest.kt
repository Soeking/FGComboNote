import io.kotest.core.spec.style.FunSpec
import io.kotest.assertions.ktor.*
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import net.soeki.fcn.module

class RoutingTest : FunSpec({
    test("check root") {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/").apply {
                response shouldHaveStatus HttpStatusCode.OK
            }
        }
    }

    test("get versions") {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/setting/versions").apply {
                response shouldHaveStatus HttpStatusCode.OK
            }
        }
    }
})
