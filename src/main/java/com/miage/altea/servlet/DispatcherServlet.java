package com.miage.altea.servlet;

import com.miage.altea.annotation.RequestMapping;
import com.miage.altea.controller.Controller;
import com.miage.altea.controller.HelloController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/*", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private Map<String, Method> uriMappings = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("Getting request for " + req.getRequestURI());
        final String uri = req.getRequestURI();
        final Method method = getMappingForUri(uri);
        Object parameters = req.getParameterMap();

        if(method == null){
            resp.sendError(404, "no mapping found for request uri " + req.getRequestURI());
            return;
        }
        try{
            Object result = new Object();
            Object controller = method.getDeclaringClass().newInstance();
            if(((Map) parameters).isEmpty()){
                result = method.invoke(controller);
            }else{
             result = method.invoke(controller,parameters);
            }
            resp.setStatus(200);

            resp.getWriter().print(result.toString());
        }catch (Exception e){
            resp.sendError(500, "exception when calling method someThrowingMethod : some exception message");
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // on enregistre notre controller au démarrage de la servlet
        this.registerController(HelloController.class);
    }

    protected void registerController(Class controllerClass){
        System.out.println("Analysing class " + controllerClass.getName());
        if(controllerClass.getDeclaredAnnotation(Controller.class) == null){
            throw new IllegalArgumentException();
        }
        for(Method method : controllerClass.getDeclaredMethods()){
            registerMethod(method);
        }
    }

    protected void registerMethod(Method method) {
        System.out.println("Registering method " + method.getName());

        if(method.getReturnType() == void.class){
            return;
        }

        RequestMapping annotation = method.getDeclaredAnnotation(RequestMapping.class);
        if(annotation !=null){
            final String uri = annotation.uri();
            if(uri != null){
                this.getMappings().put(uri,method);
            }
        }
    }

    protected Map<String, Method> getMappings(){
        return this.uriMappings;
    }

    protected Method getMappingForUri(String uri){
        return this.uriMappings.get(uri);
    }
}
