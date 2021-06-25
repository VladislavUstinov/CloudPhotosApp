package com.laserdiffraction01.laserdiffraction01.configuration;

import com.laserdiffraction01.laserdiffraction01.domain.Role;
import com.laserdiffraction01.laserdiffraction01.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                //the more specific rules should go first
                .authorizeRequests()
                //Доступ только для не зарегистрированных пользователей
                    .antMatchers("/user/registration").not().fullyAuthenticated()
                    .antMatchers("/test", "/testpostBooleanListSecond", "/testpostBooleanList", "/testRestMapping", "/testpost", "/testGetJson", "/testRequestParamName", "/testPathVarId/**").not().fullyAuthenticated()

                //Доступ только для зарегистрированных пользователей
                    .antMatchers("/admin/showAll").hasRole(Role.ADMIN_ROLE_STRING)
                    .antMatchers("/news", "/photos", "/account").hasAnyRole(Role.ADMIN_ROLE_STRING, Role.USER_ROLE_STRING)
                //доступ для всех
                    .antMatchers("/", "/index").permitAll()
                //доступ для всех к bootstrap
                    .antMatchers("/webjars/bootstrap/3.3.7-1/css/bootstrap.min.css",
                        "/webjars/jquery/1.11.1/jquery.min.js",
                        "/webjars/bootstrap/3.3.7-1/js/bootstrap.min.js").permitAll()
                //доступ к защищенной базе данных todo:прописать h2-console мои логин и пароль
                    //.antMatchers("/h2-console/**", "/h2-console").permitAll()
                    .anyRequest().authenticated()// - it disables bootstrap for non-authorozied users!
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/photos", true)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll();
    }


}
/*
//from habr.com/482552
        httpSecurity
                .csrf()
                .disable()
                .authorizeRequests()
                //Доступ только для не зарегистрированных пользователей
                .antMatchers("/user/registration").not().fullyAuthenticated()
                //Доступ только для пользователей с ролью Администратор
                .antMatchers("/admin/**").hasRole(Role.ADMIN_ROLE_STRING)
                .antMatchers("/news").hasRole(Role.USER_ROLE_STRING)
                //Доступ разрешен всем пользователям
                .antMatchers("/", "/resources/**").permitAll()
                //Все остальные страницы требуют аутентификации
                .anyRequest().authenticated()
                .and()
                //Настройка для входа в систему
                .formLogin()
                .loginPage("/login.html")
                //Перенарпавление на главную страницу после успешного входа
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .logoutSuccessUrl("/"); */
                /*.authorizeRequests()
                //Доступ только для не зарегистрированных пользователей
                    .antMatchers("/user/registration").not().fullyAuthenticated()
                //Доступ только для пользователей с ролью Администратор
                    .antMatchers("/admin/**").hasRole(Role.ADMIN_ROLE_STRING)
                    .antMatchers("/news").hasRole(Role.USER_ROLE_STRING)
                //Доступ разрешен всем пользователям
                    .antMatchers("/", "/resources/**").permitAll()
                //Все остальные страницы требуют аутентификации
                    .anyRequest().authenticated()
                .and()*/

//from https://www.baeldung.com/spring-security-login
        /*
            http
      .csrf().disable()
      .authorizeRequests()
      .antMatchers("/admin/**").hasRole("ADMIN")
      .antMatchers("/anonymous*").anonymous()
      .antMatchers("/login*").permitAll()
      .anyRequest().authenticated()

      .and()
      .formLogin()
      .loginPage("/login.html")
      .loginProcessingUrl("/perform_login")
      .defaultSuccessUrl("/homepage.html", true)
      .failureUrl("/login.html?error=true")
      .failureHandler(authenticationFailureHandler())
      .and()
      .logout()
      .logoutUrl("/perform_logout")
      .deleteCookies("JSESSIONID")
      .logoutSuccessHandler(logoutSuccessHandler());
         */

