package xyz.itihub.sercurity.server.auth;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;
import java.util.List;

/**
 * 客户端凭证缓存配置
 */
@Configuration
@ConditionalOnProperty(prefix = "security.oauth2.client", name = "store-type", havingValue = "redis")
public class CacheClientDetailsService extends JdbcClientDetailsService {

    private final String OAUTH_CLIENT_KEY = "oauth_client";

    private RedisTemplate<String, ClientDetails> redisTemplate;

    public CacheClientDetailsService(DataSource dataSource, RedisTemplate redisTemplate) {
        super(dataSource);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        ClientDetails clientDetails = (ClientDetails) redisTemplate.opsForHash().get(OAUTH_CLIENT_KEY, clientId);

        if (null == clientDetails) {
            clientDetails = super.loadClientByClientId(clientId);
            redisTemplate.opsForHash().put(OAUTH_CLIENT_KEY, clientId, clientDetails);
        }
        return clientDetails;
    }

    @Override
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
        super.addClientDetails(clientDetails);
        redisTemplate.opsForHash().put(OAUTH_CLIENT_KEY, clientDetails.getClientId(), clientDetails);
    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
        redisTemplate.opsForHash().delete(OAUTH_CLIENT_KEY, clientDetails.getClientId());
        super.updateClientDetails(clientDetails);
    }

    @Override
    public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
        redisTemplate.opsForHash().delete(OAUTH_CLIENT_KEY, clientId);
        super.updateClientSecret(clientId, secret);
    }

    @Override
    public void removeClientDetails(String clientId) throws NoSuchClientException {
        redisTemplate.opsForHash().delete(OAUTH_CLIENT_KEY, clientId);
        super.removeClientDetails(clientId);
    }

    @Override
    public List<ClientDetails> listClientDetails() {
        return super.listClientDetails();
    }
}
