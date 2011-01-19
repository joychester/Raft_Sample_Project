@echo off
set DT_IE_AGENT_ACTIVE=true
set DT_IE_SESSION_NAME=dynaTrace_Automation

java -cp ..\Classes;..\lib\*; raft.engine.TestEngine > log.txt