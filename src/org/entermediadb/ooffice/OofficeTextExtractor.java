package org.entermediadb.ooffice;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.entermediadb.asset.Asset;
import org.entermediadb.asset.MediaArchive;
import org.entermediadb.asset.convert.ConversionManager;
import org.entermediadb.asset.convert.ConvertInstructions;
import org.entermediadb.asset.convert.ConvertResult;
import org.entermediadb.asset.convert.MediaTranscoder;
import org.entermediadb.asset.scanner.MetadataExtractor;
import org.entermediadb.asset.scanner.MetadataPdfExtractor;
import org.openedit.repository.ContentItem;
import org.openedit.util.PathUtilities;

public class OofficeTextExtractor extends MetadataExtractor
{
	public static final Collection FORMATS = Arrays.asList(new String[] {"doc","docx","rtf","ppt","pptx","wps","odt","html","xml","csv", "xls", "xlsx"});

	private static final Log log = LogFactory.getLog(OofficeTextExtractor.class);
	protected MetadataPdfExtractor fieldMetadataPdfExtractor;
	protected MediaTranscoder fieldOofficeDocumentTranscoder;
	
	public MediaTranscoder getOofficeDocumentTranscoder()
	{
		return fieldOofficeDocumentTranscoder;
	}

	public void setOofficeDocumentTranscoder(MediaTranscoder inOofficeDocumentTranscoder)
	{
		fieldOofficeDocumentTranscoder = inOofficeDocumentTranscoder;
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
		ConversionManager manager = inArchive.getTranscodeTools().getManagerByTranscoder("ooffice");
		ConvertInstructions inst = manager.createInstructions();
		inst.setAssetSourcePath(inAsset.getSourcePath());
		inst.setOutputExtension("pdf");
		String outputpage = "/WEB-INF/data/"+ inArchive.getCatalogId() + "/generated/" + inAsset.getSourcePath() + "/document.pdf";
		
		ContentItem out = inArchive.getContent( outputpage);
		if( !out.exists() || out.getLength()==0)
		{
			//Create PDF
			inst.setInputFile(inputFile);
			inst.setOutputFile(out);
			ConvertResult tmpresult = manager.transcode(inst);
			if( !tmpresult.isOk() )
			{
				return false;
			}
		}	
		//now use the PDF extractor
		getMetadataPdfExtractor().extractData(inArchive, out, inAsset);
		
//		//now get the page info out of the PDF?
//		Asset tmp = inArchive.createAsset("tmp/" + inAsset.getSourcePath());
//		getPdfMetadataExtractor().extractData(inArchive, pdf, tmp);
//		
//		inAsset.setProperty(type, tmppath)
		
		return true;
	}
}
