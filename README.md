# easy-mvc
轻量级web框架


### web.xml中增加Listener和Servlet
``` java ```
<listener>
    <listener-class>easy.framework.mvc.listener.ContainerListener</listener-class>
</listener>
<servlet>
    <servlet-name>easyMvc</servlet-name>
    <servlet-class>easy.framework.mvc.dispatcher.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath*:easy-mvc.properties</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
    <async-supported>true</async-supported>
</servlet>
<servlet-mapping>
    <servlet-name>easyMvc</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
``` java ```
