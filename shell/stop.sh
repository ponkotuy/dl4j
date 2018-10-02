#!/bin/sh

INS_OPTS="--region us-west-2 --instance-ids i-0db6de9ed3aaa1e24"

aws ec2 stop-instances $INS_OPTS && aws ec2 wait instance-stopped $INS_OPTS
