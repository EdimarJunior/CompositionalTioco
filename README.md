This repository presents the generated code and the graphic results contained in the articles "Testing Real-Time Systems from Compositional Symbolic Specifications" submitted to the Software Tools for Technology Transfer Journal (STTT) and "Testing interruptions for the tioco conformance relation" submitted to IEEE Symposium on Real-time Computing (ISORC).

Our main objectives are to allow the SYMBOLRT tool to use the sequential, parallel and interruption compositional operators. For this, we present initial results towards test case generation of the following systems already presented in our articles:

* Choose; Pay

* Mouse || Screen

* (Target Designation ; Target Tracking ) || Radar

* Mouse Interrupted /\ Reset

* Cell Phone


*GETTING STARTED

Installing SYMBOLRT

- Download the binaries here http://www.dsc.ufcg.edu.br/~wilker/symbolrt/SYMBOLRT_1.4.zip.
- Unzip
- Install the dependences:
  * Install the CVC4 (remeber, CVC4) and edit the symbolrt.properties file in order to set the correct path.
  * Download the jgraphx package and extract the jgraphx.jar file. Put the extracted file in the lib folder.
  * Download the xstream-[version].jar file, rename the file to xstream.jar, and put it in the lib folder.
