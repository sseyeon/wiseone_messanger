#
# Enviroment Variable
#

# resolve links - $0 may be a softlink

this="$0"
while [ -h "$this" ]; do
  ls=`ls -ld "$this"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '.*/.*' > /dev/null; then
    this="$link"
  else
    this=`dirname "$this"`/"$link"
  fi
done

# convert relative path to absolute path
bin=`dirname "$this"`
script=`basename "$this"`
bin=`cd "$bin"; pwd`
this="$bin/$script"

UC_HOME=`dirname "$this"`/..
UC_CONF=$UC_HOME/conf
UC_LIB=$UC_HOME/lib

echo $UC_HOME
echo $UC_CONF
echo $UC_LIB
