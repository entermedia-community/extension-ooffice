package org.entermedia.creator;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.openedit.entermedia.Asset;
import org.openedit.entermedia.BaseEnterMediaTest;
import org.openedit.entermedia.creator.ConvertInstructions;
import org.openedit.entermedia.creator.ConvertResult;

import com.openedit.page.Page;

public class DocumentCreatorTest extends BaseEnterMediaTest
{

	public void testConvert() throws Exception
	{
		ConvertInstructions inst = new ConvertInstructions();
		
		String sourcepath = "test/test.doc";
		Asset asset = getMediaArchive().createAsset(sourcepath);
		asset.setProperty("fileformat", "doc");
		inst.setAssetSourcePath(asset.getSourcePath());
		inst.setOutputExtension("pdf");
		OofficeDocumentCreator creator = (OofficeDocumentCreator)getFixture().getModuleManager().getBean("oofficeDocumentCreator");
		String tmppath = creator.populateOutputPath(getMediaArchive(), inst);
		
		Page out = getFixture().getPageManager().getPage(tmppath);
		getFixture().getPageManager().removePage(out);
		assertFalse(out.exists());
		if( !out.exists() || out.getContentItem().getLength()==0)
		{
			//Create 
			ConvertResult tmpresult = creator.convert(getMediaArchive(), asset, out, inst);
			assertTrue( tmpresult.isOk() );
			assertTrue(out.exists());
		}	
		
	}
}
