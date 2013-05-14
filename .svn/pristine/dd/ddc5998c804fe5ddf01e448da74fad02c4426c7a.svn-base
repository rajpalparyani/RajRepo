#Manage Nginx WebServer
class nginx {
 package { 'nginx':
  ensure =>latest,
 }

 service { 'nginx':
  ensure => running,
  require => Package['nginx'],
  enable => true,
 }


}

