#!/usr/bin/ruby
require 'rubygems'
require 'json-schema'

if ARGV.length < 2
    print "\nUsage: jsonvalidate schema jsonfilename\n\n" 
    print "This file validates a json schema. The first parameter is the schema definition.\nThe second parameter is the json file to validate\n"
    print "use schema_view.json to validate a view schema and schema_engine.json to validate an engine schema\n"
    print "e.g. jsonvalidate schema_view.json mynewconfigfile.json\n\n"
    exit 1
end
    
print "Validating #{ARGV[0]}\n"

begin
    JSON::Validator.validate!(ARGV[0], ARGV[1])
rescue JSON::Schema::ValidationError
  print "File failed validation\n"
  puts $!.message
  exit 1
end
print "File validated OK\n"
exit 0
    
