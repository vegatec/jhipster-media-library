package net.vegatec.media_library;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.vegatec.media_library.config.AsyncSyncConfiguration;
import net.vegatec.media_library.config.EmbeddedElasticsearch;
import net.vegatec.media_library.config.EmbeddedKafka;
import net.vegatec.media_library.config.EmbeddedSQL;
import net.vegatec.media_library.config.JacksonConfiguration;
import net.vegatec.media_library.config.TestSecurityConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = { MediaLibraryApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class, TestSecurityConfiguration.class }
)
@EmbeddedElasticsearch
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
