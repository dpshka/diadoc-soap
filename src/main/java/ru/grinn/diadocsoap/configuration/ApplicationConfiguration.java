package ru.grinn.diadocsoap.configuration;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class ApplicationConfiguration extends WsConfigurerAdapter {
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        var servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "edo")
    public DefaultWsdl11Definition defaultWsdl11Definition() {
        var wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("EdoPortType");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://www.grinn-corp.ru/gestori/edo");
        wsdl11Definition.setSchema(edoSchema());
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema edoSchema() {
        return new SimpleXsdSchema(new ClassPathResource("edo.xsd"));
    }

}
