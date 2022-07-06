@echo off

echo.
echo ###############################################################################
echo # Start - %~n0%~x0 (%0)
echo ###############################################################################
echo.

java -jar C:/Workspace/uf/pdf-manipulate-metadata/target/pdf-manipulate-metadata-jar-with-dependencies.jar %1%

echo.
echo ###############################################################################
echo # Ende
echo ###############################################################################
echo.

echo on
