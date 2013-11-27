require 'rubygems'
require 'net/http'
require 'json'
require "uri"

#http://jira.telenav.com:8080/rest/api/2/search?jql=project%3DSPEECHSVC%20AND%20fixVersion%3D19396
 
jira_url = "http://jira.telenav.com:8080/rest/api/2/search?jql=project%3D"
#project = "$[/myProject/JIRA_Project_Name]"
project = "SPEECHSVC"
limit1 = "%20AND%20fixVersion%3D"
#fixversion = "$[/myWorkflow/FixVersion]"
fixversion = "19396"
new_string=""


uri = URI.parse(jira_url + project + limit1 + fixversion)

http = Net::HTTP.new(uri.host, uri.port)
request = Net::HTTP::Get.new(uri.request_uri)
request.basic_auth("trapuser@telenav.com", "trap_deploy123")
response = http.request(request)
 
if response.code =~ /20[0-9]{1}/
	data = JSON.parse(response.body)
	fields = data.keys
	issuelist = data.fetch("issues")
	issuelist.each { |issue| new_string += "http://jira.telenav.com:8080/browse/" + issue.fetch("key") + "\n" }
else
	raise StandardError, "Unsuccessful response code " + response.code 
end

puts new_string