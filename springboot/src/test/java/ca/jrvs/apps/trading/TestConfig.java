package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {"ca.jrvs.apps.trading.dao", "ca.jrvs.apps.trading.service"})
public class TestConfig {

    @Bean
    public MarketDataConfig marketDataConfig() {
        MarketDataConfig marketDataConfig = new MarketDataConfig();

        marketDataConfig.setHost("https://cloud.iexapis.com/v1/");
        marketDataConfig.setToken("sk_bf9c12a648c347dba28544a58c5ca09b");

        return marketDataConfig;
    }

    @Bean
    public DataSource dataSource() {
        System.out.println("Creating apacheDataSource");
        String jdbcUrl = "jdbc:postgresql://" +
                "localhost:5432/jrvstrading";
//                "jdbc:postgresql://" +
//                        "trading-psql-demo-local" + ":" +
//                        "5432" +
//                        "/" +
//                        "jrvstrading";
        String user = "postgres";
        String password = "password";
//                        System.getenv("PSQL_HOST") + ":" +
//                        System.getenv("PSQL_PORT") +
//                        "/" +
//                        System.getenv("PSQL_DB");
//        String user = System.getenv("PSQL_USER");
//        String password = System.getenv("PSQL_PASSWORD");

        //Never log your credentials/secrets. Use IDE debugger instead
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(jdbcUrl);
        basicDataSource.setUsername(user);
        basicDataSource.setPassword(password);
        return basicDataSource;
    }

    @Bean
    public HttpClientConnectionManager httpClientConnectionManager(){
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(50);
        cm.setDefaultMaxPerRoute(50);
        return cm;
    }
}
