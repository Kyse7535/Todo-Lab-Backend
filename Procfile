web: java --add-opens java.base/java.lang=ALL-UNNAMED \
     --add-opens java.base/java.util=ALL-UNNAMED \
     -jar $JAVA_OPTS -Dserver.port=$PORT target/dependency/webapp-runner.jar \
      --session-store redis target/todo-0.0.1-SNAPSHOT.war