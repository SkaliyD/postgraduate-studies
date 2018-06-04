package skaliy.web.server.postgraduatestudies.security


import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
//import org.springframework.security.web.csrf.CsrfFilter
//import org.springframework.security.web.csrf.CsrfTokenRepository
//import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository

import skaliy.web.server.postgraduatestudies.entities.enums.UserRole
import skaliy.web.server.postgraduatestudies.repositories.UsersRepository


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true
)
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var usersRepository: UsersRepository

    override fun configure(http: HttpSecurity?) {
        http!!
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/api/branches/post/**",
                        "/api/branches/put/**",
                        "/api/branches/delete/**",
                        "/api/contact-info/get/one**",
                        "/api/contact-info/get/all**",
                        "/api/contact-info/post/**",
                        "/api/contact-info/put/set-one**",
                        "/api/contact-info/delete/**",
                        "/api/degrees/post/**",
                        "/api/degrees/put/set-one**",
                        "/api/degrees/delete/**",
                        "/api/departments/post/**",
                        "/api/departments/put/set-one**",
                        "/api/departments/delete/**",
                        "/api/faculties/post/**",
                        "/api/faculties/put/**",
                        "/api/faculties/delete/**",
                        "/api/institutes/post/**",
                        "/api/institutes/put/**",
                        "/api/institutes/delete/**",
                        "/api/scientific-links/get/one**",
                        "/api/scientific-links/get/all**",
                        "/api/scientific-links/post/**",
                        "/api/scientific-links/put/set-one**",
                        "/api/scientific-links/delete/**",
                        "/api/sections/get/one**",
                        "/api/sections/get/all**",
                        "/api/sections/put/set-one**",
                        "/api/sections/delete/one**",
                        "/api/sections/delete/all**",
                        "/api/specialities/post/**",
                        "/api/specialities/put/**",
                        "/api/specialities/delete/**",
                        "/api/study-info/get/one**",
                        "/api/study-info/get/all**",
                        "/api/study-info/post/**",
                        "/api/study-info/put/set-one**",
                        "/api/study-info/delete/**",
                        "/api/tasks/get/one**",
                        "/api/tasks/get/all**",
                        "/api/tasks/put/set-one**",
                        "/api/tasks/delete/one**",
                        "/api/tasks/delete/all**",
                        "/api/users/get/one**",
                        "/api/users/get/all**",
                        "/api/users/post/**",
                        "/api/users/put/set-one**",
                        "/api/users/delete/**"
                ).hasRole(
                        UserRole.ADMIN.value
                )
                .antMatchers(
                        "/api/sections/get/my**",
                        "/api/sections/post/add-my**",
                        "/api/sections/put/set-my**",
                        "/api/sections/delete/my**",
                        "/api/study-info/get/my**",
                        "/api/study-info/put/set-my**",
                        "/api/tasks/get/my**",
                        "/api/tasks/post/add-my**",
                        "/api/tasks/put/set-my**",
                        "/api/tasks/delete/my**",
                        "/api/users/get/my-instructor**"
                ).hasAnyRole(
                        UserRole.GRADUATE_STUDENT.value,
                        UserRole.DOCTORAL_STUDENT.value
                )
                .antMatchers(
                        "/api/users/get/my-students**",
                        "/api/tasks/put/set-mark-instructor**"
                ).hasRole(
                        UserRole.INSTRUCTOR.value
                )
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .logout().permitAll()
//                .and()
//                .addFilterAfter(CsrfHeaderFilter(), CsrfFilter::class.java)
//                .csrf().csrfTokenRepository(csrfTokenRepository())
    }

//    private fun csrfTokenRepository(): CsrfTokenRepository {
//        val repository = HttpSessionCsrfTokenRepository()
//        repository.setHeaderName("X-XSRF-TOKEN")
//        return repository
//    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Autowired
    @Throws(Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        usersRepository.getAll(true)?.forEach { user ->
            auth
                    .inMemoryAuthentication()
                    .withUser(user.contactInfo.email)
                    .password(passwordEncoder().encode(usersRepository.getPassword(user.idUser)))
                    .roles(user.role.value)
        }
    }

}