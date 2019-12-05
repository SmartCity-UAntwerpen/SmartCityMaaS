package be.uantwerpen.controller

import be.uantwerpen.repositories.UserRepository
import be.uantwerpen.services.PassengerService
import be.uantwerpen.services.UserService
import org.apache.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.concurrent.atomic.AtomicLong

@Controller
@RequestMapping("/mapbuilder")
class MapBuilderController {

    @Autowired
    private val userRepository: UserRepository? = null
    @Autowired
    private val userService: UserService? = null
    @Autowired
    private val passengerService: PassengerService? = null

    private val logger = LogManager.getLogger(MapBuilderController::class.java)

    private val counter = AtomicLong()

    @GetMapping("/map")
    fun init(model: Model, @RequestParam(value = "name", defaultValue = "World") name: String) : String {
        val loginUser = userService?.principalUser
        model.addAttribute("currentUser", loginUser)
        return "mapbuilder"
    }
}

data class Greeting(val id: Long, val content: String) {

}