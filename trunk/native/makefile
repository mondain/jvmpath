# define release and debug file suffixes
OBJ_REL_EXT = rel_
OBJ_DBG_EXT = dbg_
.SUFFIXES : .c .cpp .$(OBJ_REL_EXT)o .$(OBJ_DBG_EXT)o

CC = gcc
CPP = g++

CC_DEFINES =
CC_FLAGS = -O2 -Wall
CC_INCLUDES = -I../include -I../../include -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux

CPP_DEFINES =
CPP_FLAGS = -O2 -Wall
CPP_INCLUDES = -I../include -I../../include -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux

CC_DEFINES =
CC_DBG_FLAGS = -O -g -Wall
CC_DBG_INCLUDES = $(CC_INCLUDES)

CPP_DBG_DEFINES =
CPP_DBG_FLAGS = -g -O -Wall
CPP_DBG_INCLUDES = $(CPP_INCLUDES)

LDFLAGS = -fPIC -shared
LDFLAGS_REL = $(LDFLAGS)
LDFLAGS_DBG = $(LDFLAGS)

LIBS = -lm 

DIST_DIR = ../../dist
FILTERS_DIR = $(DIST_DIR)
OUTDIR_REL = $(FILTERS_DIR)
OUTDIR_DBG = $(FILTERS_DIR)/Debug

PATH_SRC = src/linux/PathTask.cpp

PATH_REL = $(OUTDIR_REL)/libpath.so
PATH_DBG = $(OUTDIR_DBG)/libpath.so

PATH_REL_OBJ = $(PATH_SRC:.cpp=.$(OBJ_REL_EXT)o)
PATH_DBG_OBJ = $(PATH_SRC:.cpp=.$(OBJ_DBG_EXT)o)

$(PATH_REL): $(PATH_REL_OBJ)
	$(CPP) $(LDFLAGS_REL) $(PATH_REL_OBJ) $(LIBS) -o $@

$(PATH_DBG): $(PATH_DBG_OBJ)
	$(CPP) $(LDFLAGS_DBG) $(PATH_DBG_OBJ) $(LIBS) -o $@

rel: $(PATH_REL)
dbg: $(PATH_DBG)

all: rel

.cpp.$(OBJ_REL_EXT)o:
	$(CPP) -c $< $(CPP_FLAGS) $(CPP_DEFINES) $(CPP_INCLUDES) -o $@

.c.$(OBJ_REL_EXT)o:
	$(CC) -c $< $(CC_FLAGS) $(CC_DEFINES) $(CC_INCLUDES) -o $@

.cpp.$(OBJ_DBG_EXT)o:
	$(CPP) -c $< $(CPP_DBG_FLAGS) $(CPP_DBG_DEFINES) $(CPP_DBG_INCLUDES) -o $@

.c.$(OBJ_DBG_EXT)o:
	$(CC) -c $< $(CC_DBG_FLAGS) $(CC_DBG_DEFINES) $(CC_DBG_INCLUDES) -o $@

rebuild: clean all

clean:
	rm -f $(PATH_REL)
	rm -f $(PATH_REL_OBJ)
	rm -f $(PATH_DBG)
	rm -f $(PATH_DBG_OBJ)
