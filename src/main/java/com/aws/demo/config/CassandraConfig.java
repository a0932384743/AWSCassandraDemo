package com.aws.demo.config;

import com.datastax.oss.driver.api.core.CqlSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.config.SessionBuilderConfigurer;
import org.springframework.data.cassandra.core.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

@Slf4j
@Configuration
@AutoConfiguration
@ConditionalOnClass({CqlSession.class})
@EnableConfigurationProperties({CassandraProperties.class})
@EnableCassandraRepositories(
        basePackages = "com.aws.demo.repository")
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keyspace;

    @Value("${spring.data.cassandra.contact-points}")
    private String contactPoints;

    @Value("${spring.data.cassandra.port}")
    private int port;

    @Value("${spring.data.cassandra.username}")
    private String username;

    @Value("${spring.data.cassandra.password}")
    private String password;

    @Value("${ssl.trustStore.trustStoreLocation}")
    private String trustStoreLocation;

    @Value("${ssl.trustStore.trustStorePassword}")
    private String trustStorePassword;


    @Override
    protected String getKeyspaceName() {
        return keyspace;
    }

    @Bean
    @Override
    public CqlSessionFactoryBean cassandraSession() {
        final CqlSessionFactoryBean cqlSessionFactoryBean = new CqlSessionFactoryBean();
        cqlSessionFactoryBean.setContactPoints(contactPoints);
        cqlSessionFactoryBean.setKeyspaceName(keyspace);
        cqlSessionFactoryBean.setPort(port);
        cqlSessionFactoryBean.setUsername(username);
        cqlSessionFactoryBean.setPassword(password);
        cqlSessionFactoryBean.setSessionBuilderConfigurer(getSessionBuilderConfigurer());
        return cqlSessionFactoryBean;
    }

    @Override
    protected SessionBuilderConfigurer getSessionBuilderConfigurer() {
        return (cqlSessionBuilder) -> {
            SSLContext sslContext = createSslContext();
            return cqlSessionBuilder.withSslContext(sslContext);
        };
    }


    private SSLContext createSslContext() {
        SSLContext sslcontext = null;
        try {
            final TrustManager[] trustManagers = getTrustManagers();
            sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, trustManagers, new SecureRandom());
            log.info("Got SSL context");
        } catch (Exception e) {
            log.error("Exception occured", e);
        }

        return sslcontext;
    }

    private TrustManager[] getTrustManagers() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {

        final InputStream trustStoreLocationInputStream = CassandraConfig.class.getResourceAsStream(trustStoreLocation);
        final KeyStore keystore = KeyStore.getInstance("JKS");
        final char[] trustStorePasswordCharArray = trustStorePassword.toCharArray();

        keystore.load(trustStoreLocationInputStream, trustStorePasswordCharArray);

        final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keystore);

        final TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

        return trustManagers;
    }

}
