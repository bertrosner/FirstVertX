import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.Map;

public class FirstWebVerticle extends AbstractVerticle 
{
    private Map<String, JsonObject> m_products = new HashMap<>();

    @Override
    public void start() {

        setUpInitialData();

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.get("/").handler(this::handleRoot);
        router.get("/m_products/:productID").handler(this::handleGetProduct);
        router.put("/m_products/:productID").handler(this::handleAddProduct);
        router.delete("/m_products/:productID").handler(this::handleDeleteProduct);
        router.post("/m_products/:productID").handler(this::handlePostProduct);
        router.get("/m_products").handler(this::handleListProducts);

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }

    private void handleRoot(RoutingContext a_routingContext)
    {
        HttpServerResponse response = a_routingContext.response();
        response.end("<h1>Hello from my first Vert.x 3 application</h1>");
    }

    private void handleGetProduct(RoutingContext routingContext)
    {
        String productID = routingContext.request().getParam("productID");
        HttpServerResponse response = routingContext.response();
        if (productID == null)
        {
            sendError(400, response);
        }
        else
        {
            JsonObject product = m_products.get(productID);
            if (product == null) {
                sendError(404, response);
            } else {
                response.putHeader("content-type", "application/json").end(product.encodePrettily());
            }
        }
    }

    private void handleAddProduct(RoutingContext a_routingContext) 
    {
        String productID = a_routingContext.request().getParam("productID");
        HttpServerResponse response = a_routingContext.response();
        if (productID == null) 
        {
            sendError(400, response);
        } 
        else 
        {
            JsonObject product = a_routingContext.getBodyAsJson();
            if (product == null) {
                sendError(400, response);
            } else {
                m_products.put(productID, product);
                response.end();
            }
        }
    }

    private void handleDeleteProduct(RoutingContext a_routingContext)
    {
        String productID = a_routingContext.request().getParam("productID");
        HttpServerResponse response = a_routingContext.response();
        if (productID == null) {
            sendError(400, response);
        } else {
            JsonObject product = a_routingContext.getBodyAsJson();
            if (product == null) {
                sendError(400, response);
            } else {
                m_products.put(productID, product);
                response.end();
            }
        }
    }

    private void handlePostProduct(RoutingContext a_routingContext)
    {
        String productID = a_routingContext.request().getParam("productID");
        HttpServerResponse response = a_routingContext.response();
        if (productID == null) {
            sendError(400, response);
        } else {
            JsonObject product = a_routingContext.getBodyAsJson();
            if (product == null) {
                sendError(400, response);
            } else {
                m_products.put(productID, product);
                response.end();
            }
        }
    }

    private void handleListProducts(RoutingContext a_routingContext)
    {
        JsonArray arr = getM_products();
        a_routingContext.response().putHeader("content-type", "application/json").end(arr.encodePrettily());
    }

    public JsonArray getM_products()
    {
        JsonArray arr = new JsonArray();
        m_products.forEach((k, v) -> arr.add(v));
        return arr;
    }

    private void sendError(int statusCode, HttpServerResponse response) {
        response.setStatusCode(statusCode).end();
    }

    private void setUpInitialData()
    {
        addProduct(new JsonObject().put("id", "prod3568").put("name", "Egg Whisk").put("price", 3.99).put("weight", 150));
        addProduct(new JsonObject().put("id", "prod7340").put("name", "Tea Cosy").put("price", 5.99).put("weight", 100));
        addProduct(new JsonObject().put("id", "prod8643").put("name", "Spatula").put("price", 1.00).put("weight", 80));
    }

    private void addProduct(JsonObject product)
    {
        m_products.put(product.getString("id"), product);
    }
}
