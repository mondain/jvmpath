package org.gregoire.tools;


/**
 * Simple utility to add a new path to the system path for the current process.
 * This is intended to be used with DLLs that reference _other_ DLLs, which
 * are loaded using only the system path, not the path specified with
 * java.load.library.
 *
 * @author Paul Gregoire (mondain@gmail.com)
 */
public class PathUpdate {

    /**
     * Add the specified path to the environment path of the current process.
     * The specified path is appended to the current path.
     * <blockquote>
     * PATH = %PATH%;&lt;path&gt;
     * </blockquote>
     * <p>
     * This only affects the current process, and so can be run using any user permissions.
     *
     * @param path The path fragment that should be appended to the system path.
     */
    public static native void addToPath(String path);
    
}