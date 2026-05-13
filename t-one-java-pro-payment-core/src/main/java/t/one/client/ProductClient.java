package t.one.client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import t.one.dto.ProductResponse;

import java.util.List;

@Component
public class ProductClient {

    private final RestClient restClient;

    @Autowired
    public ProductClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<ProductResponse> getProductsByClientId(Long userId) {
        return restClient.get()
                .uri("/api/products/{userId}", userId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<ProductResponse>>() {});
    }
}
