package ru.otus.app;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bson.types.ObjectId;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.gson.JsonElementValueReader;
import org.modelmapper.spi.MappingContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.List;

@EnableWebMvc
@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = "ru.otus")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    protected WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
                Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
                builder.serializerByType(ObjectId.class, new JsonSerializer<ObjectId>() {
                    @Override
                    public void serialize(ObjectId value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        gen.writeString(value.toString());
                    }
                });

                converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
            }
        };
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ModelMapper modelMapper() {
        final ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().addValueReader(new JsonElementValueReader());
        modelMapper.addConverter(new Converter<String, List<String>>() {
            @Override
            public List<String> convert(MappingContext<String, List<String>> context) {
                return new Gson().fromJson(context.getSource(), new TypeToken<List<String>>() {
                }.getType());
            }
        });
        return modelMapper;
    }
}
