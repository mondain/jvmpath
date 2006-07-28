package org.gregoire.tools.ant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

/**
 * <p>Loads a native library file. The class can load the library by attempting to
 * have the System class load the library from the library path. If this fails the
 * class can attempt to load the file by extracting it from the classpath,
 * including the Jar files on the classpath and then loading it with the full
 * file path. A temporary directory is used to temporarily extract the file.
 * If the file is loaded successfully the library name is placed in a hashtable
 * so that subsequent calls to load the library to not actually attempt to
 * load the library.</p>
 * <p>Java Webstart and JNLP can also fullfill a similar role to this utility class
 * using the 'native' library element in the JNLP file.</p>
 * <p> Copyright (c) Xoetrope Ltd., 2002-2004</p>
 * <p> $Revision: 1.22 $</p>
 * <p> License: see License.txt</p>
 */
public class LibraryLoader
{
  /**
   * The internal buffer length
   */
  protected final static int BUF_LEN = 4096;
  
  /**
   * The directory to which files are extracted.
   */
  protected static String extractDir = null;
  
  private static Hashtable loadedResources = new Hashtable();
  private static long sourceModDate = 0l;

  /**
   * Load a library resource (native library)
   * @param clazz the class loader 
   * @param resName the resource to load
   */
  public static void load( ClassLoader clazz, String resName )
  {
    LibraryLoader ll = new LibraryLoader();
    ll.load( clazz, resName, true, false );
  }

  /**
   * Extract a file from a jar file to the specified location
   * @param classLoader the classloder to use to load the file
   * @param resName the resource/file name
   * @throws java.io.IOException IO problems reading or extracting the file.
   * @return the path to which the file is extracted
   */
  public static String extractFile( ClassLoader classLoader, String resName ) throws IOException
  {
    LibraryLoader ll = new LibraryLoader();
    return ll.extractFile( classLoader, resName, false );
  }

  /**
   * Load a native library
   * @param classLoader the classLoader used to find the library/resource
   * @param resName the library/resource name
   * @param checkLibPath true to check the system library path first before checking the resource path
   * @param overwrite true to overwrite any existing temporary file, if false the the timestamp is checked to see
   * if the file needs to be extracted to the temporary directory
   */
  public void load( ClassLoader classLoader, String resName, boolean checkLibPath, boolean overwrite )
  {
    if ( loadedResources.get( resName ) == null ) {
      boolean loadedFromSystem = checkLibPath;
      if ( checkLibPath ) {
        // Attempt to load the library from the system library path
        try {
          System.load( resName );
        }
        catch ( SecurityException sec ) {
          loadedFromSystem = false;
        }
        catch ( UnsatisfiedLinkError ul ) {
          loadedFromSystem = false;
        }
      }

      try {
        // If the file is not loaded then try extracting it to a temporary file and
        // loading it directly
        if ( !loadedFromSystem ) {
          String extractedFile = extractFile( classLoader, resName, overwrite );
          System.load( extractedFile );
        }

        // Save the resource name so that it is not reloaded
        loadedResources.put( resName, resName );
      }
      catch ( IOException ex ) {
        ex.printStackTrace();
      }
    }
  }

  /**
   * Get the directory name to which the library will be extracted.
   * @return the path
   */
  protected String getExtractDirectory()
  {
    if ( extractDir == null ) {
      try {
        File tempFile = File.createTempFile( "xui", ".dll" );
        String path = tempFile.getPath();
        extractDir = path.substring( 0, path.lastIndexOf( File.separatorChar ) + 1 );

        tempFile.delete();
      }
      catch ( IOException ex ) {
      }
    }
    return extractDir;
  }

  /**
   * Check that the temporary file exists and is up-to-date
   * @param url the URL of the source file
   * @param targetFile the temporary file
   * @return true if the file exists and is up-to-date
   */
  protected boolean isUpToDate( URL url, File targetFile )
  {
    try {
      long targetModDate = 0l;
      sourceModDate = 0l;

      if ( targetFile.exists() ) {
        targetModDate = targetFile.lastModified();
      }
      else {
        return false; // The file does not exist
      }

      String path = url.getFile();
      // For a Jar file the path will be something like c:\tmp\myjar.jar!/mypackage/mydll.dll
      if ( path.indexOf( ".jar" ) > 0 ) {
        path = path.substring( 6 );
        int separatorPos = path.indexOf( '!' );

        // Skip the first forward slash
        String searchPath = path.substring( separatorPos + 2 );
        searchPath = searchPath.replace( '\\', '/' );
        JarFile jf = new JarFile( path.substring( 0, separatorPos ) );
        JarEntry je = jf.getJarEntry( searchPath );
        sourceModDate = je.getTime();
      }
      else {
        File _file = new File( url.getFile() );
        sourceModDate = _file.lastModified();
      }

      // For the same version of the file the dates should match
      return ( sourceModDate == targetModDate );
    }
    catch ( IOException ex ) {
      return false;
    }
  }

  /**
   * Extract a file from a jar file to the specified location
   * @param classLoader the classloder to use to load the file
   * @param overwrite true to overwrite any existing file
   * @param resName the resource to extract
   * @return the path to which the file is extracted
   * @throws IOException IO problems reding or extracting the file
   */
  public String extractFile( ClassLoader classLoader, String resName, boolean overwrite ) throws IOException
  {
    URL url = classLoader.getResource( resName );
    String targetPath = getExtractDirectory() + resName;
    File targetFile = new File( targetPath );
    if ( !overwrite && ( url != null ))
      overwrite = !isUpToDate( url, targetFile );

    if ( overwrite ) {
      FileOutputStream fos = new FileOutputStream( targetFile );

      InputStream is = classLoader.getResource( resName ).openStream();
      byte[] c = new byte[ BUF_LEN ];

      int i = 1;
      String result = new String( "" );
      while ( i != -1 ) {
        i = is.read( c, 0, BUF_LEN );
        if ( i > 0 ) {
          fos.write( c, 0, i );
        }
      }

      fos.flush();
      fos.close();

      // Set the last moified file date
      targetFile.setLastModified( sourceModDate );
    }

    return targetPath;
  }
}