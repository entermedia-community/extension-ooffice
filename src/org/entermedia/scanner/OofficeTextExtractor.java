package org.entermedia.scanner;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.entermedia.creator.OofficeDocumentCreator;
import org.openedit.entermedia.Asset;
import org.openedit.entermedia.MediaArchive;
import org.openedit.entermedia.creator.ConvertInstructions;
import org.openedit.entermedia.creator.ConvertResult;
import org.openedit.entermedia.creator.MediaCreator;
import org.openedit.entermedia.scanner.MetadataExtractor;
import org.openedit.entermedia.scanner.MetadataPdfExtractor;

import com.openedit.page.Page;
import com.openedit.util.PathUtilities;

public class OofficeTextExtractor extends MetadataExtractor
{
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

	public boolean extractData(MediaArchive inArchive, File inputFile, Asset inAsset)
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
		if (type != null && type.equals("pdf"))
		{
			return false; //PDF's are already being extracted
		}
		String format = inArchive.getMediaRenderType(type);
		if( !format.equals("document"))
		{
			return false;
		}
		//ooffice can create a PDF then we can pull the size and text from it
		ConvertInstructions inst = new ConvertInstructions();
		inst.setAssetSourcePath(inAsset.getSourcePath());
		inst.setOutputExtension("pdf");
		String tmppath = getMediaCreator().populateOutputPath(inArchive, inst);
		
		Page out = inArchive.getPageManager().getPage(tmppath);
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
		File pdf = new File( out.getContentItem().getAbsolutePath() );
		getMetadataPdfExtractor().extractData(inArchive, pdf, inAsset);
		return true;
	}
}
