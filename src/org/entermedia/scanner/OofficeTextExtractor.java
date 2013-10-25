package org.entermedia.scanner;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openedit.entermedia.Asset;
import org.openedit.entermedia.MediaArchive;
import org.openedit.entermedia.creator.ConvertInstructions;
import org.openedit.entermedia.creator.ConvertResult;
import org.openedit.entermedia.creator.MediaCreator;
import org.openedit.entermedia.scanner.MetadataExtractor;
import org.openedit.entermedia.scanner.MetadataPdfExtractor;
import org.openedit.repository.ContentItem;

import com.openedit.page.Page;
import com.openedit.util.PathUtilities;

public class OofficeTextExtractor extends MetadataExtractor
{
	public static final Collection FORMATS = Arrays.asList(new String[] {"doc","docx","rtf","ppt","pptx","wps","odt","html","xml","csv", "xls", "xlsx"});

	private static final Log log = LogFactory.getLog(OofficeTextExtractor.class);
	protected MetadataPdfExtractor fieldMetadataPdfExtractor;
	protected MediaCreator fieldOofficeDocumentCreator;
	
	public MediaCreator getMediaCreator()
	{
		return fieldOofficeDocumentCreator;
	}

	public void setMediaCreator(MediaCreator inOofficeDocumentCreator)
	{
		fieldOofficeDocumentCreator = inOofficeDocumentCreator;
	}

	public MetadataPdfExtractor getMetadataPdfExtractor()
	{
		return fieldMetadataPdfExtractor;
	}

	public void setMetadataPdfExtractor(MetadataPdfExtractor inMetadataPdfExtractor)
	{
		fieldMetadataPdfExtractor = inMetadataPdfExtractor;
	}

	public boolean extractData(MediaArchive inArchive, ContentItem inputFile, Asset inAsset)
	{
		String type = PathUtilities.extractPageType(inputFile.getPath());
		if (type == null || "data".equals(type.toLowerCase()))
		{
			type = inAsset.getFileFormat();
		}

		if (type != null)
		{
			type = type.toLowerCase();
		}
		if( type == null )
		{
			return false;
		}
		if ( type.equals("pdf"))
		{
			return false; //PDF's are already being extracted
		}
		
		if( !FORMATS.contains(type) )
		{
			return false;
		}
		//ooffice can create a PDF then we can pull the size and text from it
		ConvertInstructions inst = new ConvertInstructions();
		inst.setAssetSourcePath(inAsset.getSourcePath());
		inst.setOutputExtension("pdf");
		String outputpage = "/WEB-INF/data/"+ inArchive.getCatalogId() + "/generated/" + inAsset.getSourcePath() + "/document.pdf";
		
		//String tmppath = getMediaCreator().populateOutputPath(inArchive, inst);
		
		Page out = inArchive.getPageManager().getPage(outputpage);
		if( !out.exists() || out.getContentItem().getLength()==0)
		{
			//Create PDF
			ConvertResult tmpresult = getMediaCreator().convert(inArchive, inAsset, out, inst);
			if( !tmpresult.isOk() )
			{
				return false;
			}
		}	
		//now use the PDF extractor
		getMetadataPdfExtractor().extractData(inArchive, out.getContentItem(), inAsset);
		
//		//now get the page info out of the PDF?
//		Asset tmp = inArchive.createAsset("tmp/" + inAsset.getSourcePath());
//		getPdfMetadataExtractor().extractData(inArchive, pdf, tmp);
//		
//		inAsset.setProperty(type, tmppath)
		
		return true;
	}
}
