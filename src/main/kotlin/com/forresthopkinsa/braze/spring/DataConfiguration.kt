package com.forresthopkinsa.braze.spring

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.Database
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(basePackages = ["com.forresthopkinsa.braze.data"])
class DataConfiguration(@Value("\${braze.persist}") private val persist: Boolean) {

    private val url =
        if (persist) "jdbc:h2:./braze;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;" else "jdbc:h2:mem:testdb"

    @Bean
    fun dataSource(): DataSource = DataSourceBuilder.create()
        .username("sa")
        .url(url)
        .build()

    @Bean
    fun jpaVendorAdapter(): JpaVendorAdapter = HibernateJpaVendorAdapter().apply {
        setDatabase(Database.H2)
        setGenerateDdl(true)
    }

    @Bean
    fun entityManagerFactory(ds: DataSource, adapter: JpaVendorAdapter): LocalContainerEntityManagerFactoryBean =
        LocalContainerEntityManagerFactoryBean().apply {
            dataSource = ds
            jpaVendorAdapter = adapter
            setPackagesToScan("com.forresthopkinsa.braze.model")
        }

    @Bean
    fun transactionManager(emf: EntityManagerFactory): JpaTransactionManager = JpaTransactionManager(emf)

}