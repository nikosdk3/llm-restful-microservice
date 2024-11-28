#!/bin/bash

# Test Successful Query

API_URL="http://52.91.2.250:8080/query"
API_KEY="" # Replace with a valid OpenAI API key

# Make a POST request
response=$(curl -s -w "%{http_code}" -o response.json -X POST "$API_URL" \
  -H "Authorization: Bearer $API_KEY" \
  -H "Content-Type: application/json" \
  -d 'What is the capital of France?')

# Check if the response code is 200 (OK)
if [ "$response" -eq 200 ]; then
  echo "Test Passed: Valid query returned a 200 OK response."
else
  echo "Test Failed: Expected 200 OK, but got $response."
fi
