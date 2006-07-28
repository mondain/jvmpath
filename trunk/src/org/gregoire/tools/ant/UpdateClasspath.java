package com.streamray.ant;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;

/**
 * Allows updating of the system classpath via a classpath reference
 *
 * @author Paul Gregoire 11/15/2001
 */
public class UpdateClasspath extends Task {

   private static final String TRUE = "true";
   private static final String YES = "yes";
   private Path classpath;

   private org.apache.tools.ant.Project antProject;

    /**
     * Set the classpath
     */
    public void setClasspathref(Reference cpReference){
        classpath = new Path(this.getProject()).createPath();      
        classpath.setRefid(cpReference);               
    }

   /**
    * Updated the classpath  
    */
   public void execute() throws BuildException {

	  System.out.println("\tUpdating system classpath...");

	  antProject = this.getProject();

	  String pathSep = System.getProperty("path.separator");

	  String cp = antProject.getProperty("java.class.path") + pathSep + classpath.toString();
	  System.setProperty("java.class.path", cp);

	  System.out.println("\tUpdated classpath: " + System.getProperty("java.class.path"));
	  System.out.println();
   }
}

