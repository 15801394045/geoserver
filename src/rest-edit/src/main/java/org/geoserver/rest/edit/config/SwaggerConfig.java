package org.geoserver.rest.edit.config;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author ily
 * @date 04 23, 2020
 * @since 1.0.0
 */
/*@EnableWebMvc
@Configuration
@EnableSwagger2*/
public class SwaggerConfig {
    // @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.geoserver.rest.edit"))
                .build()
                .apiInfo(apiInfo())
                .ignoredParameterTypes(
                        org.geotools.styling.Stroke.class,
                        freemarker.template.Template.class,
                        org.geotools.ows.wms.Layer.class,
                        org.geotools.ows.wmts.model.WMTSLayer.class);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Hgisserver Rest API docs")
                .description("RESTful API description")
                .version("3.6.4")
                .build();
    }
}
