package com.benope.verbose.spoon.web

import com.benope.verbose.spoon.web.user.dto.UserResponse
import com.benope.verbose.spoon.web.user.service.UserManageService
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.validation.constraints.NotBlank

@Configuration
class ViewConfig : WebMvcConfigurer {
    override fun addViewControllers(registry: ViewControllerRegistry) {
        // main page
        registry.addViewController("/").setViewName("index")

        // dashboard
        registry.addViewController("/dashboard").setViewName("index")

        // 사용자 관리
        registry.addViewController("/user/list").setViewName("/user/userList")
        registry.addViewController("/user/create").setViewName("/user/userCreate")

        // 휴가 관리
        registry.addViewController("/time-off/create").setViewName("/vacation/timeOffCreate")
        registry.addViewController("/leave-request/create").setViewName("/vacation/leaveRequestCreate")
        registry.addViewController("/leave-request/detail").setViewName("/vacation/leaveRequestDetail")
        registry.addViewController("/leave-request/approval").setViewName("/vacation/leaveRequestApproval")

    }
}

@Controller
@RequestMapping("/user")
class UserController(
    private val userManageService: UserManageService
) {

    @GetMapping("/profile")
    @PreAuthorize("hasRole('ADMIN') or  #username == authentication.principal.username")
    fun userUpdateView(@RequestParam @NotBlank username: String, mav: ModelAndView): ModelAndView {
        mav.addObject("user", UserResponse.fromUser(userManageService.findUser(username)))
        mav.viewName = "/user/userProfile"
        return mav
    }

    @GetMapping("/change-password")
    @PreAuthorize("hasRole('ADMIN') or  #username == authentication.principal.username")
    fun changePasswordView(@RequestParam @NotBlank username: String, mav: ModelAndView): ModelAndView {
        mav.addObject("user", UserResponse.fromUser(userManageService.findUser(username)))
        mav.viewName = "/user/changePassword"
        return mav
    }

}