require 'rubygems'
require 'net/http'
require 'json'
require "uri"

#http://jira.telenav.com:8080/rest/api/latest/issue/SCOUTIOS-116.json
 
jira_url = "http://jira.telenav.com:8080/rest/api/latest/issue/"
issue_keys = %w[SCOUTIOS-115 SCOUTIOS-116]
json_ext = ".json"
 
for issue in issue_keys

	uri = URI.parse(jira_url + issue + json_ext)

	http = Net::HTTP.new(uri.host, uri.port)
	request = Net::HTTP::Get.new(uri.request_uri)
	request.basic_auth("trapuser@telenav.com", "trap_deploy123")
	response = http.request(request)
 
    if response.code =~ /20[0-9]{1}/
        data = JSON.parse(response.body)
        fields = data.keys
 
        #puts "Output for issue " + issue

        puts "Issue Id :" + data["id"] + ", Reporter:" + data["fields"]["reporter"]["name"] + ", Summary: " + data["fields"]["summary"] 
        puts "\n"#extra line feed for readability
    else
     raise StandardError, "Unsuccessful response code " + response.code + " for issue " + issue
    end
end
