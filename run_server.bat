@echo off
title Cryptic Game Server
echo Starting Server...
java --add-opens java.base/java.time=ALL-UNNAMED -jar target/Cryptic-GameServer-1.1.1-shaded.jar
pause
