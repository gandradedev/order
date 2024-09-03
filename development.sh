#!/bin/bash

echo "##########"
echo "Create SQS queue example"
echo "aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name my_queue_name"
echo "##########"

echo "##########"
echo "List SQS queues"
echo "aws --endpoint-url http://localhost:4566 sqs list-queues"
echo "##########"

echo "##########"
echo "Create S3 bucket example"
echo "aws --endpoint-url=http://localhost:4566 s3 mb s3://my-bucket-name"
echo "##########"

echo "##########"
echo "List S3 buckets"
echo "aws --endpoint-url http://localhost:4566 s3 ls"
echo "##########"
