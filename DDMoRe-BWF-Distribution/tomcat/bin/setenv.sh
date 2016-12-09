export JAVA_OPTS="$JAVA_OPTS\
 -Xms1024m\ 
 -Xmx2048m\ 
 -XX:PermSize=512m\ 
 -XX:MaxPermSize=512m\
 -Dorg.apache.el.parser.COERCE_TO_ZERO=false\
 -Dspring.profiles.active=database"
 