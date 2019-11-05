rm -rf org
cp -r ../CompiledServer/production/09Scape/org .
rm -rf plugin/
cp -r ../CompiledServer/production/09Scape/org/crandor/plugin .
java -server -cp ".:./bin;.:./data/libs/*" org.crandor.Server 
