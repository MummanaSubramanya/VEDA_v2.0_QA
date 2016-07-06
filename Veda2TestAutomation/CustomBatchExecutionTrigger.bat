:: Right now we're in the eclipse working directory
@echo %cd%
@cd /D %~dp0
:: We changed the directory to the script location
@echo %cd%
:: We now can use workspace relative paths!
ant -f build.xml
pause
