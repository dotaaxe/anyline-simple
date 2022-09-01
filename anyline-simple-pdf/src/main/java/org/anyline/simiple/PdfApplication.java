package org.anyline.simiple;

import org.anyline.pdf.util.PDFUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;


@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
public class PdfApplication  {


	public static void main(String[] args) throws Exception {
		File file = new File("D:\\a.pdf");
		String text = PDFUtil.read(file);
		System.out.println(text);
	}

}
