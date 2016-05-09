package tv.gsoft.imageMagicWs;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.glassfish.grizzly.http.server.HttpServer;
import org.im4java.core.Info;
import org.im4java.core.InfoException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.response.Response;

public class ImageResourceTest {

	private static HttpServer server;

	@BeforeClass
	public static void before() {
		server = Servidor.startServer();
	}

	@AfterClass
	public static void after() {
		Servidor.stopServer(server);
	}

	@Test
	public void testListagemDasImagens() {
		File folder = new File("imgs/");
		int imagesCount = folder.listFiles().length;
		when()
			.get("/images")
		.then()
			.statusCode(200)
			.body("size()", equalTo(imagesCount));
	}
	
	@Test
	public void testUploadDeImagem() throws IOException {
		File file = new File("imgs/img1.jpg");
		byte[] bytes = Files.readAllBytes(file.toPath());
		file.delete();
		
		File folder = new File("imgs/");
		int imagesCountBefore = folder.listFiles().length;
		
		given()
			.multiPart("file", "img1.jpg", bytes)
		.expect()
			.statusCode(200)
		.when()
			.post("/images");
		
		int imagesCountAfter = folder.listFiles().length;
		assertEquals(imagesCountAfter , imagesCountBefore+1);
	}
	
	@Test
	public void testUploadDeImagemErrorQuandoNenhumaImagemEhEnviada() throws IOException {
		
		given()
			.multiPart("file", "", new byte[0])
		.expect()
			.statusCode(400)
		.when()
			.post("/images");
	}
	
	@Test
	public void testInformacoesDaImagem() {
		when()
			.get("/images/info/img1.jpg")
		.then()
			.statusCode(200)
			.body("info.imageWidth", equalTo(1920));
	}
	
	@Test
	public void testInformacoesDaImagemErrorImagemNaoExiste() throws IOException {
		
		when()
			.get("/images/info/img000.jpg") //deve ser um filename que nao existe no diretorio
		.then()
			.statusCode(404);
	}
	
	@Test
	public void testRedimensionamentoDeImagem() throws InfoException {
		Response response = when()
			.get("/images/resize/300/300/img1.jpg")
		.then()
			.statusCode(200)
			.contentType("image/png")
		.extract().response();

		InputStream is = new ByteArrayInputStream(response.asByteArray());
		Info info = new Info("-", is);
		assertEquals(300, info.getImageWidth());
		assertEquals(199, info.getImageHeight());
	}
	
	@Test
	public void testRedimensionamentoDeImagemErrorImagemNaoExiste() throws InfoException {
		when()
			.get("/images/resize/300/300/img000.jpg")
		.then()
			.statusCode(400);
	}
	
	@Test
	public void testRedimensionamentoDeImagemErrorDimensaoInvalida() throws InfoException {
		when()
			.get("/images/resize/300/-1/img000.jpg")
		.then()
			.statusCode(400);
	}

}
