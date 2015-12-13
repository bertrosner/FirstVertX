import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class FirstWebVerticleTest
{
    private Vertx m_vertx;
    private Async m_async;

    @Before
    public void setUp(TestContext context)
    {
        m_vertx = Vertx.vertx();
        m_vertx.deployVerticle(FirstWebVerticle.class.getName(), context.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext context)
    {
        m_vertx.close(context.asyncAssertSuccess());
    }

    final int NUMBER_TEST = 3;
    int m_counterComplete = 0;
    private void asyncComplete()
    {
        if (m_counterComplete == NUMBER_TEST)
            m_async.complete();
    }

    @Test
    public void testMyApplication(TestContext a_context)
    {
        m_async = a_context.async();

        HttpClient httpClient = m_vertx.createHttpClient();

        m_vertx.createHttpClient().getNow(8080, "localhost", "/",
                getHttpClientResponseHandler("get root"));

        m_vertx.createHttpClient().getNow(8080, "localhost", "/products",
                getHttpClientResponseHandler("get products"));

        m_vertx.createHttpClient().getNow(8080, "localhost", "/products/prod3568",
                getHttpClientResponseHandler("get prod3568"));
    }

    private Handler<HttpClientResponse> getHttpClientResponseHandler(String a_call)
    {
        return response ->
        {
            response.handler(body ->
            {
                System.out.println("####### " + a_call);
                System.out.println(body.toString());
                asyncComplete();
            });
        };
    }
}