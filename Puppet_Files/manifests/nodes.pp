node 'ec2d-rajpuppet' {
 file { '/tmp/newhello.txt':
  content => "This is a second test file of Puppet\n",

 }
}

