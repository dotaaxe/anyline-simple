package org.anyline.simiple;

import org.anyline.pdf.util.PDFUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;


@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
public class PdfApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

		return application.sources(PdfApplication.class);
	}

	public static void main(String[] args) throws Exception {
		File file = new File("");
		String text = PDFUtil.read(file);

	}

}
