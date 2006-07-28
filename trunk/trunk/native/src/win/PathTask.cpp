/*
    PathTask.cpp
    
    This is based on original work done by Nate Roe.

    @author Nate Roe
    @author Paul Gregoire (mondain@gmail.com)

*/
#include "PathTask.h"

#include <iostream>
#include <windows.h>

JNIEXPORT void JNICALL Java_org_gregoire_tools_PathTask_addToPath(JNIEnv *env, jclass clazz, jstring jpath) {

	char *oldPath = getenv("PATH");

	const char *path = env->GetStringUTFChars(jpath, 0);
	if (path) {
		int oldLen = strlen(oldPath);

		char *newPath = new char[oldLen + strlen(path) + 7];

		strcpy(newPath, "PATH=");
		strcpy(newPath + 5, oldPath);
		strcpy(newPath + oldLen + 5, ";");
		strcpy(newPath + oldLen + 6, path);

		_putenv(newPath);

		delete [] newPath;
		env->ReleaseStringUTFChars(jpath, path);
	} else {
		// something went wrong, report or throw or something.
	}
}

