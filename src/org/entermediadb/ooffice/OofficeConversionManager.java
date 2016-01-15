package org.entermediadb.ooffice;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.entermediadb.asset.Asset;
import org.entermediadb.asset.MediaArchive;
import org.entermediadb.asset.convert.BaseConversionManager;
import org.entermediadb.asset.convert.ConvertInstructions;
import org.entermediadb.asset.convert.ConvertResult;
import org.openedit.page.Page;
import org.openedit.repository.ContentItem;
import org.openedit.util.PathUtilities;

public class OofficeConversionManager extends BaseConversionManager
{
	private static final Log log = LogFactory.getLog(OofficeConversionManager.class);

	@Override
	public ContentItem findOutputFile(ConvertInstructions inStructions)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ContentItem createCacheFile(ConvertInstructions inStructions, ContentItem inInput)
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected String getCacheName()
	{
		return "document";
	}


}