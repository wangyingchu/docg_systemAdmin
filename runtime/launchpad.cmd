@echo off
setlocal EnableDelayedExpansion
set EXTCLASS=extCLASS\*
set LIB=lib\*
"java" -XX:+UseG1GC -XX:+DisableExplicitGC -Xmx10G -Xrs ^
--add-opens=jdk.management/com.sun.management.internal=ALL-UNNAMED  ^
--add-opens=java.base/jdk.internal.misc=ALL-UNNAMED  ^
--add-opens=java.base/sun.nio.ch=ALL-UNNAMED  ^
--add-opens=java.management/com.sun.jmx.mbeanserver=ALL-UNNAMED  ^
--add-opens=jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED  ^
--add-opens=java.base/sun.reflect.generics.reflectiveObjects=ALL-UNNAMED  ^
--add-opens=java.base/java.io=ALL-UNNAMED  ^
--add-opens=java.base/java.nio=ALL-UNNAMED  ^
--add-opens=java.base/java.util=ALL-UNNAMED  ^
--add-opens=java.base/java.lang=ALL-UNNAMED  ^
--add-opens=java.base/java.lang.invoke=ALL-UNNAMED  ^
--add-opens=java.base/sun.security.action=ALL-UNNAMED  ^
-jar docg-sac-1.0.1.jar