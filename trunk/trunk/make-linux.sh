#!/bin/sh

# clean and build 
    echo "Building"
    make -C "native/makefile" clean
    if [ $# -lt 1 ]
    then
	make -C "native/makefile"
    fi

echo "Build complete"
