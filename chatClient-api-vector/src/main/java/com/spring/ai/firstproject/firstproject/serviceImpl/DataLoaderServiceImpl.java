package com.spring.ai.firstproject.firstproject.serviceImpl;

import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import com.spring.ai.firstproject.firstproject.service.DataLoaderService;

@Service
public class DataLoaderServiceImpl implements DataLoaderService{

	@Value("classpath:simple_data.json")
	private Resource jsonResouece;

	@Value("classpath:MCC_LAWS_OF_CRICKET.pdf")
	private Resource pdfResouece;
	
	@Override
	public List<Document> loadDocumentFromJson() {
		var jsonReader = new JsonReader(jsonResouece);
		List<Document> listOfDocument = jsonReader.read();
		System.out.println("Calling loadDocumentFromJson : "+listOfDocument);
		return listOfDocument;
	}

	@Override
	public List<Document> loadDocumentFromPdf(){
		PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(pdfResouece,
				PdfDocumentReaderConfig.builder()
					.withPageTopMargin(0)
					.withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
						.withNumberOfTopTextLinesToDelete(0)
						.build())
					.withPagesPerDocument(1)
					.build());

		return pdfReader.read();
    }
}
