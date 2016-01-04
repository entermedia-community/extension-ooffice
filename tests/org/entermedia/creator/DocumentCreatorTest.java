package org.entermedia.creator;

import org.entermediadb.asset.Asset;
import org.entermediadb.asset.BaseEnterMediaTest;
import org.entermediadb.asset.convert.ConvertInstructions;
import org.entermediadb.asset.convert.ConvertResult;
import org.entermediadb.asset.convert.MediaConverter;
import org.openedit.page.Page;

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
		MediaConverter creator = (MediaConverter)getFixture().getModuleManager().getBean("oofficeDocumentCreator");
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
