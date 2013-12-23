            Oracle Coherence Java Examples
            =======================================================

Contents
========

    * Overview
    * Directory Structure
    * Build Instructions
    * Run Instructions
    * References

Overview
========

 This document describes the source and build system for the Oracle Coherence Java examples.


Prerequisites
==================
  In order to build and run the examples, you must have Coherence version 3.6 or later and a Java 
  development kit (JDK) 1.5 or greater.
  
  $COHERENCE_HOME   
    Make sure that the COHERENCE_HOME enviroment variable points to the location of the unpacked Coherence 3.6
    (or later) directory.
  
  $JAVA_HOME
    Make sure that the JAVA_HOME environment variable points to the location of a 1.5 or greater JDK before
    building the examples. A Java runtime 1.5 or greater is needed to run the examples.


Directory Structure
======================

  The directory structure described below is relative to ../examples/

  java/bin
    Scripts for building and executing examples. There are two sets of scripts. Scripts with no file extension
    are bash scripts. Scripts with a .cmd file extension are Windows command scripts. The following description
    refers to the script names without specifying any file extension.
    
    build               -- builds an example
    run                 -- runs an example
    run-cache-server    -- run the cluster "server" used for examples
    run-proxy           -- run a proxy node. Optional for some java examples. The proxy is used by a
                           Coherence*Extend client.


  java/src
    All example source. The examples are in the com.tangosol.examples.<example name> package. The classes
    for objects stored in the cache used by the examples are in the com.tangosol.examples.pof package. 

  java/classes
     The class files output from a build. This directory will not exist until the build
     script is executed.

  java/resource/config
     The common coherence configuration files required by the examples.  
  
  java/resource/<example name>
    If an example has configuration that is required instead of the common configuration, it will 
    have its own directory. The security example uses configuration files from java/resource/security.

  resource
    The data file used for the contacts LoaderExample: contacts.csv. 

  $COHERENCE_HOME/lib 
    Coherence libraries used for compiling and running the examples.

     
Build Instructions
==================
 
  Execute the build script with the name of the example: "bin/build contacts" or "bin/build security".
  The script will build the pof package files and then the files for the particular example.

Run Instructions
=================

  Execute the run script. There are two parts to running the example.

  contacts example
      First, start one or more cache servers: "bin/run-cache-server". 
      Each execution will start a cache server cluster node. To add additional 
      nodes, execute the command in a new command shell.

      Second, in a new command shell, run with the name of the example: "bin/run contacts".
      The Driver.main method will run through the features of the example with output
      going to the command window (stdout).
      
      Starting with Coherence 3.6, an example of the new Query Language feature was added.  
      This example shows how to configure and use a simple helper class "FilterFactory" using the 
      Coherence InvocationService.
      
  security example
      The security example requires Coherence*Extend, which uses a proxy.
       
      First start a proxy: "bin/run-proxy security".
       
      Optionally, start one or more cache servers as described in the contacts example. The proxy
      is storage enabled, so it will act as both a proxy and a cache server node.
       
      Second, in a new command shell, run with the name of the example: "bin/run security".
      The Driver.main method will run through the features of the example with output
      going to the command window (stdout).


References
==========

  Coherence:

  * Getting Started:
    http://www.oracle.com/technology/products/coherence/index.html

