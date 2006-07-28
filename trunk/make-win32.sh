BASEDIR=`pwd`
echo "BASEDIR=" $BASEDIR

# clean and build each source
    echo "Building" 
    cd "native"
    if [ -f path.mak ]
    then
	nmake /f path.mak clean
	nmake /f path.mak
    fi
    cd $BASEDIR

echo "Build complete"
