rm -rf org
cp -r ../CompiledServer/production/09Scape/org .
java -server -cp ".:./bin;.:./lib/*" org.keldagrim.Management false true false true true
