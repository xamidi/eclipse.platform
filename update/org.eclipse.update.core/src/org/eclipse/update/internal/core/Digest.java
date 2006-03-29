package org.eclipse.update.internal.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.update.core.JarContentReference;
import org.eclipse.update.core.model.FeatureModel;
import org.xml.sax.SAXException;

public class Digest {
	
	private URL source;
	private File localSource;
	private JarFile digestJar;
	private InputStream inputStream;
	

	public Digest(URL source){
		this.source = source;
	}
	
	public FeatureModel[] parseDigest() throws IOException, CoreException, SAXException {
		DigestContentProvider digestContentProvider = new DigestContentProvider(source);
		localSource = digestContentProvider.asLocalReference(new JarContentReference( null, source), null).asFile();
		digestJar = new JarFile(localSource);
		
		ZipEntry digestEntry = digestJar.getEntry("digest.xml");
		
		if (digestEntry != null) {
			inputStream = digestJar.getInputStream(digestEntry);
			DigestParser digest = new DigestParser();
			digest.init(new LiteFeatureFactory());
			return digest.parse(inputStream);
		} else {
			throw new CoreException(null);
		}
	}

}