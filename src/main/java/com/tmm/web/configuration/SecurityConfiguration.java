package com.tmm.web.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Spring doesn't yet support pure java configuration of spring security
 * so this will just have to import the old fashioned xml file.
 * 
 * @author rob
 *
 */
@Configuration
@ImportResource("classpath:META-INF/spring/security.xml")
public class SecurityConfiguration {}