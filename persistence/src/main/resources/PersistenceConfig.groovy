import com.tvsc.core.AppProfiles
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType

/**
 *
 * @author Taras Zubrei
 */
def properties = new Properties()
properties.load(new ClassPathResource('persistence.properties').inputStream)
beans {
    xmlns([context: 'http://www.springframework.org/schema/context', tx: 'http://www.springframework.org/schema/tx'])
    context.'component-scan'('base-package': 'com.tvsc.persistence')
    tx.'annotation-driven'()

    if (environment.activeProfiles.contains(AppProfiles.TEST)) {
        dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript(properties.getProperty('db.schema'))
                .addScript(properties.getProperty('db.data'))
                .build();

    } else {
        dataSource(DriverManagerDataSource) { bean ->
            bean.setUrl(properties.getProperty('db.prod.url'));
            bean.setUsername(properties.getProperty('db.prod.username'));
            bean.setPassword(properties.getProperty('db.prod.password'));
            bean.setDriverClassName(properties.getProperty('db.prod.driver'));
        }
    }
    transactionManager(DataSourceTransactionManager) {
        dataSource = dataSource
    }
    jdbcTemplate(JdbcTemplate) {
        dataSource = dataSource
    }

}