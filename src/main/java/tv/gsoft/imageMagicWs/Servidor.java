package tv.gsoft.imageMagicWs;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class Servidor {

	public static void main(String[] args) throws IOException {
		HttpServer server = startServer();
		System.out.println("Servidor rodando");
		System.in.read();
		stopServer(server);
	}

	public static void stopServer(HttpServer server) {
		server.shutdownNow();
	}

	public static HttpServer startServer() {
		ResourceConfig config = new ResourceConfig()
				.packages("tv.gsoft.imageMagicWs", "org.glassfish.jersey.examples.multipart")
				.register(MultiPartFeature.class);
		URI uri = URI.create("http://localhost:8080/");
		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, config);
		return server;
	}

}
