#!/bin/bash

cd `dirname $0`/..
EXAMPLES_DIR=`pwd`
EXAMPLE_PATH_BEGIN=com/tangosol/examples

. ./bin/function_set_example_env
. ./bin/function_set_env

set_example_env $1
set_env

# Perform build
echo building $EXAMPLE from $EXAMPLE_PATH ...

mkdir -p $CLASSES_DIR
$JAVA_HOME/bin/javac -d $CLASSES_DIR -source 1.5 -cp $CP $(find $EXAMPLES_DIR/src/$POF_PATH -name "*.java")
$JAVA_HOME/bin/javac -d $CLASSES_DIR -source 1.5 -cp $CP $(find $EXAMPLES_DIR/src/$EXAMPLE_PATH -name "*.java")

echo To run this example, execute \'run $EXAMPLE\'
