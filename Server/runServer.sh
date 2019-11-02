rm -rf org
cp -r ../CompiledServer/production/09Scape/org .
java -server -cp ".:./bin;.:./data/libs/*" org.crandor.Server 
