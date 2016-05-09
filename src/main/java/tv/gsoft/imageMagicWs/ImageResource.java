package tv.gsoft.imageMagicWs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.Info;
import org.im4java.core.InfoException;
import org.im4java.process.Pipe;

@Path("images")
public class ImageResource {
	
	private static final String IMAGES_PATH = "imgs/";
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Image> list() {
		File folder = new File(IMAGES_PATH);
		List<Image> images = new ArrayList<Image>();
		for (final File fileEntry : folder.listFiles()) {
			if (!fileEntry.isDirectory()) {
				images.add(new Image(fileEntry.getName()));
			}
		}
		return images;
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		try {
			saveImage(uploadedInputStream, IMAGES_PATH + fileDetail.getFileName());
			return Response.ok().build();
		} catch (IOException e) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}

	@Path("info/{filename}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Image info(
			@PathParam("filename") String filename) {

		try {
			return new Image(filename, new Info(IMAGES_PATH+filename, true));
		} catch (InfoException e) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}
	
	@Path("resize/{width}/{height}/{filename}")
	@GET
	@Produces("image/png")
	public Response resize(
			@PathParam("filename") String filename, 
			@PathParam("width") int width,
			@PathParam("height") int height) {

		try {
			byte[] imageData = resizeImage(filename, width, height);
			return Response.ok(imageData).build();

		} catch (IOException | InterruptedException | IM4JavaException e) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}

	private byte[] resizeImage(String filename, int width, int height)
			throws IOException, InterruptedException, IM4JavaException {
		
		IMOperation op = new IMOperation();
		op.addImage(IMAGES_PATH + filename);
		op.resize(width, height);
		op.addImage("png:-");

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Pipe pipeOut = new Pipe(null, os);
		
		ConvertCmd cmd = new ConvertCmd();
		cmd.setOutputConsumer(pipeOut);
		cmd.run(op);
		byte[] imageData = os.toByteArray();
		os.close();
		return imageData;
	}

	private void saveImage(InputStream uploadedInputStream, String uploadedFileLocation) throws IOException {

		OutputStream out = null;
		int read = 0;
		byte[] bytes = new byte[1024];
		out = new FileOutputStream(new File(uploadedFileLocation));
		while ((read = uploadedInputStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
	}
	
	// Apenas para apresentação 
	
	@GET
	@Path("form")
	public Response form() throws IOException {
		String html = new String(Files.readAllBytes(Paths.get("form.html")));
		return Response.ok(html).build();
	}
}
