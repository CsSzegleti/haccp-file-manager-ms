package io.c0dr.filemanager.controller.amqp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@Configuration("requestQueueNamesConfig")
@ConfigurationProperties("amqp")
@Data
@Validated
public class RequestQueueNamesConfig {
    private final ApplicationContext context;

     @Data
    public static class Queue {
        @NotBlank
        private String request;
        @NotBlank
        private String response;
    }

    @Data
    public static class Module {
        @NotBlank
        private String name;
        @NotNull
        @NotEmpty
        private List<Queue> queues;
    }

    @NotNull
    @NotEmpty
    private List<Module> modules;


    public String[] getRequestQueueNames() {
        return getRequestQueueNames(context.getId());
    }
    public String[] getRequestQueueNames(String moduleName) {
        return modules.stream().filter(m -> m.getName().equals(moduleName))
                .map(m -> m.getQueues()).flatMap(Collection::stream).map(queue -> queue.getRequest()).toArray(String[]::new);
    }

    public String getResponseQueueName(String requestQueueName) {
        return getResponseQueueName(context.getId(), requestQueueName);

    }

    public String getResponseQueueName(String moduleName, String requestQueueName) {
        return modules.stream().filter(m -> m.getName().equals(moduleName))
                .map(m -> m.getQueues()).flatMap(Collection::stream)
                .filter(queue -> queue.getRequest().equals(requestQueueName))
                .findAny().map( queue -> queue.getResponse()).orElse(null);

    }

    public String getResponseQueueNameFromAllModules(String requestQueueName) {
        return modules.stream()
                .map(m -> m.getQueues()).flatMap(Collection::stream)
                .filter(queue -> queue.getRequest().equals(requestQueueName))
                .findAny().map( queue -> queue.getResponse()).orElse(null);
    }

}
