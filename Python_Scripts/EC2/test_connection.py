import boto.ec2

#conn = boto.ec2.connect_to_region("us-west-2",aws_access_key_id='AKIAIK7M6GAZAVLEBV3A',aws_secret_access_key='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')
conn = boto.ec2.connect_to_region('us-west-2')

reservations = conn.get_all_reservations()

print reservations