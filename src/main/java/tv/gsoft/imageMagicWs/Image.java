package tv.gsoft.imageMagicWs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.im4java.core.Info;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Image {
	
	private String filename;
	private Info info;
		
	public Image(){}
	
	public Image(String filename) {
		this.filename = filename;
	}
	
	public Image(String filename, Info info) {
		this.filename = filename;
		this.info = info;
	}

	public String getFilename() {
		return filename;
	}
	
	public Info getInfo() {
		return info;
	}
	
}
