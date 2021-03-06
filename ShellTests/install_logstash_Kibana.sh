************************************************
************************************************

Install elasticsearch

wget https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-0.90.0.noarch.rpm
rpm -ivh elasticsearch-0.90.0.noarch.rpm

Open the elasticsearch.yml file in an editor of choice


# vi /etc/elasticsearch/elasticsearch.yml
Make some basic changes to it.


cluster.name: Avengers
node.name: “Avengers”
path.conf: /etc/elasticsearch
path.data: /var/lib/elasticsearch
path.logs: /var/log/elasticsearch
network.host: <Public IP>

Install Paramedic -:
/usr/share/elasticsearch/bin/plugin -install karmi/elasticsearch-paramedic

URL to Access Paramedic -:http://54.236.233.141:9200/_plugin/paramedic/index.html

************************************************
************************************************

Install Logstash -:

wget https://logstash.objects.dreamhost.com/release/logstash-1.1.9-monolithic.jar

java -jar ./logstash-1.1.9-monolithic.jar agent -f /etc/logstash/logstash.conf -- web --backend elasticsearch://localhost/?local

 mkdir -p /etc/logstash
 vi /etc/logstash/logstash.conf
Append the following to it.


input {
file {
type => "remote-syslog"
# Wildcards work, here :)
path => [ "/var/log/remote/remotesys.log" ]
}
}
filter {
grok {
type => "remote-syslog"
pattern => [ "%{SYSLOGBASE}" ]
}
grep {
type => "remote-syslog"
match => [ "@message", "apache-access:" ]
add_tag => "apache-access"
drop => false
}
grok {
type => "remote-syslog"
tags => ["apache-access"]
pattern => [ "%{COMBINEDAPACHELOG}" ]
}
}
output {
elasticsearch {
}}


logstash URL : http://10.190.2.87:9292/search

************************************************
************************************************


-- Install Ruby 1.8.7 -:

yum install -y gcc zlib zlib-devel
wget ftp://ftp.ruby-lang.org/pub/ruby/1.8/ruby-1.8.7-p330.tar.gz
tar xvf ruby-1.8.7-p330.tar.gz
cd ruby-1.8.7-p330
./configure --enable-pthread --prefix=/usr
make
make install

Remove old version 1.3.1 using
rpm -qa | grep ruby
rpm --erase rubygems-1.3.1-1

-- install rubygems using : 
1.) wget http://rubyforge.org/frs/download.php/69365/rubygems-1.3.6.tgz
2.) ruby setup.rb
3.) gem update –system

************************************************
************************************************

Install kibana using -:

wget http://github.com/rashidkpc/Kibana/tarball/kibana-ruby
cd rashidkpc-Kibana-e8fe7f2/
mkdir tmp
tar zxvf kibana-ruby
/usr/local/bin/gem install bundler

vi KibanaConfig.rb
Make sure the following feilds are set.
KibanaHost = '0.0.0.0'

URL to Check Kibana : http://ec2d-logui-01.dyn.mypna.com:5601/

************************************************
************************************************

Install Redis

yum install redis

vi /etc/redis.conf

bind 0.0.0.0 logt