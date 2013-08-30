#!/bin/sh

if [ -z "$JAVA_HOME" ] ; then
    echo "environment variable JAVA_HOME must be set"
    exit 1
fi

export MADCOW_LIB_CLASSPATH=`find ./.madcow/lib -name "*.jar" | tr "\n" ":"`
export MADCOW_CONF_CLASSPATH=./conf
export PROJECT_LIB_CLASSPATH=`find ./lib -name "*.jar" | tr "\n" ":"`

export CLASSPATH=$MADCOW_LIB_CLASSPATH:$MADCOW_CONF_CLASSPATH:$PROJECT_LIB_CLASSPATH

#echo "Running Madcow 2.0 using classpath"
#echo $CLASSPATH

$JAVA_HOME/bin/java $JAVA_OPTS -classpath $CLASSPATH au.com.ps4impact.madcow.execution.MadcowCLI $@
