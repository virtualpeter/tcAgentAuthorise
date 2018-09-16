# tcAgentAuthorise

A server-side plugin for teamcity to implement auto-agent-authorising

Author: Peter Viertel - @virtualpete


# creation

* Requires Java JDK version 1.6 or later - I have been using openjdk 8.

* Guide to creating plugins is here: https://confluence.jetbrains.com/display/TCD18/Getting+Started+with+Plugin+Development

```
$ mvn org.apache.maven.plugins:maven-archetype-plugin:2.4:generate -DarchetypeRepository=http://download.jetbrains.com/teamcity-repository -DarchetypeArtifactId=teamcity-server-plugin -DarchetypeGroupId=org.jetbrains.teamcity.archetypes -DarchetypeVersion=RELEASE
```

## instructions from archetype readme.txt
 1. Implement
 Put your implementing classes to "tcAgentAuthorise-server" module. Do not forget to update spring context file in 'main/resources/META-INF'. See TeamCity documentation for details.

 2. Build
 Issue 'mvn package' command from the root project to build your plugin. Resulting package tcAgentAuthorise.zip will be placed in 'target' directory. 
 
 3. Install
 To install the plugin, put zip archive to 'plugins' dir under TeamCity data directory and restart the server.

 
## Acknowledgement

This project here: https://plugins.jetbrains.com/plugin/9303-agent-auto-authroize has worked fine for years - however its using an older plugin framework and i want to do a lot more things so im starting from the teamcity guide and the basic method i use was learnt from looking at this older project.
