#!/bin/sh

INS_OPTS="--region us-west-2 --instance-ids i-0db6de9ed3aaa1e24"

aws ec2 start-instances $INS_OPTS && aws ec2 wait instance-running $INS_OPTS

ssh `aws ec2 describe-instances $INS_OPTS | jq -r .Reservations[0].Instances[0].PublicDnsName`
